package ru.ip_fateev.lavka

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import ru.ip_fateev.lavka.Inventory.LocalData
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.common.Common
import ru.ip_fateev.lavka.cloud.model.Position
import ru.ip_fateev.lavka.cloud.model.ProductList
import ru.ip_fateev.lavka.cloud.model.Receipt
import ru.ip_fateev.lavka.cloud.model.ReceiptType
import ru.ip_fateev.lavka.documents.ReceiptState
import java.util.*

class DataSyncService : LifecycleService() {

    enum class ServiceState(value: Int) {
        STOPED(0),
        IDLE(1),
        SYNC_PRODUCTS(2),
        PRODUCTS_DOWNLOAD(3),
        SYNC_PAID_RECEIPT(4);

        companion object {
            private val VALUES = ServiceState.values()
            fun getByValue(value: Int): ServiceState {
                var res = VALUES.firstOrNull { it.ordinal == value }
                if (res == null) {
                    res = STOPED
                }
                return res
            }
        }
    }

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val TAG = "DataSync Service"

    lateinit var cloudApi: Api
    lateinit var inventory: LocalData
    lateinit var localRepository: LocalRepository
    var state = MutableLiveData(ServiceState.STOPED)
    var productListRequestTime = MutableLiveData(Calendar.getInstance().time)
    var productListForAdd = MutableLiveData<List<Long>>(listOf())
    lateinit var activeSellPaidReceipt: MutableLiveData<Long?>
    var serviceRunning = false

    companion object {
        lateinit var service: DataSyncService

        fun startService(context: Context) {
            val startIntent = Intent(context, DataSyncService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, DataSyncService::class.java)
            context.stopService(stopIntent)
        }

        fun instance(): DataSyncService {
            return service
        }
    }

    override fun onCreate() {
        super.onCreate()

        cloudApi = Common.api
        inventory = App.getInstance()?.getInventory()!!
        localRepository = App.getInstance()?.getRepository()!!

        activeSellPaidReceipt = MutableLiveData<Long?>(null)
        localRepository.getActiveSellReceipt().observe(this) {
            if (it != null) {
                if (it.type == ru.ip_fateev.lavka.documents.ReceiptType.SELL && it.state == ReceiptState.PAID) {
                    activeSellPaidReceipt.postValue(it.id)
                }
                else
                {
                    activeSellPaidReceipt.postValue(null)
                }
            }
            else
            {
                activeSellPaidReceipt.postValue(null)
            }
        }
        service = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val pendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        notificationBuilder = NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID_STATE)
            .setContentTitle("Связь с базой")
            .setContentText("Подключение...")
            .setSmallIcon(R.drawable.ic_notification_sync)
            .setContentIntent(pendingIntent)


        serviceRunning = true

        startForeground(1, notificationBuilder.build())

        state.observe(this) {
            val notificationManager = ContextCompat.getSystemService(this@DataSyncService,
                NotificationManager::class.java)  as NotificationManager

            var str = ""

            when(it) {
                ServiceState.SYNC_PRODUCTS -> {
                    str += "Обновление списка продуктов"
                }
                ServiceState.PRODUCTS_DOWNLOAD -> {
                    str += "Обновление продуктов"
                }
                ServiceState.SYNC_PAID_RECEIPT -> {
                    str += "Отправка чека"
                }
                else -> {
                    str += "Ожидание"
                }
            }

            str += ", скачать: " + productListForAdd.value?.size.toString()

            notificationBuilder.setContentText(str)
            notificationManager.notify(1, notificationBuilder.build())
        }

        startSync()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startSync() {
        lifecycleScope.launch {
            sync()
        }
    }

    private suspend fun sync() {
        state.value = ServiceState.IDLE
        while(serviceRunning) {
            when (state.value) {
                ServiceState.IDLE -> {
                    if (activeSellPaidReceipt.value != null) {
                        state.value = ServiceState.SYNC_PAID_RECEIPT
                    } else if ((Calendar.getInstance().time.time - productListRequestTime.value!!.time) > 10000) {
                        state.value = ServiceState.SYNC_PRODUCTS
                    } else if (productListForAdd.value?.size!! > 0) {
                        state.value = ServiceState.PRODUCTS_DOWNLOAD
                    }
                    else
                    {
                        delay(1000L)
                    }
                }

                ServiceState.SYNC_PRODUCTS -> {
                    withContext(Dispatchers.IO) {
                        syncProductList()
                    }
                    productListRequestTime.value = Calendar.getInstance().time
                    state.value = ServiceState.IDLE
                }

                ServiceState.PRODUCTS_DOWNLOAD -> {
                    if (productListForAdd.value?.size!! > 0) {
                        val list = productListForAdd.value!!.toMutableList()
                        val id = list.first()
                        withContext(Dispatchers.IO) {
                            downloadProduct(id).let {
                                if (it) {
                                    list.remove(id)
                                    productListForAdd.postValue(list.toList())
                                }
                            }
                        }
                    }
                    state.value = ServiceState.IDLE
                }

                ServiceState.SYNC_PAID_RECEIPT -> {
                    if (activeSellPaidReceipt.value != null) {
                        val id = activeSellPaidReceipt.value!!
                        withContext(Dispatchers.IO) {
                            syncPayment(id).let {
                                if (it) {
                                    activeSellPaidReceipt.postValue(null)
                                    localRepository.setReceiptState(id, ReceiptState.CLOSED)
                                }
                            }
                        }
                    }

                    state.value = ServiceState.IDLE
                }

                else -> {
                    delay(1000L)
                }
            }
        }
    }

    private fun syncProductList() {
        Log.d(TAG, "Sync products")

        try {
            val productListResponse = cloudApi.getProductList().execute()
            //Log.d(TAG, "productListResponse:\n ${productListResponse.body()}")
            if (productListResponse.isSuccessful() && productListResponse.body() != null) {
                val productList = productListResponse.body() as ProductList
                if (productList.result && productList.id_list != null) {
                    val productIdList: MutableList<Long> = ArrayList()
                    val localProductList = inventory.productList
                    localProductList.forEach { productIdList += it.id }

                    // ЧТО ДОБАВИТЬ вычитаем из полученного списка, то что у нас есть
                    val forAdd: MutableList<Long> = ArrayList(productList.id_list)
                    forAdd.removeAll(productIdList)

                    // ЧТО УДАЛИТЬ вычитаем из нашего списка то, что получили
                    val forRemove: MutableList<Long> = ArrayList(productIdList)
                    forRemove.removeAll(productList.id_list!!)

                    // ЧТО СРАВНИТЬ вычитаем из списка, который получили, то что добавляем и то что удаляем
                    val forCompare: MutableList<Long> = ArrayList(productList.id_list)
                    forCompare.removeAll(forAdd)
                    forCompare.removeAll(forRemove)

                    productListForAdd.postValue(forAdd)

                    Log.d(TAG, "For add: ${forAdd.size}")
                    Log.d(TAG, "For remove: ${forRemove.size}")
                    Log.d(TAG, "For compare: ${forCompare.size}")
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }
    }

    private fun downloadProduct(id: Long): Boolean {
        Log.d(TAG, "Download product: ${id}")

        try {
            val productResponse = cloudApi.getProduct(id).execute()
            //Log.d(TAG, "productResponse:\n ${productResponse.body()}")
            if (productResponse.isSuccessful() && productResponse.body() != null) {
                val product = productResponse.body() as ru.ip_fateev.lavka.cloud.model.Product
                if (product.result) {
                    val newProduct = Product()
                    newProduct.id = product.product_id!!
                    newProduct.name = product.name
                    newProduct.barcode = product.barcode
                    newProduct.price = product.price
                    inventory.InsertProduct(newProduct)
                    return true
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }

    private suspend fun syncPayment(id: Long): Boolean {
        val r = localRepository.getReceipt(id)

        if (r == null) {
            return false
        }

        val posList = localRepository.getPositions(id)
        var positions: MutableList<Position> = mutableListOf();

        for (p in posList) {
            positions.add(Position(productId = p.productId, productName = p.productName, price = p.price, count = 1))
        }

        val transactionsList = localRepository.getTransactions(id)


        val receipt = Receipt(
            id = r.uuid,
            type = ReceiptType.SELL,
            deviceUid = null,
            timestamp = Calendar.getInstance().time,
            positions = positions
        )

        try {
            val receiptResponse = cloudApi.postReceipt(receipt).execute()
            if (receiptResponse.isSuccessful() && receiptResponse.body() != null) {
                val receipt = receiptResponse.body() as Receipt
                if (receipt.result != null) {
                    if (receipt.result){

                    }
                }

                return true
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }
}
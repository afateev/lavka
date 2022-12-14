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
import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.common.Common
import ru.ip_fateev.lavka.cloud.model.*
import ru.ip_fateev.lavka.data.ReceiptState
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
    lateinit var localRepository: LocalRepository
    var state = MutableLiveData(ServiceState.STOPED)
    var productListRequestTime = MutableLiveData(Calendar.getInstance().time)
    var productListForAdd = MutableLiveData<List<Long>>(listOf())
    lateinit var activeSellPaidReceipt: MutableLiveData<Long?>
    lateinit var activeSellDelayedReceipt: MutableLiveData<Long?>
    var syncPaymentTime = MutableLiveData(Calendar.getInstance().time)
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
        localRepository = App.getInstance().getRepository()

        activeSellPaidReceipt = MutableLiveData<Long?>(null)
        localRepository.getActiveSellReceipt().observe(this) {
            if (it != null) {
                if (it.type == ru.ip_fateev.lavka.data.ReceiptType.SELL && it.state == ReceiptState.PAID) {
                    syncPaymentTime = MutableLiveData(Calendar.getInstance().time)
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

        activeSellDelayedReceipt = MutableLiveData<Long?>(null)
        localRepository.getOneReceiptLive(ru.ip_fateev.lavka.data.ReceiptType.SELL, ReceiptState.DELAYED).observe(this) {
            if (it != null) {
                if (it.type == ru.ip_fateev.lavka.data.ReceiptType.SELL && it.state == ReceiptState.DELAYED) {
                    activeSellDelayedReceipt.postValue(it.id)
                }
                else
                {
                    activeSellDelayedReceipt.postValue(null)
                }
            }
            else
            {
                activeSellDelayedReceipt.postValue(null)
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
                    } else if (activeSellDelayedReceipt.value != null) {
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
                        syncUserList()
                    }
                    productListRequestTime.value = Calendar.getInstance().time
                    state.value = ServiceState.IDLE
                }

                ServiceState.PRODUCTS_DOWNLOAD -> {
                    if (productListForAdd.value?.size!! > 0) {
                        val list = productListForAdd.value!!.toMutableList()

                        var maxCount = 10
                        // если список на добавлние большой, проще спросить все разом
                        if (list.size > maxCount * 2) {
                            withContext(Dispatchers.IO) {
                                downloadProductsAll().let {
                                    if (it) {
                                        list.clear()
                                        productListForAdd.postValue(list.toList())
                                    }
                                }
                            }
                        }
                        // если список на добавлние небольшой, докачиваем помаленьку
                        else {
                            if (maxCount > list.size) {
                                maxCount = list.size
                            }
                            var ids = list.subList(0, maxCount)
                            withContext(Dispatchers.IO) {
                                downloadProducts(ids).let {
                                    if (it) {
                                        list.removeAll(ids)
                                        productListForAdd.postValue(list.toList())
                                    }
                                }
                            }
                        }
                    }
                    state.value = ServiceState.IDLE
                }

                ServiceState.SYNC_PAID_RECEIPT -> {
                    if (activeSellPaidReceipt.value != null) {
                        val id = activeSellPaidReceipt.value!!

                        if ((Calendar.getInstance().time.time - syncPaymentTime.value!!.time) > 5000) {
                            activeSellPaidReceipt.postValue(null)
                            localRepository.setReceiptState(id, ReceiptState.DELAYED)
                        }
                        else {

                            withContext(Dispatchers.IO) {
                                syncPayment(id).let {
                                    if (it) {
                                        activeSellPaidReceipt.postValue(null)
                                        localRepository.setReceiptState(id, ReceiptState.CLOSED)
                                    }
                                }
                            }
                        }
                    } else if (activeSellDelayedReceipt.value != null) {
                        val id = activeSellDelayedReceipt.value!!

                        withContext(Dispatchers.IO) {
                            syncPayment(id).let {
                                if (it) {
                                    activeSellDelayedReceipt.postValue(null)
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

    private suspend fun syncProductList() {
        Log.d(TAG, "Sync products")

        try {
            val productListResponse = cloudApi.getProductList().execute()
            //Log.d(TAG, "productListResponse:\n ${productListResponse.body()}")
            if (productListResponse.isSuccessful() && productListResponse.body() != null) {
                val productList = productListResponse.body() as ProductList
                if (productList.result && productList.id_list != null) {
                    val productIdList: MutableList<Long> = ArrayList()
                    val localProductList = localRepository.getProducts()
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

    private suspend fun insertOrUpdateProduct(product: ru.ip_fateev.lavka.cloud.model.Product) {
        if (product.product_id == null) {
            return
        }

        if (product.name == null) {
            return
        }

        if (product.barcode == null) {
            return
        }

        if (product.price == null) {
            return
        }
        val newProduct = ru.ip_fateev.lavka.data.Product(
            id = product.product_id!!,
            name = product.name!!,
            barcode = product.barcode!!,
            price = product.price!!
        )

        val localProductList = localRepository.getProducts()
        for (p in localProductList) {
            if (p.id == newProduct.id) {
                if (p != newProduct) {
                    localRepository.updateProduct(newProduct)
                }
                return
            }
        }

        localRepository.insertProduct(newProduct)
    }

    private suspend fun downloadProduct(id: Long): Boolean {
        Log.d(TAG, "Download product: ${id}")

        try {
            val productResponse = cloudApi.getProduct(id).execute()
            //Log.d(TAG, "productResponse:\n ${productResponse.body()}")
            if (productResponse.isSuccessful() && productResponse.body() != null) {
                val product = productResponse.body() as ru.ip_fateev.lavka.cloud.model.Product
                if (product.result) {
                    insertOrUpdateProduct(product)
                    return true
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }

    private suspend fun downloadProducts(ids: List<Long>): Boolean {
        Log.d(TAG, "Download product: $ids")

        try {
            val productListResponse = cloudApi.getProductList(ids).execute()
            //Log.d(TAG, "productResponse:\n ${productResponse.body()}")
            if (productListResponse.isSuccessful() && productListResponse.body() != null) {
                val productList = productListResponse.body() as ProductList
                if (productList.result) {
                    if (productList.product_list != null) {
                        for (product in productList.product_list!!) {
                            insertOrUpdateProduct(product)
                        }
                        return true
                    }
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }

    private suspend fun downloadProductsAll(): Boolean {
        Log.d(TAG, "Download product all")

        try {
            val productListResponse = cloudApi.getProductListAll().execute()
            //Log.d(TAG, "productResponse:\n ${productResponse.body()}")
            if (productListResponse.isSuccessful() && productListResponse.body() != null) {
                val productList = productListResponse.body() as ProductList
                if (productList.result) {
                    if (productList.product_list != null) {
                        for (product in productList.product_list!!) {
                            insertOrUpdateProduct(product)
                        }
                        return true
                    }
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }

    private suspend fun syncUserList() {
        Log.d(TAG, "Sync users")

        try {
            val userListResponse = cloudApi.getUserList().execute()
            Log.d(TAG, "userListResponse:\n ${userListResponse.body()}")
            if (userListResponse.isSuccessful() && userListResponse.body() != null) {
                val userList = userListResponse.body() as UserList
                if (userList.result && userList.user_list != null) {
                    val localList = localRepository.getUsers()
                    Log.d(TAG, "Users local: ${localList}")

                    for (u in userList.user_list!!) {
                        if (u.id != null && u.name != null) {

                            var found: ru.ip_fateev.lavka.data.User? = null

                            for (l in localList) {
                                if (l.id == u.id) {
                                    found = l
                                    break
                                }
                            }

                            val user = ru.ip_fateev.lavka.data.User(id = u.id!!, name = u.name!!)

                            if (found == null) {
                                Log.d(TAG, "User add: ${user}")
                                localRepository.insertUser(user)
                            } else {
                                if (found.name != u.name) {
                                    Log.d(TAG, "User update: ${user}")
                                    localRepository.updateUser(user)
                                }
                            }
                        }
                    }


                    /*Log.d(TAG, "For add: ${forAdd.size}")
                    Log.d(TAG, "For remove: ${forRemove.size}")
                    Log.d(TAG, "For compare: ${forCompare.size}")*/
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }
    }

    private suspend fun syncPayment(id: Long): Boolean {
        val r = localRepository.getReceipt(id)

        if (r == null) {
            return false
        }

        val posList = localRepository.getPositions(id)
        var positions: MutableList<Position> = mutableListOf()

        for (p in posList) {
            positions.add(Position(productId = p.productId, productName = p.productName, price = p.price, quantity = 1))
        }

        val transactionsList = localRepository.getTransactions(id)
        var transactions: MutableList<Transaction>  = mutableListOf()

        for (t in transactionsList) {
            val transactionType = when(t.type) {
                ru.ip_fateev.lavka.data.TransactionType.NONE -> TransactionType.NONE
                ru.ip_fateev.lavka.data.TransactionType.CASH -> TransactionType.CASH
                ru.ip_fateev.lavka.data.TransactionType.CASHCHANGE -> TransactionType.CASHCHANGE
                ru.ip_fateev.lavka.data.TransactionType.CARD -> TransactionType.CARD
            }

            transactions.add(Transaction(type = transactionType, amount = t.amount, rrn = t.rrn))
        }


        val receiptType = when(r.type) {
            ru.ip_fateev.lavka.data.ReceiptType.NONE -> ReceiptType.NONE
            ru.ip_fateev.lavka.data.ReceiptType.SELL -> ReceiptType.SELL
        }

        val t = Calendar.getInstance()
        t.timeInMillis = r.dateTime

        val receipt = Receipt(
            uuid = r.uuid,
            type = receiptType,
            deviceUid = App.getDevId(),
            timestamp = t.time,
            positions = positions,
            transactions = transactions
        )

        try {
            val receiptResponse = cloudApi.postReceipt(receipt).execute()
            if (receiptResponse.isSuccessful() && receiptResponse.body() != null) {
                val receipt = receiptResponse.body() as Receipt
                if (receipt.result != null) {
                    if (receipt.result){
                        Log.d(TAG, "Receipt " + receipt.uuid + " pushed")
                        return true
                    }
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        return false
    }
}
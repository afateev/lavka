package ru.ip_fateev.lavka
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.App.Companion.getInstance
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.common.Common
import ru.ip_fateev.lavka.cloud.model.Position
import ru.ip_fateev.lavka.cloud.model.ProductList
import ru.ip_fateev.lavka.cloud.model.Receipt
import ru.ip_fateev.lavka.cloud.model.ReceiptType
import java.util.*

class DataSyncService : LifecycleService() {

    enum class ServiceState(value: Int) {
        STOPED(0),
        RUN(1),
        IDLE(2);

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
    var state = MutableLiveData(ServiceState.STOPED)
    var syncRunning = false
    var serviceRunning = false

    companion object {
        fun startService(context: Context) {
            val startIntent = Intent(context, DataSyncService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, DataSyncService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        cloudApi = Common.api

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

            when(it) {
                ServiceState.RUN -> {
                    notificationBuilder.setContentText("Синхронизация")
                }
                else -> {
                    notificationBuilder.setContentText("Ожидание")
                }
            }

            notificationManager.notify(1, notificationBuilder.build())
        }

        //startSync()

        val timer = Timer()
        //timer.scheduleAtFixedRate(timerTask, 0, 10000)

        return super.onStartCommand(intent, flags, startId)
    }

    private val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            if (!syncRunning) {
                val notificationManager = ContextCompat.getSystemService(this@DataSyncService,
                    NotificationManager::class.java)  as NotificationManager
                notificationBuilder.setContentText("Синхронизация")
                notificationManager.notify(1, notificationBuilder.build())
                doSync()
                notificationBuilder.setContentText("Ожидание")
                notificationManager.notify(1, notificationBuilder.build())
            }
        }
    }

    private fun startSync() {
        CoroutineScope(Dispatchers.Main).launch {
            state.value = ServiceState.IDLE
            while(serviceRunning) {
                state.value = ServiceState.RUN
                delay(1000L)
                state.value = ServiceState.IDLE
                delay(1000L)
            }
        }
    }

    private fun doSync() {
        syncRunning = true

        Log.d(TAG, "Sync start")
        val inventory = getInstance()!!.getInventory()

        var positions: MutableList<Position> = mutableListOf();
        positions.add(Position(productId = 0, productName = "test", price = 100.0, count = 1))

        val receipt = Receipt(
            id = UUID.randomUUID(),
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
            }

            val productListResponse = cloudApi.getProductList().execute()
            Log.d(TAG, "productListResponse:\n ${productListResponse.body()}")
            if (productListResponse.isSuccessful() && productListResponse.body() != null) {
                val productList = productListResponse.body() as ProductList
                if (productList.result && productList.id_list != null) {
                    val productIdList: MutableList<Long> = ArrayList()
                    val localProductList = inventory!!.productList
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

                    // скачиваем себе, то что нужно добавить
                    Log.d(TAG, "For add: ${forAdd.size}")
                    forAdd.forEach {
                        val productResponse = cloudApi.getProduct(it).execute()
                        Log.d(TAG, "productResponse:\n ${productResponse.body()}")
                        if (productResponse.isSuccessful() && productResponse.body() != null) {
                            val product = productResponse.body() as ru.ip_fateev.lavka.cloud.model.Product
                            if (product.result) {
                                    val newProduct = Product()
                                    newProduct.id = product.product_id!!
                                    newProduct.name = product.name
                                    newProduct.barcode = product.barcode
                                    newProduct.price = product.price
                                    inventory.InsertProduct(newProduct)
                            }
                        }
                    }

                    Log.d(TAG, "For remove: ${forRemove.size}")
                    Log.d(TAG, "For compare: ${forCompare.size}")
                }
            }
        } catch (throwable: Throwable) {
            Log.d(TAG, throwable.toString())
        }

        Log.d(TAG, "Sync complete")

        syncRunning = false
    }
}
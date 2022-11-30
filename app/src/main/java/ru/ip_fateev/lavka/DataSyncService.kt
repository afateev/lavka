package ru.ip_fateev.lavka
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.ip_fateev.lavka.App.Companion.getInstance
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.common.Common
import ru.ip_fateev.lavka.cloud.model.Position
import ru.ip_fateev.lavka.cloud.model.ProductList
import ru.ip_fateev.lavka.cloud.model.Receipt
import ru.ip_fateev.lavka.cloud.model.ReceiptType
import java.util.*

class DataSyncService : Service() {
    private val CHANNEL_ID = "DataSync Service"
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val TAG = "DataSync Service"

    lateinit var cloudApi: Api
    var syncRunning = false

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, DataSyncService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, DataSyncService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cloudApi = Common.api

        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel(this)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("DataSync Service Title")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_notification_sync)
            .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()
        startForeground(1, notification)

        val timer = Timer()

        timer.scheduleAtFixedRate(timerTask, 0, 10000)
        //stopSelf();
        //stopForeground(true)
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "DataSync Service Channel",
                NotificationManager.IMPORTANCE_LOW)
            serviceChannel.setSound(null, null)
            serviceChannel.setShowBadge(false)
            val manager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
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

        val receiptResponse = cloudApi.postReceipt(receipt).execute()

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

        Log.d(TAG, "Sync complete")

        syncRunning = false
    }
}
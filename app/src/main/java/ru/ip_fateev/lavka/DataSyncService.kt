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
import android.util.Pair
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import org.json.JSONException
import org.json.JSONObject
import ru.ip_fateev.lavka.App.Companion.getInstance
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.common.Common
import ru.ip_fateev.lavka.cloud.model.ProductList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ApiProductListGetResult(root: JSONObject) {
    var valid = false
    var result = false
    var id_list: MutableList<Long> = ArrayList()

    init {
        fromJson(root)
    }

    fun fromJson(root: JSONObject) {
        try {
            result = root.getBoolean("result")
            val id_list_tmp = root.getJSONArray("id_list")
            for (i in 0 until id_list_tmp.length()) {
                id_list.add(id_list_tmp.getLong(i))
            }
            valid = true
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

internal class ApiProductGetResult(root: JSONObject) {
    var valid = false
    var result = false
    var product_id: Long? = null
    var name = ""
    var barcode = ""
    var price: Double? = null

    init {
        fromJson(root)
    }

    fun fromJson(root: JSONObject) {
        try {
            result = root.getBoolean("result")
            product_id = root.getLong("product_id")
            name = root.getString("name")
            barcode = root.getString("barcode")
            price = root.getDouble("price")
            valid = true
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

class DataSyncService : Service() {
    private val CHANNEL_ID = "DataSync Service"
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val TAG = "DataSync Service"
    private val API_URL = "https://ip-fateev.ru/api"
    private val API_URL_PRODUCT_LIST = "$API_URL/product/list"
    private val API_URL_PRODUCT_GET = "$API_URL/product/"

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

        /*
        val response = cloudApi.getProductList().execute()
        if (response.isSuccessful() && response.body() != null) {
            val res = response.body() as ProductList
            if (res.result != null && res.id_list != null) {
                if (res.result) {

                }
            }
        }*/
        /*
        cloudApi.getProductList().enqueue(object : Callback<ProductList> {
            override fun onFailure(call: Call<ProductList>, t: Throwable) {

            }

            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                if (response.isSuccessful() && response.body() != null) {
                    val res = response.body() as ProductList
                    if (res.result != null && res.id_list != null) {
                        if (res.result) {

                        }
                    }
                }
            }
        })*/

        Log.d(TAG, "Sync start")
        val inventory = getInstance()!!.getInventory()

        val getProductListResult: Pair<Boolean, JSONObject?> = syncRequestGetJson(API_URL_PRODUCT_LIST)

        if (getProductListResult.first) {
            val res = getProductListResult.second?.let { ApiProductListGetResult(it) }
            if (res != null) {
                if (res.valid && res.result) {
                    val productList = inventory!!.productList
                    val productIdList: MutableList<Long> = ArrayList()
                    for (i in productList.indices) {
                        productIdList.add(productList[i].id)
                    }

                    // ЧТО ДОБАВИТЬ вычитаем из полученного списка, то что у нас есть
                    val forAdd: MutableList<Long> = ArrayList(res.id_list)
                    forAdd.removeAll(productIdList)

                    // ЧТО УДАЛИТЬ вычитаем из нашего списка то, что получили
                    val forRemove: MutableList<Long> = ArrayList(productIdList)
                    forRemove.removeAll(res.id_list)

                    // ЧТО СРАВНИТЬ вычитаем из списка, который получили, то что удаляем и то что удаляем
                    val forCompare: MutableList<Long> = ArrayList(res.id_list)
                    forCompare.removeAll(forAdd)
                    forCompare.removeAll(forRemove)

                    // скачиваем себе, то что нужно добавить
                    for (i in forAdd.indices) {
                        val id = forAdd[i]
                        val getProductResult: Pair<Boolean, JSONObject?> =
                            syncRequestGetJson(API_URL_PRODUCT_GET + id.toString())
                        if (getProductResult.first) {
                            val product = getProductResult.second?.let { ApiProductGetResult(it) }
                            if (product != null) {
                                if (product.valid && product.result) {
                                    val newProduct = Product()
                                    newProduct.id = product.product_id!!
                                    newProduct.name = product.name
                                    newProduct.barcode = product.barcode
                                    newProduct.price = product.price
                                    inventory.InsertProduct(newProduct)
                                }
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "Response is:\n $res")
        }

        Log.d(TAG, "Sync complete")

        syncRunning = false
    }

    private fun syncRequestGetJson(urlString: String): Pair<Boolean, JSONObject?> {
        var result = false
        var response: JSONObject? = null
        val res: Pair<Boolean, String?> = syncRequestGet(urlString)
        if (res.first) {
            try {
                response = res.second?.let { JSONObject(it) }
                result = true
                Log.d(TAG, "Get JSON Response is:\n $response")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Log.d(TAG, """Get JSON Response is: ${res.second}""")
        }
        return Pair(result, response)
    }

    private fun syncRequestGet(urlString: String): Pair<Boolean, String?> {
        var result = false
        var response: String? = ""
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connectTimeout = 5000
            urlConnection.readTimeout = 5000
            urlConnection.useCaches = false
            urlConnection.connect()
            val responseCode = urlConnection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                val stream = urlConnection.errorStream
                val bufferedReader = BufferedReader(InputStreamReader(stream))
                var s: String?
                while (bufferedReader.readLine().also { s = it } != null) {
                    response += s
                }
                bufferedReader.close()
            } else {
                val stream = urlConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(stream))
                var s: String?
                while (bufferedReader.readLine().also { s = it } != null) {
                    response += s
                }
                bufferedReader.close()
                result = true
            }
            urlConnection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(result, response)
    }
}
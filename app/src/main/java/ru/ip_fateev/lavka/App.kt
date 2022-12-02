package ru.ip_fateev.lavka

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import ru.ip_fateev.lavka.Inventory.LocalData
import ru.ip_fateev.lavka.documents.LocalDatabase

class App : Application() {
    companion object {
        val NOTIFICATION_CHANNEL_ID_STATE = "State notification channel"

        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
    }

    private var inventory: LocalData? = null
    lateinit var database: LocalDatabase
    lateinit var localRepository: LocalRepository

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID_STATE, "Уведомления о состоянии",
                NotificationManager.IMPORTANCE_LOW)
            serviceChannel.setSound(null, null)
            serviceChannel.setShowBadge(false)
            val manager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }

        instance = this
        inventory = LocalData(applicationContext, "data_test1")
        database = LocalDatabase.instance(this)
        localRepository = LocalRepository(database.receiptDao(), database.positionDao(), database.transactionDao())
        DataSyncService.startService(this)
        UposService.startService(this, "UPOS Service running...")
    }

    fun getInventory(): LocalData? {
        return inventory
    }

    fun getRepository(): LocalRepository {
        return localRepository
    }
}
package ru.ip_fateev.lavka

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class UposService : Service() {

    private val CHANNEL_ID = "UPOS Service"
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val TAG = "UPOS Service"

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, UposService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, UposService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel(this)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("UPOS Service Title")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_notification_sync)
            .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()
        startForeground(1, notification)

        //stopSelf();
        //stopForeground(true)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "UPOS Service Channel",
                NotificationManager.IMPORTANCE_LOW)
            serviceChannel.setSound(null, null)
            serviceChannel.setShowBadge(false)
            val manager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}
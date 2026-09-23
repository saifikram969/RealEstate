package com.btjnonbrokerage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.btjnonbrokerage.Activity.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var notificationId = 0
    lateinit var pendingIntent: PendingIntent
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        val notificationIntent = Intent(this, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE

        )


        val data = message.data
        val notificationType = data["type"]

        val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
        val percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        pushNotification(message.notification!!.title.toString(),message.notification!!.body)
    }

    private fun pushNotification(title: String, message: String?) {
        Log.d("got",title)
        notificationId++
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "default_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())

    }
}
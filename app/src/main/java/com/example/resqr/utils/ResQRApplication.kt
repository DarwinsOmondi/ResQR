package com.example.resqr

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class ResQRApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "resqr_qr_channel",
                "ResQR LockScreen QR",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notification for displaying medical QR code on lock screen"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("ResQRApplication", "Notification channel resqr_qr_channel created")
        }
    }
}
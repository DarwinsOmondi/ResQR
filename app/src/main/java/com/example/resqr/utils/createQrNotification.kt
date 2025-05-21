package com.example.resqr.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.resqr.R
import androidx.core.graphics.scale

fun createQrNotification(
    context: Context,
    qrBitmap: Bitmap,
    userName: String,
    bloodType: String
): Notification {
    val channelId = "resqr_qr_channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(
                channelId,
                "ResQR LockScreen QR",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notification for displaying medical QR code on lock screen"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
            Log.d("createQrNotification", "Created fallback notification channel: $channelId")
        }
    }
    val scaledQrBitmap = qrBitmap.scale(200, 200)
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_qr_code_24)
        .setContentTitle("Emergency QR Active")
        .setContentText("$userName • Blood Type: $bloodType")
        .setLargeIcon(scaledQrBitmap)
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(scaledQrBitmap) // Scaled bitmap for expanded view
                .bigLargeIcon(null as Bitmap?)
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOngoing(true)
        .build()

    Log.d("createQrNotification", "Notification created for user: $userName")
    return notification
}
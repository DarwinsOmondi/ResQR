package com.example.resqr.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.resqr.R

class QrForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val qrContent = intent?.getStringExtra("qrContent") ?: return START_NOT_STICKY
        val qrBitmap = generateQrCodeBitmap(qrContent)
        startForeground(1, createNotificationWithQR(qrBitmap))
        return START_STICKY
    }

    private fun createNotificationWithQR(qrBitmap: Bitmap): Notification {
        val channelId = "qr_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "QR Code Lock Screen", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                description = "Shows your emergency QR code"
            }
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Emergency QR Code")
            .setContentText("Show this to the responder")
            .setSmallIcon(R.drawable.ic_qr_code)
            .setLargeIcon(qrBitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(qrBitmap))
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

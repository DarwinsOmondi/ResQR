package com.example.resqr.utils

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import android.util.Log
import java.io.FileNotFoundException

class QrForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("QrForegroundService", "onStartCommand called with intent: $intent")
        val qrBitmapUri = intent?.getParcelableExtra<Uri>("qrBitmapUri")
        val userName = intent?.getStringExtra("userName") ?: "User"
        val bloodType = intent?.getStringExtra("bloodType") ?: "Unknown"

        if (qrBitmapUri != null) {
            Log.d("QrForegroundService", "Received qrBitmapUri: $qrBitmapUri")
            try {
                contentResolver.openInputStream(qrBitmapUri)?.use { inputStream ->
                    val qrBitmap = BitmapFactory.decodeStream(inputStream)
                    if (qrBitmap != null) {
                        Log.d("QrForegroundService", "Bitmap decoded successfully")
                        val notification = createQrNotification(this, qrBitmap, userName, bloodType)
                        startForeground(101, notification)
                        Log.d("QrForegroundService", "Foreground service started with notification")
                    } else {
                        Log.e("QrForegroundService", "Failed to decode bitmap")
                        stopSelf()
                    }
                }
            } catch (e: FileNotFoundException) {
                Log.e("QrForegroundService", "File not found: $qrBitmapUri", e)
                stopSelf()
            } catch (e: SecurityException) {
                Log.e("QrForegroundService", "Security exception accessing URI", e)
                stopSelf()
            }
        } else {
            Log.e("QrForegroundService", "No qrBitmapUri provided")
            stopSelf()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
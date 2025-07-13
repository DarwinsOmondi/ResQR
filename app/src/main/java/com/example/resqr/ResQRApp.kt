package com.example.resqr

import android.app.Application
import android.content.Context

class ResQRApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}

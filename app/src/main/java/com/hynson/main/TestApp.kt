package com.hynson.main

import android.app.Application
import android.content.Context
import com.hynson.crash.CrashAHandler
import com.hynson.crash.CrashBHandler
import com.hynson.crash.CrashHandler
import com.hynson.webview.HttpServer

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread {
            try {
                val server = HttpServer(8080)
                server.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        CrashHandler.getInstance().init(base)

        val original = Thread.getDefaultUncaughtExceptionHandler()

        val a = CrashAHandler(original)
        val b = CrashBHandler(a)

        Thread.setDefaultUncaughtExceptionHandler(b)
    }
}
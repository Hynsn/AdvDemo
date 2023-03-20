package com.hynson.alertwindow

import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.LifecycleService

class AlertWindowService : LifecycleService() {

    private val windowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private var alertView: View? = null

    override fun onCreate() {
        super.onCreate()


    }

    fun show(layout: Int) {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val layoutParam = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.RGBA_8888
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.START or Gravity.TOP
            //设置剧中屏幕显示
            x = metrics.widthPixels / 2 - width / 2
            y = metrics.heightPixels / 2 - height / 2
        }
        alertView = LayoutInflater.from(this).inflate(layout, null).apply {
            setOnTouchListener(MoveListener(layoutParam, windowManager))
        }
        windowManager.addView(alertView, layoutParam)
    }

    fun dismiss() {
        alertView?.let {
            if (it.windowToken != null) {
                windowManager.removeView(it)
            }
        }
    }
}
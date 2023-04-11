package com.hynson.alertwindow

import android.animation.ValueAnimator
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.hynson.R


class AlertWindowService : LifecycleService() {
    private val windowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }
    private var windowParams: WindowManager.LayoutParams? = null

    private var floatRootView: View? = null//悬浮窗View

    override fun onCreate() {
        super.onCreate()
        val myEventObserver = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onAppBackground() {

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onAppForeground() {

            }
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.i("TAG", "onStateChanged: $event")
            }
        })
        initObserve()
    }

    /**
     * 初始化订阅
     */
    private fun initObserve() {
        ViewModleMain.apply {
            /**
             * 悬浮窗按钮的显示和隐藏
             */
            isVisible.observe(this@AlertWindowService, {
                floatRootView?.visibility = if (it) View.VISIBLE else View.GONE
            })
            /**
             * 悬浮窗按钮的创建和移除
             */
            isShowSuspendWindow.observe(this@AlertWindowService) {
                if (it) {
                    showWindow()
                } else {
                    floatRootView?.run {
                        if (windowToken != null) {
                            windowManager.removeView(floatRootView)
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建悬浮窗
     */
    private fun showWindow() {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val layoutParam = WindowManager.LayoutParams().apply {
            // 设置type兼容
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
            }
            format = PixelFormat.RGBA_8888
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            gravity = Gravity.START or Gravity.TOP
            //设置剧中屏幕显示
            x = outMetrics.widthPixels / 2 - width / 2
            y = outMetrics.heightPixels / 2 - height / 2
        }
        windowParams = layoutParam

        // 新建悬浮窗控件
        floatRootView = LayoutInflater.from(this).inflate(R.layout.alert_timer, null)
        floatRootView?.setOnTouchListener(MoveListener(layoutParam, windowManager))
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(floatRootView, layoutParam)
    }
}
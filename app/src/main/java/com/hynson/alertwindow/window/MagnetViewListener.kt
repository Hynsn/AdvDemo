package com.hynson.alertwindow.window

import android.view.MotionEvent

/**
 * 磁力吸附悬浮窗 对应的事件
 */
interface MagnetViewListener {
    fun onRemove(view: AlertMagnetView)
    fun onClick(event: MotionEvent)
}
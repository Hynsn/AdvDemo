package com.hynson.alertwindow.window

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import java.lang.ref.WeakReference

/**
 * AlertView管理类
 */
class AlertViewManager(builder: AlertWindow.Builder) : IAlertView, AlertLifecycle.Callback {
    private var mContainer: WeakReference<FrameLayout>? = null

    private val alertView = AlertView(builder.app, builder.layoutId, builder.layoutParam)

    init {
        builder.lifecycle.register(this)
    }

    private fun getActivityRoot(activity: Activity): FrameLayout? {
        try {
            return activity.window.decorView as FrameLayout//.findViewById(android.R.id.content) as FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun activityAttach(activity: Activity) {
        // 添加
        getActivityRoot(activity)?.let {
            attach(it)
        }
    }

    override fun activityDetach(activity: Activity) {
        getActivityRoot(activity)?.let {
            detach(it)
        }
    }

    private fun getContainer(): FrameLayout? {
        if (mContainer == null) {
            return null
        }
        return mContainer?.get()
    }

    private fun attach(c: FrameLayout) {
        mContainer?.clear()

        mContainer = WeakReference(c)

        addViewToWindow()
    }

    private fun addViewToWindow() {
        if (getContainer() == null) return

        alertView.parent?.let {
            val p = it as ViewGroup
            p.removeView(alertView)
        }
        if (!ViewCompat.isAttachedToWindow(alertView)) {
            getContainer()?.addView(alertView)
        }
    }

    private fun detach(container: FrameLayout) {
        if (alertView.parent == container) {
            container.removeView(alertView)
        }
        if (getContainer() != null && getContainer() == container) {
            mContainer?.clear()
            mContainer = null
        }
    }

    override fun show() {
        addViewToWindow()
    }

    override fun hide() {
        getContainer()?.let { detach(it) }
    }

    override fun getView(): View {
        return alertView.mView
    }
}
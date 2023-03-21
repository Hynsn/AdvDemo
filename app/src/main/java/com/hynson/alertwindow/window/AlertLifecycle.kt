package com.hynson.alertwindow.window

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AlertLifecycle : Application.ActivityLifecycleCallbacks {

    // 增加回调接口将ActivityLifecycleCallbacks事件传递出去
    private var callback: Callback? = null

    /**
     * 注册ActivityLifecycleCallbacks
     */
    fun bind(context: Application): AlertLifecycle {
        context.registerActivityLifecycleCallbacks(this)
        return this
    }

    fun register(cb: Callback) {
        callback = cb
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {
        callback?.activityAttach(p0)
    }

    override fun onActivityPaused(p0: Activity) {
        callback?.activityDetach(p0)
    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    interface Callback {
        fun activityAttach(activity: Activity)
        fun activityDetach(activity: Activity)
    }
}
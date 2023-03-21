package com.hynson.alertwindow.window

import android.app.Application
import android.view.ViewGroup

object AlertWindow {
    private var builder: Builder? = null

    fun with(context: Application): Builder {
        val b = Builder(context)
        builder = b
        return b
    }

    fun get(): IAlertView? {
        if (builder == null) {
            throw IllegalArgumentException("can not invoke before with()!")
        }
        return builder?.alertWindow
    }

    class Builder(val app: Application) {
        var lifecycle: AlertLifecycle = AlertLifecycle().bind(app)

        var layoutId = 0
        var alertWindow: AlertViewManager? = null
        var layoutParam: ViewGroup.LayoutParams? = null

        fun setLayoutId(id: Int): Builder {
            layoutId = id
            return this
        }

        fun setLayoutParam(param: ViewGroup.LayoutParams): Builder {
            layoutParam = param
            return this
        }

        fun build() {
            alertWindow = AlertViewManager(this)
        }
    }
}
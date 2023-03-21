package com.hynson.alertwindow.window

import android.view.View

interface IAlertView {
    fun show()
    fun hide()
    fun getView(): View

//    fun setOnClickListener(listener:AlertView.)
}
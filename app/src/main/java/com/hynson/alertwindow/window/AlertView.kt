package com.hynson.alertwindow.window

import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class AlertView(context: Context, resId: Int, layoutParam: ViewGroup.LayoutParams? = null) :
    AlertMagnetView(context), MagnetViewListener {

    val mView: View

    init {
        mView = inflate(context, resId, this)
        layoutParams = layoutParam ?: getParams()
    }

    private fun getParams(): LayoutParams {
        val params = LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.START
        params.setMargins(100, params.topMargin, params.rightMargin, 100)
        return params
    }

    override fun onRemove(view: AlertMagnetView) {
        TODO("Not yet implemented")
    }

    override fun onClick(event: MotionEvent) {
        TODO("Not yet implemented")
    }

}
package com.hynson.alertwindow

import android.view.View
import com.base.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.AlertwindowActivityBinding

class AlertWindowActivity : BaseActivity<AlertwindowActivityBinding>(), View.OnClickListener {
    override fun getLayout() = R.layout.alertwindow_activity

    override fun bindView() {

    }

    override fun onClick(p0: View) {
        when (p0.id) {

        }
    }
}
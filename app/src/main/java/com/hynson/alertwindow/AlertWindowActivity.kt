package com.hynson.alertwindow

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.fastdroid.base.BaseActivity
import com.hynson.R
import com.hynson.alertwindow.dialog.DialogService
import com.hynson.alertwindow.window.AlertWindow
import com.hynson.databinding.AlertwindowActivityBinding

class AlertWindowActivity : BaseActivity<AlertwindowActivityBinding>(), View.OnClickListener {
    private var isReceptionShow = false

    override fun getLayout() = R.layout.alertwindow_activity

    override fun bindView() {
        startService(Intent(this, AlertWindowService::class.java))
        binding.btnForegroundNopermission.setOnClickListener(this)
        binding.btnCloseNp.setOnClickListener(this)

        binding.btnClose.setOnClickListener(this)
        binding.btnForeground.setOnClickListener(this)
        binding.btnGlobal.setOnClickListener(this)

        binding.btnOpenDialog.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_foreground_nopermission -> {
                AlertWindow.get()?.show()
            }
            R.id.btn_close_np -> {
                AlertWindow.get()?.hide()
            }

            R.id.btn_foreground -> {
                Utils.checkSuspendedWindowPermission(this) {
                    isReceptionShow = false
                    ViewModleMain.isShowSuspendWindow.postValue(true)
                }
            }

            R.id.btn_global -> {
                Utils.checkSuspendedWindowPermission(this) {
                    isReceptionShow = true
                    ViewModleMain.isShowSuspendWindow.postValue(true)
                }
            }

            R.id.btn_close -> {
                closeAllSuspendWindow()
            }

            R.id.btn_open_dialog -> {
                showGlobalDialog()
            }
        }
    }


    private fun showGlobalDialog() {
        //兼容api23版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                val intent = Intent(this, DialogService::class.java)
                startService(intent)
            } else {
                //若没有权限，提示获取.
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                Toast.makeText(this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        } else {
            //SDK在23以下，不用管.
            val intent = Intent(this, DialogService::class.java)
            startService(intent)
            finish()
        }
    }

    /**
     * 关闭所有悬浮窗
     */
    fun closeAllSuspendWindow() {
        ViewModleMain.isShowSuspendWindow.postValue(false)
        ViewModleMain.isShowWindow.postValue(false)
    }

    override fun onResume() {
        super.onResume()
        if (isReceptionShow) {
            ViewModleMain.isVisible.postValue(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isReceptionShow) {
            ViewModleMain.isVisible.postValue(false)
        }
    }
}
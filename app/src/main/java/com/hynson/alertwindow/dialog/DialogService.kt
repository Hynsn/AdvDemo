package com.hynson.alertwindow.dialog

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hynson.R


/**
 * Author: Hynsonhou
 * Date: 2023/4/8 10:07
 * Description: 全局弹框
 * History:
 * <author> <time> <version> <desc>
 * Hynsonhou 2023/4/8 1.0 首次创建
 */
class DialogService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
//         showDialog()
        showBottomSheetDialog()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 不清楚什么原因AlertDialog类型的弹框不显示
     */
    private fun showDialog() {
        val dialog = AlertDialog.Builder(this).setTitle("这是标题")
            .setMessage("这是对话框中的内容")
            .create()
        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        dialog.window?.setType(type)
        dialog.show()
    }

    /**
     * BottomSheetDialog实现
     */
    private fun showBottomSheetDialog() {
        val sheetDialog = BottomSheetDialog(this)
        val view = View.inflate(this, R.layout.dialog_bottom, null)
        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        sheetDialog.setContentView(view)
        sheetDialog.window?.setType(type)
        sheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        sheetDialog.show()
    }

}
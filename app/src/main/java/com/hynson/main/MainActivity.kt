package com.hynson.main

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hynson.R
import com.hynson.aidl.AidlActivity
import com.hynson.alertwindow.AlertWindowActivity
import com.hynson.chart.ChartActivity
import com.hynson.coordinatorlayout.CoordinatorActivity
import com.hynson.coroutine.CoroutineActivity
import com.hynson.crash.CrashActivity
import com.hynson.customview.CustomViewActivity
import com.hynson.databinding.ActivityMainBinding
import com.hynson.databinding.DBLoginActivity
import com.hynson.floatkkey.FloatKeyActivity
import com.hynson.gson.GsonActivity
import com.hynson.mbedtls.MbedtlsActivity
import com.hynson.navigation.NavigationActivity
import com.hynson.opensl.OpenslActivity
import com.hynson.set.SettingActivity
import com.hynson.shortcut.ShortcutActivity
import com.hynson.topbar.TopBarActivity
import com.hynson.webview.WebviewActivity

class MainActivity : FragmentActivity() {
    private val TAG = MainActivity::class.java.simpleName
    var binding: ActivityMainBinding? = null

    private val contentAdapter by lazy {
        ContentAdapter(getContentList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.activity = this
        binding?.rvContents?.apply {

            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = contentAdapter
        }
        contentAdapter.apply {
            setOnItemClickListener { _, _, position ->
                items[position].itemAction?.invoke(position)
            }
        }
        val message = Message.obtain()
        val handler = Handler(mainLooper)
        val msg = handler.obtainMessage()
        msg.arg1 = 2
        handler.sendMessage(msg)
        Log.i(TAG, "onCreate: " + ",hash: " + System.identityHashCode(this))
        Log.v("", "")
    }

    private fun getContentList(): List<Content> {

        val contentList = ArrayList<Content>()
        initCustomView(contentList)
        initDialog(contentList)
        contentList.add(Content(Content.ITEM_TYPE, name = "Chart", itemAction = {
            startActivity(ChartActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "WebView", itemAction = {
            startActivity(WebviewActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "DataBinding", itemAction = {
            startActivity(DBLoginActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Navigation", itemAction = {
            startActivity(NavigationActivity::class.java)
        }))
        initClassicUIInteractive(contentList)
        contentList.add(Content(Content.ITEM_TYPE, name = "testCrash", itemAction = {
            startActivity(CrashActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Mbedtls", itemAction = {
            startActivity(MbedtlsActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "OpenSL", itemAction = {
            startActivity(OpenslActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "AIDL", itemAction = {
            startActivity(AidlActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Coroutine", itemAction = {
            startActivity(CoroutineActivity::class.java)
        }))

        contentList.add(Content(Content.ITEM_TYPE, name = "Setting", itemAction = {
            startActivity(SettingActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Shortcut", itemAction = {
            startActivity(ShortcutActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Gson", itemAction = {
            startActivity(GsonActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "AlertWindow", itemAction = {
            startActivity(AlertWindowActivity::class.java)
        }))
        return contentList
    }

    private fun initCustomView(contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(Cell("TimeLine") {

        }, Cell("NumText") {

        }, Cell("RulerView") {

        }, Cell("FlowLayout") {

        }, Cell("PieChart") {

        }, Cell("BPChart") {

        }, Cell("MyView") {

        }, Cell("BarView") {

        })

        contents.add(Content(Content.ITEM_TYPE, name = "CustomView"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun initDialog(contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(Cell("AlertDialog") {
            showAlertDialog()
        }, Cell("BottomSheetDialog") {
            showBottomSheetDialog()
        }, Cell("Dialog") {
            showBottomDialog()
        })

        contents.add(Content(Content.ITEM_TYPE, name = "Dialog Gather"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun showAlertDialog() {
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

    private fun initClassicUIInteractive(contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(Cell("TopBar") {
            startActivity(TopBarActivity::class.java)
        }, Cell("Coordinator") {
            startActivity(CoordinatorActivity::class.java)
        }, Cell("FloatKey") {
            startActivity(FloatKeyActivity::class.java)
        })
        contents.add(Content(Content.ITEM_TYPE, name = "Classic UI interactive"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.component = ComponentName(this, cls)
        startActivity(intent)
    }

    /**
     * 自定义Dialog实现
     */
    private fun showBottomDialog() {
        val dialog = Dialog(this, R.style.DialogTheme).apply {
            val view = View.inflate(context, R.layout.dialog_bottom, null)
            setContentView(view)
        }

        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        dialog.window?.apply {
            setType(type)
            setGravity(Gravity.BOTTOM) //设置弹出位置
            setWindowAnimations(R.style.main_menu_animStyle) //设置弹出动画
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ) //设置对话框大小
        }

        dialog.show()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val edit = dialog.findViewById<EditText>(R.id.edt_name)
            edit.requestFocus()
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edit, 0)
        }, 200)

        dialog.findViewById<View>(R.id.tv_take_photo).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.tv_take_pic).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.tv_cancel).setOnClickListener { dialog.dismiss() }
    }
}
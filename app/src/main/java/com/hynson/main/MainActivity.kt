package com.hynson.main

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastdroid.ktbase.BaseMvvmActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hynson.R
import com.hynson.databinding.ActivityMainBinding
import com.hynson.language.AppLanguage
import com.hynson.language.LanguageAdapter
import java.util.Locale

class MainActivity : BaseMvvmActivity<ActivityMainBinding, MainVM>() {
    private val TAG = MainActivity::class.java.simpleName

    private val contentAdapter = ContentAdapter()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            showListDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getVm(provider: ViewModelProvider) = provider[MainVM::class.java]

    override fun bindView() {
        super.bindView()
        bind.rvContents.apply {
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

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        super.initData(owner, savedInstanceState)
        vm.actionList = arrayListOf(
            { v, p, cell -> showAlertDialog() },
            { v, p, cell -> showBottomSheetDialog() },
            { v, p, cell -> showBottomDialog() },
            { v, p, cell -> showPopupMenu(v) })
        contentAdapter.addAll(vm.getContentList(this))
    }

    private fun showPopupMenu(anchor: View) {
        val popupMenu = PopupMenu(this, anchor).apply {
            menuInflater.inflate(R.menu.menu_settings, menu)
            setOnMenuItemClickListener { item ->
                return@setOnMenuItemClickListener when (item.itemId) {
                    R.id.action_language -> {
                        showListDialog()
                        true
                    }

                    else -> false
                }
            }
        }
        popupMenu.show()
    }

    private fun showListDialog() {
        val languages =
            listOf(AppLanguage(Locale.CHINESE, "中文"), AppLanguage(Locale.ENGLISH, "English"))
        val adapter = LanguageAdapter(this, R.layout.item_app_language, languages)
        val listDialog = AlertDialog.Builder(this).apply {
            setTitle(R.string.change_language)
            setAdapter(adapter) { _, pos ->
                Toast.makeText(
                    this@MainActivity,
                    "切换${languages[pos].locale.language}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                changeLanguage(this@MainActivity, languages[pos].locale)
            }
        }
        listDialog.show()
    }

    /*修改应用内语言设置*/
    fun changeLanguage(context: Activity, newLocale: Locale = Locale.ROOT) {
        setAppLanguage(context, newLocale)
        ActivityCompat.recreate(context)
    }

    /*设置语言*/
    private fun setAppLanguage(context: Context, locale: Locale) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        //Android 7.0以上的方法
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale)
            configuration.setLocales(LocaleList(locale))
            context.createConfigurationContext(configuration)
            //实测，updateConfiguration这个方法虽然很多博主说是版本不适用
            //但是我的生产环境androidX+Android Q环境下，必须加上这一句，才可以通过重启App来切换语言
            resources.updateConfiguration(configuration, metrics)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Android 4.1 以上方法
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, metrics)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, metrics)
        }
    }


    private fun showAlertDialog() {
        val dialog = AlertDialog.Builder(this).setTitle("这是标题")
            .setMessage("这是对话框中的内容")
            .create()
        dialog.show()
    }

    /**
     * BottomSheetDialog实现
     */
    private fun showBottomSheetDialog() {
        val sheetDialog = BottomSheetDialog(this)
        val view = View.inflate(this, R.layout.dialog_bottom, null)
        sheetDialog.setContentView(view)
        sheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        sheetDialog.show()
    }

    /**
     * 自定义Dialog实现
     */
    private fun showBottomDialog() {
        val dialog = Dialog(this, R.style.DialogTheme).apply {
            val view = View.inflate(context, R.layout.dialog_bottom, null)
            setContentView(view)
        }
        dialog.window?.apply {
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
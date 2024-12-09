package com.hynson.main

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import com.fastdroid.ktbase.BaseVM
import com.hynson.R
import com.hynson.activityresult.GetResultActivity
import com.hynson.aidl.AidlActivity
import com.hynson.alertwindow.AlertWindowActivity
import com.hynson.chart.ChartActivity
import com.hynson.coordinatorlayout.CoordinatorActivity
import com.hynson.coroutine.CoroutineActivity
import com.hynson.crash.CrashActivity
import com.hynson.customview.CustomViewActivity
import com.hynson.databinding.DBLoginActivity
import com.hynson.detail.DetailActivity
import com.hynson.floatkkey.FloatKeyActivity
import com.hynson.gson.GsonActivity
import com.hynson.mbedtls.MbedtlsActivity
import com.hynson.mvvm.TestBaseActivity
import com.hynson.mvvm.TestMVVMActivity
import com.hynson.navigation.NavigationActivity
import com.hynson.opensl.OpenslActivity
import com.hynson.set.SettingActivity
import com.hynson.shortcut.ShortcutActivity
import com.hynson.topbar.TopBarActivity
import com.hynson.webview.WebviewActivity

class MainVM : BaseVM() {
    var actionList = mutableListOf<((v: View, position: Int, cell: Cell) -> (Unit))?>()

    private fun startActivity(context: Context, cls: Class<*>) {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.component = ComponentName(context, cls)
        context.startActivity(intent)
    }

    fun getContentList(context: Context): List<Content> {
        val contentList = ArrayList<Content>()
        initCustomView(context, contentList)
        initDialog(contentList)
        contentList.add(
            Content(
                Content.ITEM_TYPE,
                name = context.getString(R.string.chart),
                itemAction = {
                    startActivity(context, ChartActivity::class.java)
                })
        )
        contentList.add(Content(Content.ITEM_TYPE, name = "WebView", itemAction = {
            startActivity(context, WebviewActivity::class.java)
        }))
        initMvvmTest(context, contentList)
        contentList.add(Content(Content.ITEM_TYPE, name = "DataBinding", itemAction = {
            startActivity(context, DBLoginActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Navigation", itemAction = {
            startActivity(context, NavigationActivity::class.java)
        }))
        initClassicUIInteractive(context, contentList)
        contentList.add(
            Content(
                Content.ITEM_TYPE,
                name = context.getString(R.string.activity_result),
                itemAction = {
                    startActivity(context, GetResultActivity::class.java)
                })
        )
        contentList.add(Content(Content.ITEM_TYPE, name = "testCrash", itemAction = {
            startActivity(context, CrashActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Mbedtls", itemAction = {
            startActivity(context, MbedtlsActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "OpenSL", itemAction = {
            startActivity(context, OpenslActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "AIDL", itemAction = {
            startActivity(context, AidlActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Coroutine", itemAction = {
            startActivity(context, CoroutineActivity::class.java)
        }))

        contentList.add(Content(Content.ITEM_TYPE, name = "Setting", itemAction = {
            startActivity(context, SettingActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Shortcut", itemAction = {
            startActivity(context, ShortcutActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Gson", itemAction = {
            startActivity(context, GsonActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "AlertWindow", itemAction = {
            startActivity(context, AlertWindowActivity::class.java)
        }))
        contentList.add(Content(Content.ITEM_TYPE, name = "Detail", itemAction = {
            startActivity(context, DetailActivity::class.java)
        }))
        return contentList
    }

    private fun initDialog(contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(
            Cell("AlertDialog", action = actionList[0]),
            Cell("BottomSheetDialog", action = actionList[1]),
            Cell("Dialog", action = actionList[2]),
            Cell("PopupMenu", action = actionList[3])
        )
        contents.add(Content(Content.ITEM_TYPE, name = "Dialog Gather"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun initClassicUIInteractive(context: Context, contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(Cell("TopBar") { _, _, _ ->
            startActivity(context, TopBarActivity::class.java)
        }, Cell("Coordinator") { _, _, _ ->
            startActivity(context, CoordinatorActivity::class.java)
        }, Cell("FloatKey") { _, _, _ ->
            startActivity(context, FloatKeyActivity::class.java)
        }, Cell("Ble") { _, _, _ ->
            startActivity(context, com.hynson.ble.MainActivity::class.java)
        })
        contents.add(Content(Content.ITEM_TYPE, name = "Classic UI interactive"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun initCustomView(context: Context, contents: MutableList<Content>) {
        val action = { v: View, position: Int, cell: Cell ->
            CustomViewActivity.start(context, cell.resId)
        }
        val customCells =
            arrayListOf<Cell>(
                Cell("TimeLine", R.id.timelineFragment, action = action),
                Cell("NumText", R.id.numTextFragment, action = action),
                Cell("RulerView", R.id.rulerViewFragment, action = action),
                Cell("FlowLayout", R.id.flowLayoutFragment, action = action),
                Cell("PieChart", R.id.pieChartFragment, action = action),
                Cell("BPChart", R.id.BPChartFragment, action = action),
                Cell("MyView", R.id.myViewFragment, action = action),
                Cell("BarView", R.id.chartFragment, action = action),
                Cell("ExpandFragment", R.id.expandFragment, action = action)
            )

        contents.add(Content(Content.ITEM_TYPE, name = "CustomView"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }

    private fun initMvvmTest(context: Context, contents: MutableList<Content>) {
        val customCells = arrayListOf<Cell>(Cell("不带VM的Activity") { _, pos, cell ->
            startActivity(context, TestBaseActivity::class.java)
        }, Cell("带VM的Activity") { _, pos, cell ->
            startActivity(context, TestMVVMActivity::class.java)
        })

        contents.add(Content(Content.ITEM_TYPE, name = "Activity"))
        contents.add(Content(Content.SECTION_TYPE, cells = customCells))
    }
}
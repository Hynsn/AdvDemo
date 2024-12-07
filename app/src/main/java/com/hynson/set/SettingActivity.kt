package com.hynson.set

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastdroid.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivitySettingBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 13:33
 * Description: 设置页
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getLayout() = R.layout.activity_setting

    override fun bindView() {

        val itemList: MutableList<BaseBean> = ArrayList()
        itemList.add(NewTest1())
        itemList.add(NewTest3(name = "DeviceInfo", value = "跳转到另外一个页面") { "" })

        itemList.add(NewTest2(name = "DeviceName", value = "358") { "" })
        itemList.add(NewTest2(name = "Icon", value = "图") { "" })
        itemList.add(NewTest1())

        itemList.add(NewTest2(name = "Voice Commands", value = "") { "" })
        itemList.add(NewTest1())

        itemList.add(NewTest4(name = "Unit", value = "华氏度/摄氏度") { "" })
        itemList.add(NewTest4(name = "Permission", value = "获取权限") { "" })
        itemList.add(NewTest1())

        itemList.add(NewTest4(name = "Add to Home Screen", value = "快捷方式") { "" })
        itemList.add(NewTest5 { "" })
        itemList.add(NewTest4(name = "WiFi Settings", value = "快捷方式") { "" })
        itemList.add(NewTest4(name = "Share Device", value = "快捷方式") { "" })
        itemList.add(NewTest1())
//        itemList.add(NewTest5 { "" })

        val hash = LinkedHashSet<Int>()

        itemList.forEach {
            hash.add(it::class.hashCode())
        }
        itemList.forEach {
            it.type = hash.indexOf(it::class.hashCode())
        }

        Log.i(TAG, "list: ${hash.toList()}")
        val vhHash = LinkedHashMap<Int, VHInf<*>>()

        val vhList = ServiceLoader.load(VHInf::class.java).toList()
        val vhCloneList = ArrayList<VHInf<*>>()
        vhList.forEach {
            it.clazz.let { it1 -> vhHash[it1.hashCode()] = it }
        }
        hash.forEach {
            vhHash[it]?.let { it1 -> vhCloneList.add(it1) }
        }
        Log.i(TAG, "list: $vhCloneList")

        val itemAdapter = ItemAdapter(this, vhCloneList, itemList)
        binding.rvSet.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = itemAdapter
        }
    }

    companion object {
        val TAG = SettingActivity::class.java.simpleName
    }
}
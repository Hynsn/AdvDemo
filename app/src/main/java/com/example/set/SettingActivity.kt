package com.example.set

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.base.BaseActivity
import com.example.R
import com.example.databinding.ActivitySettingBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 13:33
 * Description: 设置页
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
class SettingActivity: BaseActivity<ActivitySettingBinding>() {
    override fun getLayout() = R.layout.activity_setting

    override fun bindView() {
        val itemList: MutableList<BaseBean> = ArrayList()
        itemList.add(NewTest1(name = "测试1") {
//            test()
            Log.i(TAG,"测试代码")
        })
        itemList.add(NewTest1(name = "测试2"))
        itemList.add(NewTest1(name = "测试3") {  })
        itemList.add(NewTest2(name = "测试5",value = "哈哈") { "" })
        itemList.add(NewTest1(name = "测试6") {  })
        itemList.add(NewTest2(name = "测试7",value = "哈哈") { "" })

        val itemAdapter = ItemAdapter(this, itemList)
        binding.rvSet.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = itemAdapter
        }
    }

    private fun test(){
        val clazz = ServiceLoader.load(VHInf::class.java).toList()
        clazz.forEach { it ->
            Log.i(TAG,"test:${it.type}")
        }
    }

    companion object{
        val TAG = SettingActivity::class.java.simpleName
    }
}
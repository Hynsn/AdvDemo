package com.example.set

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.base.BaseActivity
import com.example.R
import com.example.databinding.ActivityDataBindingBinding
import com.example.databinding.ActivitySettingBinding

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
        itemList.add(NewTest1(1,"测试") {
            Log.i(TAG,"测试代码")
            ""
        })
        itemList.add(NewTest1(2,"测试1") { "" })
        itemList.add(NewTest1(3,"测试2") { "" })
//        itemList.add(BaseBean(R.layout.activity_aidl))
        itemList.add(NewTest1(4,"测试3") { "" })

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

    companion object{
        val TAG = SettingActivity::class.java.simpleName
    }
}
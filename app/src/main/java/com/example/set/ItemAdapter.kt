package com.example.set

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.R

class ItemAdapter(private val context: Context, var testItemList: List<BaseBean>) :
    RecyclerView.Adapter<BaseVH<BaseBean>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<BaseBean> {
        val view =  NewTest(LayoutInflater.from(parent.context).inflate(R.layout.item_left_right_text, parent, false))
        Log.i(SettingActivity.TAG,"viewType:$viewType")
        return view as BaseVH<BaseBean>
    }

    override fun onBindViewHolder(holder: BaseVH<BaseBean>, position: Int) {
        val testItem = testItemList[position]
        holder.a = testItem
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return testItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return testItemList[position].type
    }
}



package com.example.set

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ItemAdapter(private val context: Context,val vhList:List<VHInf<*>>, var testItemList: List<BaseBean>) :
    RecyclerView.Adapter<BaseHolder<BaseBean>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<BaseBean> {
        val viewHold = vhList[viewType]
        Log.i(TAG,"className:${viewHold::class.simpleName}")

        return viewHold.convert(parent) as BaseHolder<BaseBean>
    }

    override fun onBindViewHolder(holder: BaseHolder<BaseBean>, position: Int) {
        val testItem = testItemList[position]
        holder.bean = testItem
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return testItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return testItemList[position].type
    }
    companion object{
        val TAG = ItemAdapter::class.simpleName
    }
}



package com.example.set

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 10:11
 * Description:
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
abstract class BaseVH<T:BaseBean>(itemView: View) : RecyclerView.ViewHolder(itemView){
    abstract var a:T
    val resId:Int = a.layout
    abstract fun bindData()
}
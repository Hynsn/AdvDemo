package com.example.set

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 10:11
 * Description:
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
abstract class BaseBean {
    abstract val type: Int
}

abstract class BaseHolder<T : BaseBean>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var root: View = itemView
    var bean: T? = null
    abstract fun bindData()
}

abstract class BaseVH<T> : VHInf<T> {
    override val type: ViewEum
        get() = ViewEum.Type1

    fun getView(parent: ViewGroup, layout: Int): View {
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }
}

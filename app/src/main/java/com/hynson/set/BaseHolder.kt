package com.hynson.set

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
abstract class BaseBean(var type: Int = 0)

abstract class BaseHolder<T : BaseBean>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var root: View = itemView
    var bean: T? = null
    abstract fun bindData()
}

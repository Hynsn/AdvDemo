package com.example.set

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.R
import com.google.auto.service.AutoService

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 10:27
 * Description:
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
data class NewTest4(
    override val type: Int = ViewEum.Type4.ordinal,
    var name: String,
    var value: String,
    val action: () -> (String),
) : BaseBean()

class NewTest4VH(itemView: View) : BaseHolder<NewTest4>(itemView) {
    override fun bindData() {
        bean?.run {
            root.findViewById<TextView>(R.id.tv_name).text = name
        }
    }
}

@AutoService(VHInf::class)
class NewTestVH4 : BaseVH<NewTest4VH>() {
    override val type: ViewEum
        get() = ViewEum.Type4

    override fun convert(parent: ViewGroup): NewTest4VH {
        return NewTest4VH(getView(parent, type.layout))
    }
}


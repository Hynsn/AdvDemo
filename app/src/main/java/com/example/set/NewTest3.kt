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
data class NewTest3(
    override val type: Int = ViewEum.Type3.ordinal,
    var name: String,
    var value: String,
    val action: () -> (String),
) : BaseBean()

class NewTest3VH(itemView: View) : BaseHolder<NewTest3>(itemView) {
    override fun bindData() {
        bean?.run {
            root.findViewById<TextView>(R.id.tv_name).text = name
            root.findViewById<TextView>(R.id.tv_value).text = value
        }
    }
}

@AutoService(VHInf::class)
class NewTestVH3 : BaseVH<NewTest3VH>() {
    override val type: ViewEum
        get() = ViewEum.Type3

    override fun convert(parent: ViewGroup): NewTest3VH {
        return NewTest3VH(getView(parent, type.layout))
    }
}


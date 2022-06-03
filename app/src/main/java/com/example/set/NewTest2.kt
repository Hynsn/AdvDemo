package com.example.set

import android.view.View
import android.view.ViewGroup
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
data class NewTest2(
    override val type: Int = ViewEum.Type2.ordinal,
    var name: String,
    var value: String,
    val action: () -> (String),
) : BaseBean()

class NewTest2VH(itemView: View) : BaseHolder<NewTest2>(itemView) {
    override fun bindData() {
        bean?.run {
            root.findViewById<TextView>(R.id.tv_left).text = name
            root.findViewById<TextView>(R.id.tv_right).text = value
            root.findViewById<ImageView>(R.id.iv_icon).setOnClickListener {
                action.invoke()
            }
        }
    }
}

@AutoService(VHInf::class)
class NewTestVH2 : BaseVH<NewTest2VH>() {
    override val type: ViewEum
        get() = ViewEum.Type2

    override fun convert(parent: ViewGroup): NewTest2VH {
        return NewTest2VH(getView(parent, type.layout))
    }
}

package com.hynson.set

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hynson.R
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
class NewTestVH3 : VHInf<NewTest3VH> {
    override val layout = R.layout.item_set_type3
    override val clazz: Class<*> = NewTest3::class.java

    override fun convert(parent: ViewGroup): NewTest3VH {
        return NewTest3VH(getView(parent, layout))
    }
}


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
data class NewTest5(
    override val type: Int = ViewEum.Type5.ordinal,
    val action: () -> (String),
) : BaseBean()

class NewTest5VH(itemView: View) : BaseHolder<NewTest5>(itemView) {
    override fun bindData() {
        bean?.run {
            root.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                action.invoke()
            }
        }
    }
}

@AutoService(VHInf::class)
class NewTestVH5 : BaseVH<NewTest5VH>() {
    override val type: ViewEum
        get() = ViewEum.Type5

    override fun convert(parent: ViewGroup): NewTest5VH {
        return NewTest5VH(getView(parent, type.layout))
    }
}


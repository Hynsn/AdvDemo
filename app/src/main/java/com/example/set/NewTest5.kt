package com.example.set

import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
class NewTestVH5 : VHInf<NewTest5VH> {
    override val layout = R.layout.item_set_type5
    override val clazz: Class<*> = NewTest5::class.java

    override fun convert(parent: ViewGroup): NewTest5VH {
        return NewTest5VH(getView(parent, layout))
    }
}


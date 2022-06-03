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

data class NewTest1(
    override val type: Int = ViewEum.Type1.ordinal,
    var name: String,
    val action: (() -> (Unit))? = null,
) : BaseBean()

class NewTest(itemView: View) : BaseHolder<NewTest1>(itemView) {

    override fun bindData() {
        bean?.run {
            root.findViewById<TextView>(R.id.tv_left).text = name
            root.findViewById<ImageView>(R.id.iv_icon).setOnClickListener {
                action?.invoke()
            }
            root.setOnClickListener {
                action?.invoke()
            }
        }
    }
}

@AutoService(VHInf::class)
class NewTestVH1 : BaseVH<NewTest>() {
    override val type: ViewEum
        get() = ViewEum.Type1

    override fun convert(parent: ViewGroup): NewTest {
        return NewTest(getView(parent, type.layout))
    }

}
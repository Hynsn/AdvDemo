package com.example.set

import android.view.View
import android.widget.TextView
import com.example.R

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 10:27
 * Description:
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */

data class NewTest1(
    override val layout: Int = R.layout.item_left_right_text,
    var name: String,
    val action: () -> (String)
) : BaseBean(layout){
    constructor(name: String,action: () -> String):this(R.layout.item_left_right_text,name,action)
}

class NewTest(itemView: View) : BaseVH<NewTest1>(itemView) {

    override fun bindData() {
        a.run {
            itemView.findViewById<TextView>(R.id.tv_left).text = name
            itemView.findViewById<TextView>(R.id.iv_icon).setOnClickListener {
                action.invoke()
            }
        }
    }

    override var a: NewTest1
        get() = NewTest1("") {
            ""
        }
        set(value) {}
}
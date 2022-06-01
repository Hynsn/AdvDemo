package com.example.set

import android.view.View
import android.widget.ImageView
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

data class NewTest2(
    override var type: Int,
    var name: String,
    val action: () -> (String),
) : BaseBean(type){
}

class NewTest2VH(itemView: View) : BaseVH<NewTest1>(itemView) {

    override fun bindData() {
        a?.run {
            root.findViewById<TextView>(R.id.tv_left).text = name
            root.findViewById<ImageView>(R.id.iv_icon).setOnClickListener {
                action.invoke()
            }
        }
    }

    override var a: NewTest1? = null
}
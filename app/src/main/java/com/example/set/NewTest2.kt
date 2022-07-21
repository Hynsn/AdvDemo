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
class NewTestVH2 : VHInf<NewTest2VH> {
    override val layout = R.layout.item_set_type2

    override fun convert(parent: ViewGroup): NewTest2VH {
        return NewTest2VH(getView(parent, layout))
    }
}

// 两种维护需求
// 1.设置页配置化，如下
// 2.方便新增不同样式的Item(ViewHolder)

//// 实体
//abstract val bean:T
//// 视图
//abstract val layout:Int
//// 供适配器绑定
//abstract fun bindView()

// view可能公用，事件类型可能不一样
// 需要绑定view和事件类型，多对多的关系

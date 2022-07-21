package com.example.set

import android.view.View
import android.view.ViewGroup
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
    val a:Int = 0
) : BaseBean()

class NewTest(itemView: View) : BaseHolder<NewTest1>(itemView) {

    override fun bindData() {
        bean?.run {

        }
    }
}

@AutoService(VHInf::class)
class NewTestVH1 : VHInf<NewTest> {
    override val layout = R.layout.item_set_type1
    override val clazz: Class<*> = NewTest1::class.java

    override fun convert(parent: ViewGroup): NewTest {
        return NewTest(getView(parent, layout))
    }

}

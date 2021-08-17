package com.example.test

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Author: Hynsonhou
 * Date: 2021/8/16 13:45
 * Description: 熟度选择
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/16   1.0       首次创建
 */
class DonenessPrepareAdapter : BaseQuickAdapter<SubFoodObj,BaseViewHolder>(R.layout.ag500_doneness_unchose_item) {
    override fun convert(holder: BaseViewHolder, item: SubFoodObj) {
        holder.setText(R.id.name,item.subItemName)
        val iconIV = holder.getView(R.id.icon) as ImageView
        Glide.with(iconIV.context)
            .load(item.subItemImageUrl)
            .into(iconIV)
        val anim = android.view.animation.AnimationUtils.loadAnimation(iconIV.context,R.anim.to_big_anima)
        iconIV.startAnimation(anim)
    }
}
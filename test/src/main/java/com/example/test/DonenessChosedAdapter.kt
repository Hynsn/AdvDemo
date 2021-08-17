package com.example.test

import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.animation.AnimationUtils

/**
 * Author: Hynsonhou
 * Date: 2021/8/16 13:45
 * Description: 熟度选择
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/16   1.0       首次创建
 */
class DonenessChosedAdapter : BaseQuickAdapter<ChosedItem,BaseViewHolder>(R.layout.ag500_doneness_chosed_item) {
    override fun convert(holder: BaseViewHolder, item: ChosedItem) {
        holder.setText(R.id.name,item.name)
        holder.setText(R.id.rv_chosedname,item.chosedName)
        val iconIV = holder.getView(R.id.iv_chosedicon) as ImageView
        Glide.with(iconIV.context)
            .load(item.iconUrl)
            .into(iconIV)
    }
}
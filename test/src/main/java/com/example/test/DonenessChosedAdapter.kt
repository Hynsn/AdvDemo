package com.example.test

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    class ItemDecoration(val leftRight: Int, val topBottom: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val layoutManager: LinearLayoutManager = parent.layoutManager as LinearLayoutManager
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                    outRect.bottom = topBottom
                }
                outRect.top = topBottom
                outRect.left = leftRight
                outRect.right = leftRight
            } else {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                    outRect.right = leftRight
                }
                outRect.top = topBottom
                outRect.left = leftRight
                outRect.bottom = topBottom
            }
        }
    }
}
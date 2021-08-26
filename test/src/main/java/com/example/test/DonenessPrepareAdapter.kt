package com.example.test

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val anim = android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context,R.anim.to_big_anima)
        holder.itemView.startAnimation(anim)
    }
    class ItemDecoration(val leftRight: Int, val topBottom: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val layoutManager = parent.layoutManager as GridLayoutManager?
            val lp = view.layoutParams as GridLayoutManager.LayoutParams
            val childPosition = parent.getChildAdapterPosition(view)
            val spanCount = layoutManager!!.spanCount

            if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
                //判断是否在第一排
                if (layoutManager.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) { //第一排的需要上面
                    outRect.top = topBottom
                }
                outRect.bottom = topBottom
                //这里忽略和合并项的问题，只考虑占满和单一的问题
                if (lp.spanSize == spanCount) { //占满
                    outRect.left = leftRight
                    outRect.right = leftRight
                } else {
                    outRect.left = ((spanCount - lp.spanIndex).toFloat() / spanCount * leftRight).toInt()
                    outRect.right = (leftRight.toFloat() * (spanCount + 1) / spanCount - outRect.left).toInt()
                }
            } else {
                if (layoutManager.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) { //第一排的需要left
                    outRect.left = leftRight
                }
                outRect.right = leftRight
                //这里忽略和合并项的问题，只考虑占满和单一的问题
                if (lp.spanSize == spanCount) { //占满
                    outRect.top = topBottom
                    outRect.bottom = topBottom
                } else {
                    outRect.top = ((spanCount - lp.spanIndex).toFloat() / spanCount * topBottom).toInt()
                    outRect.bottom = (topBottom.toFloat() * (spanCount + 1) / spanCount - outRect.top).toInt()
                }
            }
        }
    }
}
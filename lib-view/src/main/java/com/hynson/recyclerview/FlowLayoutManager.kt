package com.hynson.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FlowLayoutManager : RecyclerView.LayoutManager() {

    // 自动布局
    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    // 自适应
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        // 将显示的Item离屏放入缓存
        detachAndScrapAttachedViews(recycler)
        var xLineOffset = 0 // 累加item布局时的x轴偏移
        var yLineOffset = 0 // 累加item布局时的y轴偏移
        var lastLineMaxHeight = 0
        for (i in 0 until itemCount) {
            val view: View = recycler.getViewForPosition(i)
            // 获取Item的布局参数，计算位置时需要加上margin
            val params = view.layoutParams as RecyclerView.LayoutParams
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val width = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin
            val height = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
            // 累加当前行已有item的宽度
            xLineOffset += width

            // 剩余宽度
            val rWidth = getWidth() - paddingLeft - paddingRight
            // 累加宽度小于RecycleView宽度无需换行
            if (xLineOffset <= rWidth) {
                val left = xLineOffset - width + params.leftMargin + paddingLeft
                val top = yLineOffset + params.topMargin
                val right = xLineOffset - params.rightMargin + paddingRight
                val bottom = yLineOffset + height - params.bottomMargin
                // 布局Item
                layoutDecorated(view, left, top, right, bottom)

                // 比较记录Item最大高度
                lastLineMaxHeight = lastLineMaxHeight.coerceAtLeast(height)
            } else { //换行
                xLineOffset = width
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height
                }
                //更新记录当前行偏移
                yLineOffset += lastLineMaxHeight
                val left = params.leftMargin + paddingLeft
                val top = yLineOffset + params.topMargin
                val right = width - params.rightMargin + paddingRight
                val bottom = yLineOffset + height - params.bottomMargin
                layoutDecorated(view, left, top, right, bottom)
                lastLineMaxHeight = height
            }
        }
    }
}
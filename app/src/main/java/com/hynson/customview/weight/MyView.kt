package com.hynson.customview.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class MyView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var paint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = 100f
        color = Color.RED
    }


    val TEXT = "测试"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = getTextRect()
        val width = measuredWidth
        val height = measuredHeight

        // drawText x值代表绘制起点坐标，所以要减掉文本宽度
        val x = (width - rect.width()) / 2f

        val fm = paint.fontMetrics
        // 计算字体居中偏移
        val dy = (fm.descent - fm.ascent) / 2 - fm.descent
        val y = height / 2 + dy

        canvas.drawText(TEXT, x, y, paint)
    }

    // 根据Paint绘制参数计算文本所占宽高
    private fun getTextRect():Rect {
        val rect = Rect()
        paint.getTextBounds(TEXT, 0, TEXT.length, rect)
        return rect
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val rect = getTextRect()
        val width = measureWidth(widthMeasureSpec,rect.width())
        val height = measureHeight(heightMeasureSpec,rect.height())
        setMeasuredDimension(width,height)
    }

    private fun measureWidth(measureSpec: Int, width: Int): Int {
        // 获取子View的宽度测量模式
        val mode = MeasureSpec.getMode(measureSpec)
        // 获取子View的宽度Size
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> { // match_parent或具体值时
                size
            }
            MeasureSpec.AT_MOST -> { // wrap_content时
                width
            }
            else -> {
                0
            }
        }
    }

    private fun measureHeight(measureSpec: Int, height: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> { // match_parent或具体值时
                size
            }
            MeasureSpec.AT_MOST -> { // wrap_content时
                height
            }
            else -> {
                0
            }
        }
    }

}
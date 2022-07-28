package com.hynson.bpchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.hynson.R
import com.hynson.chart.BPXaxisChart
import utils.Screen

/**
 * Author: Hynsonhou
 * Date: 2022/7/28 17:34
 * Description: 柱状、散点图
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/7/28   1.0       首次创建
 */
class BarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var paint: Paint = Paint()
    private var textSize = 10f
    private var textColor = ContextCompat.getColor(context, R.color.color_a600)

    private var barWidth = 0

    private var spInMax: Float = 0f
    private var spInMin: Float = 0f
    private var dpInMax: Float = 0f
    private var dpInMin: Float = 0f

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.BPXaxisChart)

        textColor = t.getColor(R.styleable.BPXaxisChart_textColorX, textColor)
        textSize = t.getDimension(R.styleable.BPXaxisChart_textSizeX, 10f)

        barWidth = Screen.dp2px(getContext(), 7f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val p = Point(1f, 2f)
        val r = barWidth / 2f

        // 圆点
        setPaintStyle(BPXaxisChart.PaintType.SYS_PAINT)
        val cx = width / 2f
        val spMinY = spInMin + bottom
        val spMaxY = spInMin + bottom

        canvas.drawCircle(cx, spMaxY, r, paint)
        canvas.drawCircle(cx, spMinY, r, paint)
        // 柱状线
        setPaintStyle(BPXaxisChart.PaintType.SYS_LINE)
        canvas.drawLine(cx, spMaxY, cx, spMinY, paint)

        val dpMinY = dpInMin + bottom
        val dpMaxY = dpInMax + bottom
        // 棱型
        setPaintStyle(BPXaxisChart.PaintType.DIA_PAINT)
        drawDiaData(canvas, cx, dpMaxY, r)
        drawDiaData(canvas, cx, dpMinY, r)
        setPaintStyle(BPXaxisChart.PaintType.DIA_LINE)

        canvas.drawLine(cx, dpMaxY, cx, dpMinY, paint)
    }

    fun setData(
        spInMax: Int,
        spInMin: Int,
        dpInMax: Int,
        dpInMin: Int
    ) {
        this.spInMax = spInMax.toFloat()
        this.spInMin = spInMin.toFloat()
        this.dpInMax = dpInMax.toFloat()
        this.dpInMin = dpInMin.toFloat()
    }

    private fun drawDiaData(canvas: Canvas, x: Float, y: Float, r: Float) {
        setPaintStyle(BPXaxisChart.PaintType.DIA_PAINT)
        val path = Path()
        path.moveTo(x - r, y)
        path.lineTo(x, y - r)
        path.lineTo(x + r, y)
        path.lineTo(x, y + r)
        path.lineTo(x - r, y)
        canvas.drawPath(path, paint)
    }

    private fun setPaintStyle(type: BPXaxisChart.PaintType) {
        paint.reset()
        when (type) {
            BPXaxisChart.PaintType.YAXIS_LEGEND -> {
                paint.apply {
                    textSize = textSize
                    color = textColor
                    style = Paint.Style.FILL
                    textAlign = Paint.Align.RIGHT
                    isAntiAlias = true
                }
            }
            BPXaxisChart.PaintType.SYS_PAINT -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
                }
            }
            BPXaxisChart.PaintType.DIA_PAINT -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
                }
            }
            BPXaxisChart.PaintType.SYS_LINE -> {
                paint.apply {
                    style = Paint.Style.FILL;
                    isAntiAlias = true;
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
                    alpha = 0x33
                    strokeWidth = Screen.dp2px(context, 7f).toFloat()
                }
            }
            BPXaxisChart.PaintType.DIA_LINE -> {
                paint.apply {
                    style = Paint.Style.FILL;
                    isAntiAlias = true;
                    color = ContextCompat.getColor(context, R.color.color_ffa94d)
                    alpha = 0x33
                    strokeWidth = Screen.dp2px(context, 7f).toFloat()
                }
            }
            BPXaxisChart.PaintType.XAXIS_RULER -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_a600)
                }
            }
        }

    }

    data class Point(var x: Float, var y: Float)
}
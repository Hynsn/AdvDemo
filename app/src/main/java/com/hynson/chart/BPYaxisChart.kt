package com.hynson.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.hynson.R

/**
 * Author: Hynsonhou
 * Date: 2022/7/25 14:07
 * Description: Y轴参考坐标
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/7/25   1.0       首次创建
 */
class BPYaxisChart(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paint: Paint = Paint()

    // 中间指示线
    private var centerXColor = ContextCompat.getColor(context, R.color.color_002c5b)
    private var centerXSize = 10f

    // Y轴虚线
    private var YColor = ContextCompat.getColor(context, R.color.color_3300)
    private var YLineSize = 10f

    // 目标虚线
    private var minColor = ContextCompat.getColor(context, R.color.color_ffa94d)
    private var maxColor = ContextCompat.getColor(context, R.color.color_4daaf7)

    // Y轴图例文本
    private var textSizeY = 10f
    private var textColorY = ContextCompat.getColor(context, R.color.color_a600)
    private var textPaddingLeft = 10f
    private var textPaddingRight = 10f
    private var targetPaddingEnd = 10f
    private var targetPaddingBottom = 10f

    var YLineCount = 5

    var XaxisHeight = 20

    private var maxYaxis = 300f
    private var minYaxis = 0f
    private val lineRange = ArrayList<Float>()

    private var tagetMaxYaxis = 140f
    private var tagetMinYaxis = 90f

    init {

        val t = context.obtainStyledAttributes(attrs, R.styleable.BPYaxisChart)

        centerXColor = t.getColor(R.styleable.BPYaxisChart_centerXColor, centerXColor)
        centerXSize = t.getDimension(R.styleable.BPYaxisChart_centerXSize, 10f)
        YColor = t.getColor(R.styleable.BPYaxisChart_YColor, YColor)
        YLineSize = t.getDimension(R.styleable.BPYaxisChart_centerXSize, 10f)
        textColorY = t.getColor(R.styleable.BPYaxisChart_textColorY, textColorY)
        textSizeY = t.getDimension(R.styleable.BPYaxisChart_textSizeY, 10f)
        textPaddingLeft = t.getDimension(R.styleable.BPYaxisChart_textPaddingLeft, 10f)
        textPaddingRight = t.getDimension(R.styleable.BPYaxisChart_textPaddingRight, 10f)
        targetPaddingEnd = t.getDimension(R.styleable.BPYaxisChart_targetPaddingEnd,10F)
        targetPaddingBottom = t.getDimension(R.styleable.BPYaxisChart_targetPaddingBottom,10F)
    }

    private fun setPaintStyle(type: PaintType) {
        paint.reset()
        when (type) {
            PaintType.CENTER_LINE -> {
                paint.apply {
                    style = Paint.Style.STROKE
                    strokeWidth = centerXSize
                    color = centerXColor
                }
            }
            PaintType.YAXIS_LINE -> {
                val effect = DashPathEffect(floatArrayOf(10f, 10f), 1f)
                paint.apply {
                    style = Paint.Style.STROKE
                    pathEffect = effect
                    strokeWidth = YLineSize
                    color = YColor
                }
            }
            PaintType.TARGET_LINE -> {
                val effect = DashPathEffect(floatArrayOf(10f, 10f), 1f)
                paint.apply {
                    style = Paint.Style.STROKE
                    pathEffect = effect
                    strokeWidth = YLineSize
                }
            }
            PaintType.YAXIS_LEGEND -> {
                paint.apply {
                    textSize = textSizeY
                    color = textColorY
                    style = Paint.Style.FILL
                    textAlign = Paint.Align.RIGHT
                    isAntiAlias = true
                }
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (YLineCount > 2) { // 至少两条线

            val dY = (maxYaxis - minYaxis) / (YLineCount - 1)

            if (lineRange.isNotEmpty()) {
                lineRange.clear()
            }
            for (i in 0 until YLineCount) {
                lineRange.add(i * dY)
            }
            lineRange.forEach {
                drawLegendText(canvas, it)
            }
            val startX = textPaddingLeft + maxLengedWidth + textPaddingRight
            lineRange.forEach {
                drawYaxisLine(canvas, it, startX, false, YColor)
            }
            drawYaxisLine(canvas, tagetMinYaxis, startX, true, minColor)
            drawYaxisLine(canvas, tagetMaxYaxis, startX, true, maxColor)
            drawTargetText(canvas,tagetMinYaxis,minColor)
            drawTargetText(canvas,tagetMaxYaxis,maxColor)
        }
        val startX = textPaddingLeft + maxLengedWidth + textPaddingRight
        drawCenterLine(canvas, startX)
    }

    fun setYaxisMaxMin(max: Float, min: Float) {
        maxYaxis = max
        minYaxis = min

        invalidate()
    }

    private fun drawYaxisLine(canvas: Canvas, yAxis: Float, startX: Float, isTarget: Boolean, color: Int) {
        if (isTarget) {
            setPaintStyle(PaintType.TARGET_LINE)
            paint.color = color
        } else {
            setPaintStyle(PaintType.YAXIS_LINE)
            paint.color = color
        }
        val y = getYAxis(yAxis)
        canvas.drawLine(startX, y, width.toFloat(), y, paint)
    }

    private var maxLengedWidth = 0f
    private fun drawLegendText(canvas: Canvas, yAxisValue: Float) {
        val legend = yAxisValue.toInt().toString()
        val y = getYAxis(yAxisValue)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        val dy = getTextHeight(paint)
        val dx = paint.measureText(legend)
        if (dx >= maxLengedWidth) {
            maxLengedWidth = dx
        }
        canvas.drawText(legend, dx + textPaddingLeft, y + dy, paint)
    }

    private fun drawTargetText(canvas: Canvas, yAxisValue: Float,color: Int) {
        val legend = yAxisValue.toInt().toString()
        val y = getYAxis(yAxisValue)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        paint.color = color
        val dy = y - targetPaddingBottom
        val dx = width - targetPaddingEnd
        canvas.drawText(legend, dx, dy, paint)
    }

    private fun getTextHeight(paint: Paint): Int {
        val fmi = paint.fontMetricsInt
        return (fmi.bottom - fmi.top) / 2 - fmi.bottom
    }

    private fun drawCenterLine(canvas: Canvas, startX: Float) {
        setPaintStyle(PaintType.CENTER_LINE)
        val left: Float = (width + startX) / 2
        val top = 0f
        val bottom: Float = getYAxis(minYaxis)
        val path = Path()
        path.moveTo(left, top)
        path.lineTo(left, bottom)
        canvas.drawPath(path, paint)
    }

    //获取目标值坐标
    private fun getYAxis(y: Float): Float {
        // 获取view的高度 减去所有控件的高度 得到 图表的高度 16代表预留出来的边距
        val height: Int = height - XaxisHeight - paddingTop - paddingBottom
        var top = 0f
        val scale: Float
        val deltaValue: Float
        if (y in minYaxis..maxYaxis) {
            deltaValue = maxYaxis - minYaxis
            scale = (y - minYaxis) / deltaValue
            top = (height - height * scale)
            top += paddingTop
        }
        return top
    }

    enum class PaintType {
        CENTER_LINE,
        YAXIS_LINE,
        YAXIS_LEGEND, // Y轴图例
        TARGET_LINE
    }
}
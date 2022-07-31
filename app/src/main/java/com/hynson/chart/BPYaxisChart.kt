package com.hynson.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.hynson.R
import com.hynson.chart.VesyncDateFormatUtils.dateFormatForSecondTimestamp
import com.hynson.chart.VesyncDateFormatUtils.getSundayToSaturdayOfWeek
import utils.Screen
import utils.Screen.dp2px

/**
 * Author: Hynsonhou
 * Date: 2022/7/25 14:07
 * Description: Y轴参考坐标
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/7/25   1.0       首次创建
 */
open class BPYaxisChart(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paint: Paint = Paint()

    /* Y轴图例部分 */

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
    private var textWidth = Screen.dp2px(context,30f)

    // 目标文本Padding
    private var targetPaddingEnd = 10f
    private var targetPaddingBottom = 10f

    var YLineCount = 5

    var axisXBottom = 20

    // Y轴虚线最值
    private var maxYaxis = 300f
    private var minYaxis = 0f

    // 目标最值
    private var targetMaxYaxis = 140f
    private var targetMinYaxis = 90f

    private val lineRange = ArrayList<Float>()

    init {

        val t = context.obtainStyledAttributes(attrs, R.styleable.BPYaxisChart)

        centerXColor = t.getColor(R.styleable.BPYaxisChart_centerXColor, centerXColor)
        centerXSize = t.getDimension(R.styleable.BPYaxisChart_centerXSize, 10f)
        YColor = t.getColor(R.styleable.BPYaxisChart_YColor, YColor)
        YLineSize = t.getDimension(R.styleable.BPYaxisChart_centerXSize, 10f)
        textColorY = t.getColor(R.styleable.BPYaxisChart_textColorY, textColorY)
        textSizeY = t.getDimension(R.styleable.BPYaxisChart_textSizeY, 10f)
        targetPaddingEnd = t.getDimension(R.styleable.BPYaxisChart_targetPaddingEnd, 10F)
        targetPaddingBottom = t.getDimension(R.styleable.BPYaxisChart_targetPaddingBottom, 10F)
        axisXBottom = t.getDimensionPixelOffset(R.styleable.BPYaxisChart_axisXBottom, dp2px(context,20f))
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
                    isAntiAlias = true
                }
            }
            PaintType.SYS_PAINT -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
                }
            }
            PaintType.DIA_PAINT -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_ffa94d)
                }
            }
            PaintType.SYS_LINE -> {
                paint.apply {
                    style = Paint.Style.FILL;
                    isAntiAlias = true;
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
                    alpha = 0x33
                    strokeWidth = Screen.dp2px(context, 7f).toFloat()
                }
            }
            PaintType.DIA_LINE -> {
                paint.apply {
                    style = Paint.Style.FILL;
                    isAntiAlias = true;
                    color = ContextCompat.getColor(context, R.color.color_ffa94d)
                    alpha = 0x33
                    strokeWidth = Screen.dp2px(context, 7f).toFloat()
                }
            }
            PaintType.XAXIS_RULER -> {
                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = ContextCompat.getColor(context, R.color.color_a600)
                }
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawYaxis(canvas)

    }

    private fun drawYaxis(canvas: Canvas) {
        val startX = textWidth.toFloat()
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
            lineRange.forEach {
                drawYaxisLine(canvas, it, startX, false, YColor)
            }
            drawYaxisLine(canvas, targetMinYaxis, startX, true, minColor)
            drawYaxisLine(canvas, targetMaxYaxis, startX, true, maxColor)
            drawTargetText(canvas, targetMinYaxis, minColor)
            drawTargetText(canvas, targetMaxYaxis, maxColor)
        }
        drawCenterLine(canvas, startX)
    }

    fun setYaxisMaxMin(max: Float, min: Float) {
        maxYaxis = max
        minYaxis = min

        invalidate()
    }

    fun setTargetMaxMin(max: Float, min: Float) {
        targetMaxYaxis = max
        targetMinYaxis = min

        invalidate()
    }

    private fun drawYaxisLine(
        canvas: Canvas,
        yAxis: Float,
        startX: Float,
        isTarget: Boolean,
        color: Int
    ) {
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

    val rect:Rect = Rect()
    private fun drawLegendText(canvas: Canvas, yAxisValue: Float) {
        val legend = yAxisValue.toInt().toString()
        val y = getYAxis(yAxisValue)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        paint.textAlign = Paint.Align.CENTER

        paint.getTextBounds(legend,0,legend.length,rect)
        canvas.drawText(legend, textWidth.toFloat() / 2, y + rect.height()/2, paint)
    }

    private fun drawTargetText(canvas: Canvas, yAxisValue: Float, color: Int) {
        val legend = yAxisValue.toInt().toString()
        val y = getYAxis(yAxisValue)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        paint.textAlign = Paint.Align.RIGHT
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

    //获取目标值Y轴坐标
    private fun getYAxis(y: Float): Float {
        // 获取view的高度 减去所有控件的高度 得到 图表的高度 16代表预留出来的边距
        val height: Int = height - axisXBottom - paddingTop - paddingBottom
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
        TARGET_LINE,
        SYS_PAINT,// 伸缩压点
        SYS_LINE,// 伸缩压线
        DIA_PAINT,// 舒张压点
        DIA_LINE,// 舒张压线
        XAXIS_RULER // 刻度尺
    }


}
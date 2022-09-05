package com.hynson.view.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hynson.view.R

/**
 * Author: Hynsonhou
 * Date: 2022/2/24 14:21
 * Description: 圆环比例图
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/2/24   1.0       首次创建
 * Asuraliu  2022/6/7   2.0      1.修改属性命名 2.修改宽高取值实现 3.修改间距计算方式 4.修改文本baseLine计算方法 5.增加动态设置数据文本和数据集合支持
 */
class PieChart(context: Context, attr: AttributeSet) : View(context, attr) {

    //半径
    private var radius = 20f

    //数据文字大小
    private var dataTextSize: Float = 10f

    //单位文字大小
    private var unitTextSize: Float = 10f

    //数据文字颜色
    private var dataTextColor: Int = Color.BLACK

    //单位文字颜色
    private var unitTextColor: Int = Color.GRAY

    //单位文字marginTop
    private var unitTextMarginTop = 10f

    //圆环宽度
    private var circleWidth = 10f

    //默认数据文字颜色
    private var defaultDataColor = Color.parseColor("#f1f1f1")

    // 绘制相关
    private lateinit var arcPaint: Paint
    private lateinit var dataTextPaint: Paint
    private lateinit var unitTextPaint: Paint
    private lateinit var ovalRectF: RectF
    private lateinit var boundsRect: Rect

    //数据文本
    var dataText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    //单位文本
    var unitText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    //数据集合
    var charts: List<Chart>? = null
        set(value) {
            field = value
            invalidate()
        }

    //数据文本字体
    var dataTextTypeface: Typeface? = null
        set(value) {
            field = value
            invalidate()
        }

    //单位文本字体
    var unitTextTypeface: Typeface? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        val t = context.obtainStyledAttributes(attr, R.styleable.PieChart)
        circleWidth = t.getDimension(R.styleable.PieChart_pc_circle_width, circleWidth)
        dataText = t.getString(R.styleable.PieChart_pc_text)
        unitText = t.getString(R.styleable.PieChart_pc_unit)
        dataTextSize = t.getDimension(R.styleable.PieChart_pc_text_size, dataTextSize)
        unitTextSize = t.getDimension(R.styleable.PieChart_pc_unit_size, unitTextSize)
        radius = t.getDimension(R.styleable.PieChart_pc_radius, radius)
        unitTextMarginTop = t.getDimension(R.styleable.PieChart_pc_space, unitTextMarginTop)
        dataTextColor = t.getColor(R.styleable.PieChart_pc_text_color, dataTextColor)
        unitTextColor = t.getColor(R.styleable.PieChart_pc_unit_color, unitTextColor)
        defaultDataColor = t.getColor(R.styleable.PieChart_pc_default_data_color, defaultDataColor)
        t.recycle()

        initPaint()
    }

    private fun initPaint() {
        arcPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = circleWidth
            strokeCap = Paint.Cap.ROUND
        }
        dataTextPaint = Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = dataTextSize
            color = dataTextColor
        }
        unitTextPaint = Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = unitTextSize
            color = unitTextColor
        }
        ovalRectF = RectF()
        boundsRect = Rect()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = if (specMode == MeasureSpec.EXACTLY) {
            MeasureSpec.getSize(widthMeasureSpec)
        } else {
            //直径
            (radius * 2).toInt()
        }
        //圆的宽高一致
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawArc(canvas)
        drawText(canvas)
    }

    private fun drawArc(canvas: Canvas) {
        ovalRectF.set(
            circleWidth / 2,
            circleWidth / 2,
            radius * 2 - circleWidth / 2,
            radius * 2 - circleWidth / 2
        )
        var startAngle = START
        var sumAngle = 0f
        charts?.forEach {
            sumAngle += it.value
        }
        if (sumAngle == 0f) {
            arcPaint.color = defaultDataColor
            canvas.drawArc(ovalRectF, 0f, 360f, false, arcPaint)
            return
        }
        charts?.forEach {
            val tempAngle = (it.value / sumAngle) * SUM_ANGLE
            arcPaint.color = it.color
            canvas.drawArc(ovalRectF, startAngle, tempAngle, false, arcPaint)
            startAngle += tempAngle
        }
        // 处理最后一个圆角被覆盖
        charts?.first()?.apply {
            startAngle = START
            arcPaint.color = color
            canvas.drawArc(ovalRectF, startAngle, arcPaint.strokeWidth / 2, false, arcPaint)
        }
    }

    private fun drawText(canvas: Canvas) {
        val center = width / 2
        var dataTextHeight = 0
        var dataTextBaseLineOffset = 0
        var unitTextHeight = 0
        var unitTextBaseLineOffset = 0
        //文字本身有空白间隔，需要重新计算
        var unitTextMarginHeight = unitTextMarginTop
        dataText?.let {
            dataTextPaint.apply {
                //防止文字边界超过内环边界  上面的文字大小减小 下面的文字大小也跟着减小
                while (dataTextPaint.measureText(it) > width - 2f * circleWidth) {
                    dataTextSize--
                    unitTextSize--
                    textSize = dataTextSize
                }
                strokeWidth = 0F // 注意此处一定要重新设置宽度为0,否则绘制的文字会重叠
                getTextBounds(it, 0, it.length, boundsRect)
            }
            dataTextTypeface?.let { dataTextPaint.typeface = it }
            val textMetrics = dataTextPaint.fontMetricsInt
            dataTextHeight = textMetrics.bottom - textMetrics.top
            dataTextBaseLineOffset = textMetrics.top
            unitTextMarginHeight -= textMetrics.bottom - textMetrics.descent
        }

        unitText?.let {
            unitTextPaint.apply {
                //防止下面的文字超出内环边界
                while (unitTextPaint.measureText(it) > width - 2f * circleWidth) {
                    unitTextSize--
                    textSize = unitTextSize
                }
                getTextBounds(it, 0, it.length, boundsRect)
            }
            unitTextTypeface?.let { unitTextPaint.typeface = it }
            val unitMetrics: Paint.FontMetricsInt = unitTextPaint.fontMetricsInt
            unitTextHeight = unitMetrics.bottom - unitMetrics.top
            unitTextBaseLineOffset = unitMetrics.bottom
            unitTextMarginHeight -= unitMetrics.ascent - unitMetrics.top
        } ?: run {
            unitTextMarginHeight = 0f
        }

        val totalHeight = dataTextHeight + unitTextHeight + unitTextMarginHeight
        //画图理解
        val dataTextBaseline = (height - totalHeight) / 2 - dataTextBaseLineOffset // 计算文字的基线
        val unitTextBaseline = (height - (height - totalHeight) / 2) - unitTextBaseLineOffset
        dataText?.let {
            canvas.drawText(it, center.toFloat(), dataTextBaseline, dataTextPaint)
            dataTextPaint.typeface = null
        }
        unitText?.let {
            canvas.drawText(it, center.toFloat(), unitTextBaseline, unitTextPaint)
            unitTextPaint.typeface = null
        }
    }

    companion object {
        const val START = -90f
        const val SUM_ANGLE = 360
    }
}

data class Chart(val value: Int, val color: Int)

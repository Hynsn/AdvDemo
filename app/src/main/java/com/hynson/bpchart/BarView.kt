package com.hynson.bpchart

import android.content.Context
import android.graphics.*
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
    private val ract = Rect()
    private var barWidth = 0

    private val barMinHeight = Screen.dp2px(context, 200f)
    private var centerSpace = Screen.dp2px(context, 10f)

    private var spInMax: Int = 10
    private var spInMin: Int = 20
    private var dpInMax: Int = 30
    private var dpInMin: Int = 40
    var text: CharSequence = "12"
        set(value) {
            invalidate()
            field = value
        }

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.BPBarView)

        textColor = t.getColor(R.styleable.BPBarView_bar_text_color, textColor)
        textSize = t.getDimension(R.styleable.BPBarView_bar_text_size, 10f)
        text = t.getString(R.styleable.BPBarView_bar_text).toString()

        barWidth = Screen.dp2px(getContext(), 7f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = barWidth / 2f
        val h = measuredHeight
        val cx = measuredWidth / 2f

        text?.let {
            setPaintStyle(PaintType.YAXIS_LEGEND)
            val t = it.toString()
//            paint.getTextBounds(t, 0, t.length, ract)
            val y = h - paddingBottom - centerSpace
            canvas.drawText(t, cx, y.toFloat(), paint)
        }

        // 圆点
        setPaintStyle(PaintType.SYS_PAINT)
        val spMaxY = convertViewY(spInMax, h)
        val spMinY = convertViewY(spInMin, h)

        canvas.drawCircle(cx, spMaxY, r, paint)
        canvas.drawCircle(cx, spMinY, r, paint)
        // 柱状线
        setPaintStyle(PaintType.SYS_LINE)
        canvas.drawLine(cx, spMaxY, cx, spMinY, paint)

        val dpMinY = convertViewY(dpInMin, h)
        val dpMaxY = convertViewY(dpInMax, h)
        // 棱型
        setPaintStyle(PaintType.DIA_PAINT)
        drawDiaData(canvas, cx, dpMaxY, r)
        drawDiaData(canvas, cx, dpMinY, r)
        setPaintStyle(PaintType.DIA_LINE)

        canvas.drawLine(cx, dpMaxY, cx, dpMinY, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPaintStyle(PaintType.YAXIS_LEGEND)
        val t = text.toString()
        paint.getTextBounds(t, 0, t.length, ract);
        val width = measureWidth(widthMeasureSpec, ract.width())
        val sumHeight = ract.height() + barMinHeight + centerSpace
        val height = measureHeight(heightMeasureSpec, sumHeight)
        setMeasuredDimension(width, height)
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

    private fun convertViewY(y: Int, height: Int): Float {
        var ret = 0f
        val dH = maxDyHeight - minDyHeight
        if (y >= minDyHeight && y <= maxDyHeight) {
            val scale = (y - minDyHeight) / dH
            ret = (height - height * scale)
        }
        ret -= (paddingBottom + centerSpace + barWidth / 2)
        return ret
    }

    private var maxDyHeight = 300f
    private var minDyHeight = 0f

    fun setMaxMin(max: Float, min: Float) {
        if (max < min) return
        this.maxDyHeight = max
        this.minDyHeight = min
        invalidate()
    }

    fun setData(
        spInMax: Int,
        spInMin: Int,
        dpInMax: Int,
        dpInMin: Int
    ) {
        this.spInMax = spInMax
        this.spInMin = spInMin
        this.dpInMax = dpInMax
        this.dpInMin = dpInMin

        invalidate()
    }

    var isSelect: Boolean = false
        set(value) {
            invalidate()
            field = value
        }

    private fun drawDiaData(canvas: Canvas, x: Float, y: Float, r: Float) {
        setPaintStyle(PaintType.DIA_PAINT)
        val path = Path()
        path.moveTo(x - r, y)
        path.lineTo(x, y - r)
        path.lineTo(x + r, y)
        path.lineTo(x, y + r)
        path.lineTo(x - r, y)
        canvas.drawPath(path, paint)
    }

    private fun setPaintStyle(type: PaintType) {
        paint.reset()
        when (type) {
            PaintType.YAXIS_LEGEND -> {
                paint.apply {
                    textSize = 40f
                    color = textColor
                    style = Paint.Style.FILL
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    typeface = if (isSelect) Typeface.createFromAsset(
                        context.assets,
                        "fonts/BEBAS.ttf"
                    ) else Typeface.DEFAULT
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

    enum class PaintType {
        YAXIS_LEGEND,
        SYS_PAINT,// 伸缩压点
        SYS_LINE,// 伸缩压线
        DIA_PAINT,// 舒张压点
        DIA_LINE,// 舒张压线
        XAXIS_RULER // 刻度尺
    }
}
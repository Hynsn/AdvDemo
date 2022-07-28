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

    private var spInMax: Int = 0
    private var spInMin: Int = 0
    private var dpInMax: Int = 0
    private var dpInMin: Int = 0

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.BPXaxisChart)

        textColor = t.getColor(R.styleable.BPXaxisChart_textColorX, textColor)
        textSize = t.getDimension(R.styleable.BPXaxisChart_textSizeX, 10f)

        barWidth = Screen.dp2px(getContext(), 7f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = barWidth / 2f
        val h = maxDyHeight - minDyHeight
        val cx = width / 2f

        text?.let {
            setPaintStyle(PaintType.YAXIS_LEGEND)
            val t = it.toString()
            paint.getTextBounds(t, 0, t.length, ract);
            val y = height + top
            canvas.drawText(t,cx,y.toFloat(),paint)
        }

        // 圆点
        setPaintStyle(PaintType.SYS_PAINT)
        val spMaxY = convertViewY(spInMax,h)
        val spMinY = convertViewY(spInMin,h)

        canvas.drawCircle(cx, spMaxY, r, paint)
        canvas.drawCircle(cx, spMinY, r, paint)
        // 柱状线
        setPaintStyle(PaintType.SYS_LINE)
        canvas.drawLine(cx, spMaxY, cx, spMinY, paint)

        val dpMinY = convertViewY(dpInMin,h)
        val dpMaxY = convertViewY(dpInMax,h)
        // 棱型
        setPaintStyle(PaintType.DIA_PAINT)
        drawDiaData(canvas, cx, dpMaxY, r)
        drawDiaData(canvas, cx, dpMinY, r)
        setPaintStyle(PaintType.DIA_LINE)

        canvas.drawLine(cx, dpMaxY, cx, dpMinY, paint)
    }

    private fun convertViewY(y: Int, h: Float): Float {
        var ret = 0f
        if (y >= minDyHeight && y <= maxDyHeight) {
            val scale = (y - minDyHeight) / h
            ret = (height - height * scale)
        }
        ret -= paddingBottom
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

    var isSelect:Boolean = false
        set(value) {
        invalidate()
            field = value
        }
    var text:CharSequence? = null
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
                    typeface = if(isSelect) Typeface.createFromAsset(context.assets, "fonts/BEBAS.ttf") else Typeface.DEFAULT
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
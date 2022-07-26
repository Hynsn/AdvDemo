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
class BPYaxisChart(context: Context, attrs: AttributeSet) : View(context, attrs) {

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
    private var textPaddingLeft = 10f
    private var textPaddingRight = 10f

    // 目标文本Padding
    private var targetPaddingEnd = 10f
    private var targetPaddingBottom = 10f

    var YLineCount = 5

    var XaxisHeight = 20

    // Y轴虚线最值
    private var maxYaxis = 300f
    private var minYaxis = 0f

    // 目标最值
    private var targetMaxYaxis = 140f
    private var targetMinYaxis = 90f

    private val lineRange = ArrayList<Float>()

    /* 散点图 */

    private val mIndicateRecF: RectF = RectF()
    private val mIndicateBottomPadding = 0
    private val mIndicateWidth = 0f

    //柱子数量等分屏幕后的每份宽度
    private var mInterval = 0f
    private var mHeight = 0
    private val barWidth = 30f


    /* X轴坐标 */
    private var xAxisLegentList: MutableList<String> = ArrayList<String>()

    private var mSysMaxPointList = ArrayList<Point>()
    private var mSysMinPointList = ArrayList<Point>()
    private val mDiaMaxPointList = ArrayList<Point>()
    private val mDiaMinPointList = ArrayList<Point>()
    private var mDateType = DateType.DAY

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
        targetPaddingEnd = t.getDimension(R.styleable.BPYaxisChart_targetPaddingEnd, 10F)
        targetPaddingBottom = t.getDimension(R.styleable.BPYaxisChart_targetPaddingBottom, 10F)


        mHeight = Screen.dp2px(context, 400f)
        mInterval = getInterval(getDivisorCount(DateType.DAY))
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
                    color = ContextCompat.getColor(context, R.color.color_4daaf7)
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

        val count = canvas.save()

        // 绘制横坐标，绘制柱状图
        val xAxisEnd = xAxisLegentList.size - 1
        for (position in 0..xAxisEnd) {
            if (isInVisibleArea(mSysMaxPointList[position].x)) {
                drawIndicate(canvas, position)
                if (mSysMaxPointList[position].y != 0f) { // 跳过值为0的点
                    drawChartData(canvas, position)
                }
                drawXAxisText(canvas, position, xAxisLegentList[position])
            }
        }

        canvas.restoreToCount(count)
    }

    private fun drawChartData(canvas: Canvas, position: Int) {
        Log.i(TAG,"${System.identityHashCode(this)}drawChartData: ${mSysMaxPointList[position]}")
        // 圆点
        setPaintStyle(PaintType.SYS_PAINT)
        val bottom = (height - paddingBottom).toFloat()
        canvas.drawCircle(mSysMaxPointList[position].x, mSysMaxPointList[position].y - bottom, dp2px(context, 4f).toFloat(), paint)
        canvas.drawCircle(mSysMinPointList[position].x, mSysMinPointList[position].y - bottom, dp2px(context, 4f).toFloat(), paint)
        // 柱状线
        setPaintStyle(PaintType.SYS_LINE)
        canvas.drawLine(
            mSysMaxPointList[position].x,
            mSysMaxPointList[position].y,
            mSysMinPointList[position].x,
            mSysMinPointList[position].y,
            paint
        )

        // 棱型
        setPaintStyle(PaintType.DIA_PAINT)
        drawDiaData(canvas, position, mDiaMaxPointList)
        drawDiaData(canvas, position, mDiaMinPointList)
        Log.i(TAG, "drawChartData$position,$mDiaMaxPointList,$mDiaMinPointList")
        setPaintStyle(PaintType.DIA_LINE)
        canvas.drawLine(
            mDiaMaxPointList[position].x,
            mDiaMaxPointList[position].y,
            mDiaMinPointList[position].x,
            mDiaMinPointList[position].y,
            paint
        )
    }

    private fun drawDiaData(canvas: Canvas, position: Int, mPointList: List<Point>) {
        setPaintStyle(PaintType.DIA_PAINT)
        val path = Path()
        path.moveTo(mPointList[position].x - dp2px(context, 3.5f), mPointList[position].y)
        path.lineTo(mPointList[position].x, mPointList[position].y - dp2px(context, 3.5f))
        path.lineTo(mPointList[position].x + dp2px(context, 3.5f), mPointList[position].y)
        path.lineTo(mPointList[position].x, mPointList[position].y + dp2px(context, 3.5f))
        path.lineTo(mPointList[position].x - dp2px(context, 3.5f), mPointList[position].y)
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制指示标
     */
    private fun drawIndicate(canvas: Canvas, position: Int) {
        setPaintStyle(PaintType.XAXIS_RULER)
        getIndicateLocation(mIndicateRecF, position)
        val left: Float = mIndicateRecF.left + mInterval / 2
        val right: Float = mIndicateRecF.right - mInterval / 2
        val bottom: Float = mIndicateRecF.bottom
        val top: Float = bottom - dp2px(context, 6f)
        if (this.mSelectPosition == position) {
            paint.color = ContextCompat.getColor(context, R.color.color_d900)
        }
        canvas.drawRoundRect(left, top, right, bottom, 5f, 5f, paint)
    }


    /**
     * 绘制X轴文字
     */
    private var mSelectPosition: Int = -1

    private fun drawXAxisText(canvas: Canvas, position: Int, text: String) {
        getIndicateLocation(mIndicateRecF, position)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        if (this.mSelectPosition == position) {
//            paint.textSize = mXAxisTextSelectedSize
            paint.color = ContextCompat.getColor(context, R.color.color_d900)
        }
        paint.typeface = Typeface.createFromAsset(context.assets, "fonts/BEBAS.ttf")
        val x: Float = mIndicateRecF.left + barWidth / 2 + getXAxisPadding()
        var y: Float = mIndicateRecF.bottom + mIndicateBottomPadding
//        if (true) {
//            y = mIndicateRecF.top
//            val rect = Rect()
//            rect[mIndicateRecF.left.toInt(), mIndicateRecF.top.toInt(), mIndicateRecF.right.toInt()] = mIndicateRecF.bottom.toInt()
//            paint.getTextBounds(text, 0, text.length, rect)
//            //增加一些偏移
//            y += mIndicateRecF.top / 2
//        }
        canvas.drawText(text, x, y, paint)
    }

    private fun getXAxisPadding():Float{
        return textPaddingLeft + maxLengedWidth + textPaddingRight
    }

    private fun drawYaxis(canvas: Canvas) {
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
            drawYaxisLine(canvas, targetMinYaxis, startX, true, minColor)
            drawYaxisLine(canvas, targetMaxYaxis, startX, true, maxColor)
            drawTargetText(canvas, targetMinYaxis, minColor)
            drawTargetText(canvas, targetMaxYaxis, maxColor)
        }
        val startX = textPaddingLeft + maxLengedWidth + textPaddingRight
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

    private fun drawTargetText(canvas: Canvas, yAxisValue: Float, color: Int) {
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

    //获取目标值Y轴坐标
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
        TARGET_LINE,
        SYS_PAINT,// 伸缩压点
        SYS_LINE,// 伸缩压线
        DIA_PAINT,// 舒张压点
        DIA_LINE,// 舒张压线
        XAXIS_RULER // 刻度尺
    }

    enum class DateType {
        DAY,
        WEEK,
        MONTH
    }

    data class ChartData(
        val spInMax: Int,
        val spInMin: Int,
        val dpInMax: Int,
        val dpInMin: Int,
        val maxTimestamp: Long,
        val minTimestamp: Long,
        val timeStr: String
    )

    /**
     * 计算处理数据坐标
     *
     */
    private fun convertPoint(x: Float, i: Int): Point {
        val deltaValue: Float = maxYaxis - minYaxis
        var scale: Float
        var top = 0f
        val height: Int = mHeight
        getIndicateLocation(mIndicateRecF, i)
        val left = mIndicateRecF.left + mInterval / 2
        if (x in minYaxis..maxYaxis) {
            scale = (x - minYaxis) / deltaValue
            top = (height - height * scale)
        }

        return Point(left, top)
    }

    private fun getDivisorCount(type: DateType): Int {
        return when (type) {
            DateType.DAY -> 7
            DateType.WEEK -> 5
            DateType.MONTH -> 14
        }
    }

    /**
     * 获取指示器位置
     */
    private fun getIndicateLocation(outRect: RectF, position: Int) {

        val height = height
        val indicate: Float = getIndicateWidth()
        val left = indicate * position
        val right = left + indicate
        var top = paddingTop.toFloat()
        var bottom = (height - paddingBottom).toFloat()
        bottom -= mIndicateBottomPadding
        outRect.set(left,top,right,bottom)
    }

    private fun getInterval(divisor: Int): Float {
        val width = context.resources.displayMetrics.widthPixels
        return width.toFloat() / divisor
    }

    private fun getIndicateWidth(): Float {
        return mIndicateWidth + mInterval
    }

    // 用户可见视图宽度
    private var mVisibleWidth = 0
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mVisibleWidth = w
    }

    private fun isInVisibleArea(x: Float): Boolean {
        val dx = x - scrollX
        return -(mIndicateWidth + mVisibleWidth) <= dx && dx <= mVisibleWidth + mIndicateWidth
    }

    fun setData(type: DateType, data: List<ChartData>) {
        mDateType = type
        if (xAxisLegentList.isNotEmpty()) {
            xAxisLegentList.clear()
        }
        if (mSysMaxPointList.isNotEmpty()) {
            mSysMaxPointList.clear()
        }
        if (mSysMinPointList.isNotEmpty()) {
            mSysMinPointList.clear()
        }
        if (mDiaMaxPointList.isNotEmpty()) {
            mDiaMaxPointList.clear()
        }
        if (mDiaMinPointList.isNotEmpty()) {
            mDiaMinPointList.clear()
        }
        var xAxisText = ""
        data.forEachIndexed { index, it ->
            xAxisText = when (type) {
                DateType.DAY -> {
                    dateFormatForSecondTimestamp(it.maxTimestamp, VesyncDateFormatUtils.DatePattern_M_d)
                }
                DateType.WEEK -> {
                    getSundayToSaturdayOfWeek(it.maxTimestamp * 1000)
                }
                DateType.MONTH -> {
                    dateFormatForSecondTimestamp(it.maxTimestamp, VesyncDateFormatUtils.DatePattern_M)
                }
            }
            xAxisLegentList.add(index, xAxisText)
            mSysMaxPointList.add(convertPoint(it.spInMax.toFloat(), index))
            mSysMinPointList.add(convertPoint(it.spInMin.toFloat(), index))
            mDiaMaxPointList.add(convertPoint(it.dpInMax.toFloat(), index))
            mDiaMinPointList.add(convertPoint(it.dpInMin.toFloat(), index))

        }

        invalidate()
    }

    data class Point(var x: Float, var y: Float)

    companion object {
        val TAG = BPYaxisChart::class.java.simpleName
    }
}
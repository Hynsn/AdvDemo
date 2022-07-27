package com.hynson.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import com.hynson.R
import utils.Screen

/**
 * Author: Hynsonhou
 * Date: 2022/7/27 11:46
 * Description:
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/7/27   1.0       首次创建
 */
class BPXaxisChart (context: Context, attrs: AttributeSet) : View(context, attrs){
    private var paint: Paint = Paint()

    private var textSizeY = 10f
    private var textColorY = ContextCompat.getColor(context, R.color.color_a600)

    // Y轴虚线最值
    private var maxYaxis = 300f
    private var minYaxis = 0f
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


    private var ySysMaxAxisList = ArrayList<Float>()
    private var ySysMinAxisList = ArrayList<Float>()
    private val yDiaMaxAxisList = ArrayList<Float>()
    private val yDiaMinAxisList = ArrayList<Float>()

    private var mDateType = DateType.DAY

    private var XAxisPadding = 0f
    private var minYaxisHeight = 0f

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.BPXaxisChart)

        mHeight = Screen.dp2px(context, 400f)
        textColorY = t.getColor(R.styleable.BPXaxisChart_textColorX, textColorY)
        textSizeY = t.getDimension(R.styleable.BPXaxisChart_textSizeX, 10f)

        mInterval = getInterval(getDivisorCount(DateType.DAY))

        setYaxisRefer(Screen.dp2px(context,20f).toFloat(),Screen.dp2px(context,30f).toFloat())

        initScroll()
    }

    private fun setPaintStyle(type: PaintType) {
        paint.reset()
        when (type) {
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
        updateViewPoint()

        val count = canvas.save()

        // 绘制横坐标，绘制柱状图
        val xAxisEnd = xAxisLegentList.size - 1
        for (position in 0..xAxisEnd) {
            if (isInVisibleArea(mSysMaxPointList[position].x)) {
                drawIndicate(canvas, position)
                if (mSysMaxPointList[position].y != 0f) { // 跳过值为0的点
                    drawChartData(canvas, position)
                }
                drawXAxisText(canvas, position, xAxisLegentList[position], XAxisPadding)
            }
        }

        canvas.restoreToCount(count)
    }
    private fun updateOneViewPoint(points: ArrayList<Point>, yList: List<Float>) {
        val dY = maxYaxis - minYaxis
        if (points.isEmpty()) {
            yList.forEachIndexed { index, v ->
                points.add(convertPoint(v, index, dY))
            }
        }
    }
    private fun updateViewPoint() {
        updateOneViewPoint(mSysMaxPointList, ySysMaxAxisList)
        updateOneViewPoint(mSysMinPointList, ySysMinAxisList)
        updateOneViewPoint(mDiaMaxPointList, yDiaMaxAxisList)
        updateOneViewPoint(mDiaMinPointList, yDiaMinAxisList)
    }
    private fun drawChartData(canvas: Canvas, position: Int) {
        Log.i(TAG, "${System.identityHashCode(this)}drawChartData: ${mSysMaxPointList[position]}")

        val sysMaxY = mSysMaxPointList[position].y
        val sysMinY = mSysMinPointList[position].y
        val diaMaxY = mDiaMaxPointList[position].y
        val diaMinY = mDiaMinPointList[position].y

        val sysMaxX = mSysMaxPointList[position].x
        val sysMinX = mSysMinPointList[position].x
        val diaMaxX = mDiaMaxPointList[position].x
        val diaMinX = mDiaMinPointList[position].x
        // 圆点
        setPaintStyle(PaintType.SYS_PAINT)
        canvas.drawCircle(sysMaxX, sysMaxY, Screen.dp2px(context, 4f).toFloat(), paint)
        canvas.drawCircle(sysMinX, sysMinY, Screen.dp2px(context, 4f).toFloat(), paint)
        // 柱状线
        setPaintStyle(PaintType.SYS_LINE)
        canvas.drawLine(sysMaxX, sysMaxY, sysMinX, sysMinY, paint)
        // 棱型
        setPaintStyle(PaintType.DIA_PAINT)
        drawDiaData(canvas, position, mDiaMaxPointList)
        drawDiaData(canvas, position, mDiaMinPointList)

        setPaintStyle(PaintType.DIA_LINE)
        canvas.drawLine(diaMaxX, diaMaxY, diaMinX, diaMinY, paint)
    }

    private fun drawDiaData(canvas: Canvas, position: Int, mPointList: List<Point>) {
        setPaintStyle(PaintType.DIA_PAINT)
        val path = Path()
        path.moveTo(mPointList[position].x - Screen.dp2px(context, 3.5f), mPointList[position].y)
        path.lineTo(mPointList[position].x, mPointList[position].y - Screen.dp2px(context, 3.5f))
        path.lineTo(mPointList[position].x + Screen.dp2px(context, 3.5f), mPointList[position].y)
        path.lineTo(mPointList[position].x, mPointList[position].y + Screen.dp2px(context, 3.5f))
        path.lineTo(mPointList[position].x - Screen.dp2px(context, 3.5f), mPointList[position].y)
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
        val top: Float = bottom - Screen.dp2px(context, 6f)
        if (this.mSelectPosition == position) {
            paint.color = ContextCompat.getColor(context, R.color.color_d900)
        }
        canvas.drawRoundRect(left, top, right, bottom, 5f, 5f, paint)
    }


    /**
     * 绘制X轴文字
     */
    private var mSelectPosition: Int = -1

    private fun drawXAxisText(canvas: Canvas, position: Int, text: String,xAxisPadding:Float) {
        getIndicateLocation(mIndicateRecF, position)
        setPaintStyle(PaintType.YAXIS_LEGEND)
        if (this.mSelectPosition == position) {
            //            paint.textSize = mXAxisTextSelectedSize
            paint.color = ContextCompat.getColor(context, R.color.color_d900)
        }
        paint.typeface = Typeface.createFromAsset(context.assets, "fonts/BEBAS.ttf")
        val x: Float = mIndicateRecF.left + barWidth / 2 + xAxisPadding /2
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

    /**
     * 计算处理数据坐标
     *
     */
    private fun convertPoint(x: Float, i: Int, dy: Float): Point {
        var scale: Float
        var top = 0f
        val h: Int = height
        getIndicateLocation(mIndicateRecF, i)
//        val offset = height - minYaxisHeight
        val left = mIndicateRecF.left + mInterval / 2
        if (x in minYaxis..maxYaxis) {
            scale = (x - minYaxis) / dy
            top = (h - h * scale)
            top -= minYaxisHeight
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

    enum class PaintType {
        YAXIS_LEGEND,
        SYS_PAINT,// 伸缩压点
        SYS_LINE,// 伸缩压线
        DIA_PAINT,// 舒张压点
        DIA_LINE,// 舒张压线
        XAXIS_RULER // 刻度尺
    }
    data class Point(var x: Float, var y: Float)
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
        outRect.set(left, top, right, bottom)
    }

    /**
     * 设置参照坐标
     */
    fun setYaxisRefer(a:Float,b:Float){
        XAxisPadding = a
        minYaxisHeight = b
    }
    fun setYaxisMaxMin(max: Float, min: Float) {
        maxYaxis = max
        minYaxis = min

        invalidate()
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
        if (ySysMaxAxisList.isNotEmpty()) {
            ySysMaxAxisList.clear()
        }
        if (ySysMinAxisList.isNotEmpty()) {
            ySysMinAxisList.clear()
        }
        if (yDiaMaxAxisList.isNotEmpty()) {
            yDiaMaxAxisList.clear()
        }
        if (yDiaMinAxisList.isNotEmpty()) {
            yDiaMinAxisList.clear()
        }
        var xAxisText = ""
        data.forEachIndexed { index, it ->
            xAxisText = when (type) {
                DateType.DAY -> {
                    VesyncDateFormatUtils.dateFormatForSecondTimestamp(
                        it.maxTimestamp,
                        VesyncDateFormatUtils.DatePattern_M_d
                    )
                }
                DateType.WEEK -> {
                    VesyncDateFormatUtils.getSundayToSaturdayOfWeek(it.maxTimestamp * 1000)
                }
                DateType.MONTH -> {
                    VesyncDateFormatUtils.dateFormatForSecondTimestamp(
                        it.maxTimestamp,
                        VesyncDateFormatUtils.DatePattern_M
                    )
                }
            }
            xAxisLegentList.add(index, xAxisText)

            ySysMaxAxisList.add(it.spInMax.toFloat())
            ySysMinAxisList.add(it.spInMin.toFloat())
            yDiaMaxAxisList.add(it.dpInMax.toFloat())
            yDiaMinAxisList.add(it.dpInMin.toFloat())

        }
        val size = xAxisLegentList.size
        mInnerWidth = size * getIndicateWidth() - mInterval

//        smoothScrollTo(size -1)
        invalidate()
    }

    private var overScroller : OverScroller = OverScroller(context)
    private var mVelocityTracker: VelocityTracker? = null
    private var isDragged = false
    private val mTouchSlop = 0
    private val mMinimumVelocity = 0
    private val mMaximumVelocity = 0
    private var mInnerWidth = 0f
    //中心点坐标
    private var visibleCenterValuePoint = Point(0f,0f)

    private fun initScroll(){
        overScrollMode = OVER_SCROLL_ALWAYS

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker?.addMovement(event)
        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

                recycleTracker()
            }
            MotionEvent.ACTION_CANCEL -> {

                recycleTracker()
            }
        }
        return true
    }

    private fun recycleTracker(){
        if(mVelocityTracker != null) {
            mVelocityTracker?.recycle()
            mVelocityTracker = null
        }
    }

    override fun computeScroll() {
        if(overScroller.computeScrollOffset()){
            val oldX = scrollX
            val oldY = scrollY
            val dX = overScroller.currX - oldX
            val dY = overScroller.currY - oldY

            overScrollBy(dX, dY, oldX, oldY, getMaximumScroll().toInt(), 0, width, 0,false)
            invalidate()
        }
        else if(!isDragged) {
            adjustIndicate()
        }
    }

    /**
     * 调整indicate，使其居中。
     */
    private fun adjustIndicate() {
        if (mSysMaxPointList.size <= 0) {
            return
        }
        if (!overScroller.isFinished) {
            overScroller.abortAnimation()
        }
        val position: Int = computeSelectedPosition()
        mSelectPosition = position
        var scrollX: Int = getScrollByPosition(position)
        scrollX -= getScrollX()
        if (scrollX != 0) {
            overScroller.startScroll(getScrollX(), scrollY, scrollX, 0, 10)
            invalidate()
            return
        }

        //滚动完毕回调
        //onScaleChanged(position)
        if (mSysMaxPointList.size > 0 && position < mSysMaxPointList.size) {
            visibleCenterValuePoint.x = mSysMaxPointList[position].x
            visibleCenterValuePoint.y = mSysMaxPointList[position].y
        }
    }

    /**
     * 获取position的绝对滚动位置。
     */
    private fun getScrollByPosition(position: Int): Int {
        getIndicateLocation(mIndicateRecF, position)
        return (mIndicateRecF.left + getMinimumScroll()).toInt()
    }

    private fun smoothScrollTo(position: Int) {
        val xAxisEnd = xAxisLegentList.size - 1
        if (position < 0 || position > xAxisEnd) {
            return
        }
        if (!overScroller.isFinished) {
            overScroller.abortAnimation()
        }
        val scrollX = getScrollByPosition(position)
        overScroller.startScroll(getScrollX(), scrollY, scrollX - getScrollX(), 0, 20)
        invalidate()
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        if(!overScroller.isFinished){
            val oldX = scrollX
            val oldY = scrollY
            setScrollX(scrollX)
            onScrollChanged(scrollX,scrollY,oldX,oldY)
        }
        else {
            super.scrollTo(scrollX,scrollY)
        }
    }

    /**
     * 计算当前已选择的位置
     */
    private fun computeSelectedPosition(): Int {
        var centerX = (scrollX - getMinimumScroll() + getIndicateWidth() / 2).toInt()
        centerX = Math.max(0f, Math.min(mInnerWidth, centerX.toFloat())).toInt()
        return (centerX / getIndicateWidth()).toInt()
    }

    override fun computeHorizontalScrollRange(): Int {
        return getMinimumScroll().toInt()
    }

    /**
     * 获取最小滚动值。
     */
    private fun getMinimumScroll(): Float {
        return -(width - getIndicateWidth()) / 2
    }

    /**
     * 获取最大滚动值。
     */
    private fun getMaximumScroll(): Float {
        return mInnerWidth + getMinimumScroll()
    }

    companion object {
        val TAG = BPYaxisChart::class.java.simpleName
    }
}
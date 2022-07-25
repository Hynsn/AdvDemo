package com.hynson.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.core.content.ContextCompat;

import com.hynson.R;

import java.util.ArrayList;
import java.util.List;

import utils.Screen;

public class BPChartView extends View {

    private static final String TAG = BPChartView.class.getSimpleName();
    private int mXAxisBeginRange = 0;

    private int mXAxisEndRange;

    private int mYAxisEndRange;

    private float mInnerWidth;

    /**
     * 指示标 颜色
     */
    private int mIndicateColor;

    /**
     * 指示标 宽度
     */
    private float mIndicateWidth;


    /**
     * 指示标 高度
     */
    private int mIndicateHeight;

    /**
     * 指示器 间隔底部的距离
     */
    private int mIndicateBottomPadding;

    /**
     * X轴画笔 文字
     */
    private Paint mXAxisTextPaint;

    /**
     * X轴文字 默认颜色
     */
    private int mXAxisTextColor;

    /**
     * X轴文字 默认大小
     */
    private float mXAxisTextSize;

    /**
     * X轴文字 选中大小
     */
    private float mXAxisTextSelectedSize;

    /**
     * X轴文字 间隔底部的距离
     */
    private int mXAxisTextBottomPadding;

    /**
     * 画笔 指示标
     */
    private Paint mIndicatePaint;

    private Paint mSysPaint;

    private Paint mDiaPaint;

    private Paint mSysLinePaint;

    private Paint mDiaLinePaint;

    /**
     * 底部文字和指示标的高度
     */
    private int mShadowMarginHeight;

    private int mLastMotionX;

    private boolean mIsDragged;

    private boolean mIsAutoAlign = true;

    /**
     * 滚动后选择监听
     */
    private OnScaleListener mPointListener;

    /**
     * 点击选择监听
     */
    private OnSelectListener mOnSelectListener;

    /**
     * 空视图监听
     */
    private isEmptyListener mEmptyListener;

    private int mGravity = Gravity.TOP;

    private RectF mIndicateLoc;

    /**
     * 滚动相关参数
     */
    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    /**
     * X轴坐标数据集
     */
    private List<String> xAxisValueList;

    /**
     * Y轴坐标数据集
     */
    private List<Double> ySysMaxAxisValueList = new ArrayList<>();
    private List<Double> ySysMinAxisValueList = new ArrayList<>();
    private List<Double> yDiaMaxAxisValueList = new ArrayList<>();
    private List<Double> yDiaMinAxisValueList = new ArrayList<>();

    /**
     * 坐标集合
     */
    private List<Point> mSysMaxPointList = new ArrayList<>();
    private List<Point> mSysMinPointList = new ArrayList<>();
    private List<Point> mDiaMaxPointList = new ArrayList<>();
    private List<Point> mDiaMinPointList = new ArrayList<>();

    /**
     * 目标值
     */
    private double mGoalValue;

    /**
     * Y轴最大值
     */
    private double maxYAxisValue;

    /**
     * Y轴最小值
     */
    private double minYAxisValue;

    /**
     * 当前选中的下标
     */
    private int mSelectPosition = -1;

    private Point visibleCenterValuePoint; //中心点坐标

    //上一次的数据位置
    private int lastItemPosition = -1;

    /**
     * 用户可见的视图宽
     */
    private float mViewWidth;

    /**
     * X轴的底部Y 坐标值
     */
    private float xAxisYPos;
    private BPQueryTimeType mQueryType = BPQueryTimeType.DAY; //查询的日、周、月类型
    private BPUnitType mDataType = BPUnitType.MMHG; //查询的具体数据类型
    private List<BloodPressureChartData> mChartData = new ArrayList<>();

    //柱状图属性
    private float mInterval; //柱子数量等分屏幕后的每份宽度
    private float mBarInterval; //柱子之间的间隔
    private float barWidth = 30f; //柱子的宽度
    private float paddingEdge = 32f;
    private float dayScale = 0.652f; //柱子宽度比例
    private float weekScale = 0.6986f;
    private float monthScale = 0.6986f;

    public BPChartView(Context context) {
        this(context, null);
    }

    public BPChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BPChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints(context);
    }

    /**
     * 设置属性
     */
    private void initPaints(Context context) {
        //设置X轴坐标文字正常和选中状态的颜色与大小
        mXAxisTextSize = Screen.dp2px(context, 11);
        mXAxisTextColor = ContextCompat.getColor(context, R.color.color_80ff);
        mXAxisTextSelectedSize = Screen.dp2px(context, 11);
        mXAxisTextBottomPadding = Screen.dp2px(getContext(), 1);

        //X轴刻度尺
        mIndicateHeight = Screen.dp2px(context, 6);
        mIndicateWidth = Screen.dp2px(context, 1);
        mIndicateColor = ContextCompat.getColor(context, R.color.color_d900);
        mIndicateBottomPadding = Screen.dp2px(getContext(), 20);

        //设置底部文字和指示标的高度
        mShadowMarginHeight = Screen.dp2px(getContext(), 30);

        mOverScroller = new OverScroller(getContext());
        setOverScrollMode(OVER_SCROLL_ALWAYS);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        mIndicatePaint = new Paint();
        mIndicatePaint.setStyle(Paint.Style.FILL);
        mIndicatePaint.setAntiAlias(true);

        mXAxisTextPaint = new Paint();
        mXAxisTextPaint.setStyle(Paint.Style.FILL);
        mXAxisTextPaint.setTextAlign(Paint.Align.CENTER);
        mXAxisTextPaint.setAntiAlias(true);

        mSysPaint = new Paint();
        mSysPaint.setStyle(Paint.Style.FILL);
        mSysPaint.setAntiAlias(true);
        mSysPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_4daaf7));

        mDiaPaint = new Paint();
        mDiaPaint.setStyle(Paint.Style.FILL);
        mDiaPaint.setAntiAlias(true);
        mDiaPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_ffa94d));

        final int alPhaValue = 0x33;  // 20% 透明度

        mSysLinePaint = new Paint();
        mSysLinePaint.setStyle(Paint.Style.FILL);
        mSysLinePaint.setAntiAlias(true);
        mSysLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_4daaf7));
        mSysLinePaint.setAlpha(alPhaValue);
        mSysLinePaint.setStrokeWidth(Screen.dp2px(getContext(), 7f));

        mDiaLinePaint = new Paint();
        mDiaLinePaint.setStyle(Paint.Style.FILL);
        mDiaLinePaint.setAntiAlias(true);
        mDiaLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_ffa94d));
        mDiaLinePaint.setAlpha(alPhaValue);
        mDiaLinePaint.setStrokeWidth(Screen.dp2px(getContext(), 7f));

        mIndicateLoc = new RectF();
    }

    //设置图表数据
    public void initChartData(BPQueryTimeType queryType, BPUnitType dataType,
                              List<BloodPressureChartData> chartData, boolean needToEnd) {
        mQueryType = queryType;
        mDataType = dataType;
        if (chartData == null) {
            return;
        }

        if (dataType == BPUnitType.MMHG) {
            maxYAxisValue = 300.0;
            minYAxisValue = 0.0;
        } else {
            maxYAxisValue = 40.0;
            minYAxisValue = 0.0;
        }

        mChartData.clear();
        mChartData.addAll(chartData);
        initXAxisScale(queryType);
        loadData(queryType, dataType, mChartData, needToEnd);
    }

    public void clearData() {
        if (mChartData != null) {
            mChartData.clear();
        }
    }

    //刷新图表数据
    public void refreshChartData(BPQueryTimeType queryType, BPUnitType dataType) {
        mQueryType = queryType;
        mDataType = dataType;

        initXAxisScale(queryType);

        loadData(queryType, dataType, mChartData, true);
    }

    //初始化X轴的刻度
    private void initXAxisScale(BPQueryTimeType queryType) {
        float barScale;
        switch (queryType) {
            case DAY:
                barScale = dayScale;
                break;
            case WEEK:
                barScale = weekScale;
                break;
            default:
                barScale = monthScale;
                break;
        }

        mInterval = getInterval(getDivisor(queryType), paddingEdge);
        barWidth = getInterval(getDivisor(queryType), paddingEdge) * barScale; // 根据比例得到每根柱子的宽度
        mBarInterval = getBarInterval(getDivisor(queryType), barWidth);
    }

    //根据查询类型获取柱子的数量
    private int getDivisor(BPQueryTimeType queryType) {
        int divisor = 0;
        switch (queryType) {
            case DAY:
                divisor = 7;
                break;
            case WEEK:
                divisor = 5;
                break;
            case MONTH:
            case ALL:
                divisor = 14;
                break;
            default:
                break;
        }
        return divisor;
    }

    //按柱子数量等分剩下的宽度
    private float getInterval(int divisor, float paddingEdge) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;

        return (float) (width - Screen.dp2px(
                getContext(), paddingEdge)) / divisor;
    }

    //按柱子间隔
    private float getBarInterval(int divisor, float barWidth) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        return (width - Screen.dp2px(
                getContext(), paddingEdge) - barWidth * divisor) / divisor;
    }

    private void loadData(BPQueryTimeType queryType, BPUnitType dataType,
                          List<BloodPressureChartData> chartData, boolean needToEnd) {

        yDiaMaxAxisValueList.clear();
        yDiaMinAxisValueList.clear();
        ySysMinAxisValueList.clear();
        ySysMaxAxisValueList.clear();

        //装载对应数据，对相同单位的数据进行归类
        List<String> tmpXAxisValues = new ArrayList<>();

        // 填充X轴、Y轴数据
        for (int i = 0; i < chartData.size(); i++) {
            String xAxisValue = "";
            switch (queryType) {
                case DAY:
                    xAxisValue = VesyncDateFormatUtils.INSTANCE.dateFormatForSecondTimestamp(mChartData.get(i).getMaxTimestamp(), VesyncDateFormatUtils.DatePattern_M_d);
                    break;
                case WEEK:
                    xAxisValue = VesyncDateFormatUtils.INSTANCE.getSundayToSaturdayOfWeek(mChartData.get(i).getMaxTimestamp() * 1000);
                    break;
                case MONTH:
                    xAxisValue = VesyncDateFormatUtils.INSTANCE.dateFormatForSecondTimestamp(mChartData.get(i).getMaxTimestamp(), VesyncDateFormatUtils.DatePattern_M);
                    break;
                default:
                    break;

            }
            tmpXAxisValues.add(i, xAxisValue);

            if (dataType == BPUnitType.MMHG) {
                ySysMaxAxisValueList.add(i, (double) mChartData.get(i).getSpInMmHgMax());
                ySysMinAxisValueList.add(i, (double) mChartData.get(i).getSpInMmHgMin());
                yDiaMaxAxisValueList.add(i, (double) mChartData.get(i).getDpInMmHgMax());
                yDiaMinAxisValueList.add(i, (double) mChartData.get(i).getDpInMmHgMin());
            } else if (dataType == BPUnitType.KPA) {
                ySysMaxAxisValueList.add(i, (double) mChartData.get(i).getSpInKpaMax() / 10f);
                ySysMinAxisValueList.add(i, (double) mChartData.get(i).getSpInKpaMin() / 10f);
                yDiaMaxAxisValueList.add(i, (double) mChartData.get(i).getDpInKpaMax() / 10f);
                yDiaMinAxisValueList.add(i, (double) mChartData.get(i).getDpInKpaMin() / 10f);
            }
        }

        this.xAxisValueList = tmpXAxisValues;

        minYAxisValue = 0d; //计算最小值
        if (dataType == BPUnitType.MMHG) {
            maxYAxisValue = 300d; //计算最大值
        } else if (dataType == BPUnitType.KPA) {
            maxYAxisValue = 40d; //计算最大值
        }

        mXAxisEndRange = xAxisValueList.size() - 1;
        mYAxisEndRange = ySysMaxAxisValueList.size() - 1;

        mInnerWidth = (xAxisValueList.size() - mXAxisBeginRange) * getIndicateWidth() - mInterval; //图表区域总宽度

        mSysMaxPointList = createPointList(ySysMaxAxisValueList);
        mSysMinPointList = createPointList(ySysMinAxisValueList);
        mDiaMaxPointList = createPointList(yDiaMaxAxisValueList);
        mDiaMinPointList = createPointList(yDiaMinAxisValueList);
        setEmpty(false);
        if (needToEnd) {
            smoothScrollTo(chartData.size() - 1);
        }
        invalidate();
    }


    /**
     * 获取每个数据源的坐标
     * <p>
     * 1、目标值为0，均分Y轴
     * <p>
     * 2、目标值在最低测量值和最高测量值区间，均分Y轴
     * <p>
     * 3、目标值大于最高测量值
     * <p>
     * 3.1、最大值 - 最小值/2 > 目标值 - 最大值 ；均分y轴
     * 3.2、最大值 - 最小值/2 < 目标值- 最大值 :
     * <p>
     * 目标值到最大值的间距始终只有图表的1/3
     * 最大值到最小值占据图表的2/3
     * <p>
     * 4、目标值小于最小值：
     * <p>
     * 4.1、最大值 - 最小值/2 > 最小值 - 目标值 ；均分y轴
     * 4.2、最大值 - 最小值/2 < 最小值 - 目标值 :
     * <p>
     * 目标值到最小值占据图表的1/3
     * 最大值到最小值占据图表的2/3
     */
    private List<Point> createPointList(List<Double> yAxisValueList) {

        List<Point> points = new ArrayList<>();
        double deltaValue;
        double scale;
        float top = 0;
        for (int i = 0; i < yAxisValueList.size(); i++) {
            computeIndicateLocation(mIndicateLoc, i);
            float left = mIndicateLoc.left + mInterval / 2 + 1; // 加上间隔
            // 获取view的高度 减去所有控件的高度 得到 图表的高度 16代表预留出来的边距
            int h = 300;
            int height = h - mShadowMarginHeight;
            if (yAxisValueList.get(i) >= getMinYAxisValue() && yAxisValueList.get(i) <= getMaxYAxisValue()) { //目标值在最值区间
                deltaValue = getMaxYAxisValue() - getMinYAxisValue();
                scale = (yAxisValueList.get(i) - getMinYAxisValue()) / deltaValue; // 通过每个数据除以最大的数据 得到所占比

                top = (float) (5 * height / 5 - (5 * height / 5 * scale)); // 图表高度减数据高度 得到每个数据的坐标点

                top = top + getLineChartPadding(4);
            }

            Point point = new Point();
            point.x = left;
            point.y = top;
            points.add(point);
        }

        return points;
    }


    //设置图表预留边距
    private int getLineChartPadding(int dp) {
        return Screen.dp2px(getContext(), dp);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mViewWidth = w;
    }

    /**
     * 优化加载大量数据
     * 是否在可视的范围内
     *
     * @return true：在可视的范围内；false：不在可视的范围内
     */
    private boolean isInVisibleArea(float x) {
        float dx = x - getScrollX();
        return -(mIndicateWidth + mViewWidth) <= dx && dx <= mViewWidth + mIndicateWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        refreshChartData(mQueryType,mDataType);
        if (xAxisValueList == null || mSysMaxPointList.isEmpty()) {
            return;
        }

        if (xAxisValueList.size() != mSysMaxPointList.size()) {
            return;
        }

        int count = canvas.save();

        // 绘制横坐标，绘制柱状图
        for (int position = 0; position <= mXAxisEndRange; position++) {
            if (isInVisibleArea(mSysMaxPointList.get(position).x)) {
                drawIndicate(canvas, position);
                if (mSysMaxPointList.get(position).y != 0) { // 跳过值为0的点
                    //drawBarChart(canvas, position);
                    drawChartData(canvas, position);
                }
                drawXAxisText(canvas, position, xAxisValueList.get(position));
            }
        }

        canvas.restoreToCount(count);
    }

    private void drawChartData(Canvas canvas, int position) {

        // 圆点
        canvas.drawCircle(mSysMaxPointList.get(position).x, mSysMaxPointList.get(position).y,
                Screen.dp2px(getContext(), 4f), mSysPaint);
        canvas.drawCircle(mSysMinPointList.get(position).x, mSysMinPointList.get(position).y,
                Screen.dp2px(getContext(), 4f), mSysPaint);
        // 柱状线
        canvas.drawLine(mSysMaxPointList.get(position).x, mSysMaxPointList.get(position).y,
                mSysMinPointList.get(position).x, mSysMinPointList.get(position).y, mSysLinePaint);

        // 棱型
        drawDiaData(canvas, position, mDiaMaxPointList);
        drawDiaData(canvas, position, mDiaMinPointList);
        Log.i(TAG, "drawChartData" + position + "," + mDiaMaxPointList.toString() + "," + mDiaMinPointList.toString());
        canvas.drawLine(mDiaMaxPointList.get(position).x, mDiaMaxPointList.get(position).y,
                mDiaMinPointList.get(position).x, mDiaMinPointList.get(position).y, mDiaLinePaint);
    }

    private void drawDiaData(Canvas canvas, int position, List<Point> mPointList) {

        //绘制路径
        Path path = new Path();
        //从哪个点开始绘制
        path.moveTo(mPointList.get(position).x - Screen.dp2px(getContext(), 3.5f),
                mPointList.get(position).y);
        //然后绘制到哪个点
        path.lineTo(mPointList.get(position).x,
                mPointList.get(position).y - Screen.dp2px(getContext(), 3.5f));
        //然后再绘制到哪个点
        path.lineTo(mPointList.get(position).x + Screen.dp2px(getContext(), 3.5f),
                mPointList.get(position).y);
        //然后再绘制到哪个点
        path.lineTo(mPointList.get(position).x,
                mPointList.get(position).y + Screen.dp2px(getContext(), 3.5f));
        //然后再绘制到哪个点
        path.lineTo(mPointList.get(position).x - Screen.dp2px(getContext(), 3.5f),
                mPointList.get(position).y);

        canvas.drawPath(path, mDiaPaint);
    }

    /**
     * 绘制指示标
     */
    private void drawIndicate(Canvas canvas, int position) {
        computeIndicateLocation(mIndicateLoc, position);
        float left = mIndicateLoc.left + mInterval / 2;
        float right = mIndicateLoc.right - mInterval / 2;
        float bottom = mIndicateLoc.bottom;
        float top = bottom - mIndicateHeight;
        if (this.mSelectPosition == position) {
            mIndicatePaint.setColor(mIndicateColor);
        } else {
            mIndicatePaint.setColor(mIndicateColor);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, 5, 5, mIndicatePaint);
        } else {
            canvas.drawRect(left, top, right, bottom, mIndicatePaint);
        }
    }


    /**
     * 绘制X轴文字
     */
    private void drawXAxisText(Canvas canvas, int position, String text) {
        computeIndicateLocation(mIndicateLoc, position);

        if (this.mSelectPosition == position) {
            mXAxisTextPaint.setTextSize(mXAxisTextSelectedSize);
            mXAxisTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_d900));
        } else {
            mXAxisTextPaint.setTextSize(mXAxisTextSize);
            mXAxisTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_a600));
        }
        mXAxisTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/BEBAS.ttf"));

        float x = mIndicateLoc.left + barWidth / 2 + getXAxisPadding();

        float y = mIndicateLoc.bottom + mIndicateBottomPadding - mXAxisTextBottomPadding;

        if (!isAlignTop()) {
            y = mIndicateLoc.top;
            Rect rect = new Rect();
            rect.set((int) mIndicateLoc.left, (int) mIndicateLoc.top, (int) mIndicateLoc.right,
                    (int) mIndicateLoc.bottom);
            mXAxisTextPaint.getTextBounds(text, 0, text.length(), rect);
            //增加一些偏移
            y += mIndicateLoc.top / 2;
        }
        xAxisYPos = y;

        canvas.drawText(text, x, y, mXAxisTextPaint);
    }


    /**
     * 计算indicate的位置
     */
    private void computeIndicateLocation(RectF outRect, int position) {
        if (outRect == null) {
            return;
        }

        int height = getHeight();
        float indicate = getIndicateWidth();
        float left = (indicate * position);
        float right = left + indicate;
        int top = getPaddingTop();
        int bottom = height - getPaddingBottom();

        if (isAlignTop()) {
            bottom -= mIndicateBottomPadding;
        } else {
            top += mIndicateBottomPadding;
        }

        outRect.set(left, top, right, bottom);
    }

    // 纠正柱子的偏移量，使柱子中间位于中线位置
    private float getXAxisPadding() {
        if (BPQueryTimeType.MONTH.equals(mQueryType)) {
            return Screen.dp2px(getContext(), paddingEdge / 5.5f);
        } else {
            return Screen.dp2px(getContext(), paddingEdge / 3);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();

        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDragged = !mOverScroller.isFinished();
                if (mIsDragged) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }

                mLastMotionX = (int) event.getX();

                return true;

            case MotionEvent.ACTION_MOVE:

                if (mChartData != null && mChartData.size() == 1) {
                    return true;
                }

                int curX = (int) event.getX();
                int deltaX = mLastMotionX - curX;

                if (!mIsDragged && Math.abs(deltaX) > mTouchSlop) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    mIsDragged = true;

                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }

                if (mIsDragged) {

                    Log.i(TAG, deltaX > 0 ? "deltaX > 0 ====> 向左滑动" : "deltaX < 0 ====> 向右滑动");

                    mLastMotionX = curX;

                    int maxOverScrollX; //设置拖拽的最大滚动距离,滑到最左侧的时候，最大的滚动距离设置为getWidth()/2

                    if (getScrollX() <= getMinimumScroll()) { //滑动到最左侧
                        deltaX *= 0.7;

                        if (deltaX <= 0) { //向右滑动
                            maxOverScrollX = (int) -getMinimumScroll(); //设置为负数 不可滚动
                        } else { //向左滑动
                            maxOverScrollX = getWidth();
                        }
                    } else if (getScrollX() >= getMaximumScroll()) { //滑动到最右侧
                        deltaX *= 0.7;
                        if (deltaX <= 0) { //向右滑动
                            maxOverScrollX = getWidth();
                        } else { //向左滑动
                            maxOverScrollX = 0; //设置为0 不可滚动
                        }
                    } else {
                        maxOverScrollX = getWidth();
                    }

                    if (overScrollBy(deltaX, 0, getScrollX(), getScrollY(), (int) getMaximumScroll(), 0, maxOverScrollX, 0, true)) {
                        mVelocityTracker.clear();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                if (mChartData != null && mChartData.size() == 1) { //只有一个点的时候，不可滚动与滑动
                    return true;
                }

                if (mIsDragged) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) mVelocityTracker.getXVelocity();

                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        fling(-initialVelocity);
                    } else {
                        sprintBack();
                    }
                }

                //点击时候进行滚动
                if (!mIsDragged) {

                    if (visibleCenterValuePoint != null && mSysMaxPointList.size() > 0
                            && visibleCenterValuePoint.x == mSysMaxPointList.get(0).x) { //边缘值不可点击
                        if (mLastMotionX < getWidth() / 2) {
                            return true;
                        }
                    } else if (visibleCenterValuePoint != null && mSysMaxPointList.size() > 0
                            && visibleCenterValuePoint.x == mSysMaxPointList.get(
                            mSysMaxPointList.size() - 1).x) {
                        if (mLastMotionX > getWidth() / 2) {
                            return true;
                        }
                    }

                    int touchX = (int) event.getX();
                    int deltaTouch;
                    if (touchX > getWidth() / 2) {
                        deltaTouch = (touchX - getWidth() / 2);
                    } else {
                        deltaTouch = -(getWidth() / 2 - touchX);
                    }

                    mOverScroller.startScroll(getScrollX(), getScrollY(), deltaTouch, 0,
                            40); //持续时间40ms
                    invalidateView();
                }

                mIsDragged = false;
                recycleVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:

                if (mChartData != null && mChartData.size() == 1) {
                    return true;
                }

                if (mIsDragged && mOverScroller.isFinished()) {
                    sprintBack();
                }

                mIsDragged = false;

                recycleVelocityTracker();
                break;

            default:
                break;
        }

        return true;
    }

    private void refreshValues() {
        mInnerWidth = (mXAxisEndRange - mXAxisBeginRange) * getIndicateWidth();
        invalidateView();
    }

    private float getIndicateWidth() {
        return mIndicateWidth + mInterval;
    }

    /**
     * 获取最小滚动值。
     */
    private float getMinimumScroll() {
        return -(getWidth() - getIndicateWidth()) / 2;
    }

    /**
     * 获取最大滚动值。
     */
    private float getMaximumScroll() {
        return mInnerWidth + getMinimumScroll();
    }

    /**
     * 调整indicate，使其居中。
     */
    private void adjustIndicate() {
        if (mSysMaxPointList != null && mSysMaxPointList.size() <= 0) {
            return;
        }

        if (!mOverScroller.isFinished()) {
            mOverScroller.abortAnimation();
        }

        int position = computeSelectedPosition();
        this.mSelectPosition = position;

        int scrollX = getScrollByPosition(position);
        scrollX -= getScrollX();

        if (scrollX != 0) {
            mOverScroller.startScroll(getScrollX(), getScrollY(), scrollX, 0, 10);
            invalidateView();
            return;
        }

        //滚动完毕回调
        onScaleChanged(position);
        if (mSysMaxPointList != null && mSysMaxPointList.size() > 0 && position < mSysMaxPointList.size()) {
            visibleCenterValuePoint = mSysMaxPointList.get(position); // 给中心点坐标赋值
        }
    }

    //设置滑翔的最大滚动距离 overX 当值为 getWidth()/2 屏幕的一半时，会滑翔很远
    public void fling(int velocityX) {
        int overX = 0;
        mOverScroller.fling(getScrollX(), getScrollY(), velocityX, 0, (int) getMinimumScroll(),
                (int) getMaximumScroll(), 0, 0, overX, 0);
        invalidateView();
    }

    public void sprintBack() {
        mOverScroller.springBack(getScrollX(), getScrollY(), (int) getMinimumScroll(),
                (int) getMaximumScroll(), 0, 0);
        invalidateView();
    }

    public void setOnScaleListener(OnScaleListener listener) {
        if (listener != null) {
            mPointListener = listener;
        }
    }

    public void setOnSelectListener(OnSelectListener listener) {
        if (listener != null) {
            mOnSelectListener = listener;
        }
    }

    public void setEmptyListener(isEmptyListener listener) {
        if (listener != null) {
            mEmptyListener = listener;
        }
    }

    /**
     * 获取position的绝对滚动位置。
     */
    private int getScrollByPosition(int position) {
        computeIndicateLocation(mIndicateLoc, position);
        int scrollX = (int) (mIndicateLoc.left + getMinimumScroll());
        return scrollX;
    }

    /**
     * 计算当前已选择的位置
     */
    public int computeSelectedPosition() {
        int centerX = (int) (getScrollX() - getMinimumScroll() + getIndicateWidth() / 2);
        centerX = (int) Math.max(0, Math.min(mInnerWidth, centerX));
        int position = (int) (centerX / getIndicateWidth());
        return position;
    }

    public void smoothScrollTo(int position) {
        if (position < 0 || mXAxisBeginRange + position > mXAxisEndRange) {
            return;
        }

        if (!mOverScroller.isFinished()) {
            mOverScroller.abortAnimation();
        }

        int scrollX = getScrollByPosition(position);
        mOverScroller.startScroll(getScrollX(), getScrollY(), scrollX - getScrollX(), 0, 20);
        invalidateView();
    }

    public void smoothScrollToValue(int value) {
        int position = value - mXAxisBeginRange;
        smoothScrollTo(position);
    }

    /**
     * 滚动回调
     */
    private void onScaleChanged(int position) {
        if (mPointListener != null && ySysMaxAxisValueList != null && position < ySysMaxAxisValueList
                .size()) {
            mPointListener.onScaleChanged(position, ySysMaxAxisValueList.get(position), mChartData.get(position));
            lastItemPosition = position;
        }
    }

    private void setSelectListener(int listSize, int position, Point selectPoint) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelectListener(listSize, position, selectPoint);
        }
    }

    /**
     * 空视图
     */
    private void setEmpty(boolean isEmpty) {
        if (mEmptyListener != null) {
            mEmptyListener.isEmptyView(isEmpty);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mOverScroller.isFinished()) {
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            setScrollX(scrollX);
            onScrollChanged(scrollX, scrollY, oldX, oldY);
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    private boolean isAlignTop() {
        return (mGravity & Gravity.TOP) == Gravity.TOP;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
        invalidateView();
    }

    @Override
    public void computeScroll() {

        if (this.mOverScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mOverScroller.getCurrX();
            int y = this.mOverScroller.getCurrY();
            overScrollBy(x - oldX, y - oldY, oldX, oldY, (int) getMaximumScroll(), 0, getWidth(), 0,
                    false);
            invalidateView();
        } else if (!mIsDragged && mIsAutoAlign) {
            adjustIndicate();
        }
    }

    @Override
    protected int computeHorizontalScrollRange() {
        return (int) getMaximumScroll();
    }

    public void invalidateView() {
        postInvalidate();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public interface OnScaleListener {
        void onScaleChanged(int position, double yAxisValue, BloodPressureChartData data);
    }

    public interface OnSelectListener {
        void onSelectListener(int listSize, int position, Point selectPoint);
    }

    public interface isEmptyListener {
        void isEmptyView(boolean isEmpty);
    }

    class Point {

        public float x;
        public float y;

        Point() {

        }

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"x\":")
                    .append(x);
            sb.append(",\"y\":")
                    .append(y);
            sb.append('}');
            return sb.toString();
        }
    }

    public double getMaxYAxisValue() {
        return maxYAxisValue;
    }

    public double getMinYAxisValue() {
        return minYAxisValue;
    }

    public int getShadowMarginHeight() {
        return mShadowMarginHeight;
    }

    public double getGoalValue() {
        return mGoalValue;
    }

    public void setGoalValue(double goalValue) {
        this.mGoalValue = goalValue;
    }

    public List<String> getxAxisValueList() {
        return xAxisValueList;
    }

    public BPUnitType getDataType() {
        return mDataType;
    }

    public void setDataType(BPUnitType mDataType) {
        this.mDataType = mDataType;
    }

    public List<BloodPressureChartData> getChartData() {
        return mChartData;
    }
}


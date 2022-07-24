package com.hynson.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.hynson.R;

import utils.Screen;

/**
 * 趋势图MarkView
 */
public class BPChartMarkView extends View {

    private static final String TAG = BPChartMarkView.class.getSimpleName();
    /**
     * 画笔 线
     */
    private Paint mLinePaint;

    /**
     * 目标线颜色
     */
    private int mGoalLineColor;

    /**
     * MarkView线 宽度
     */
    private int mLineWidth;

    /**
     * 画笔 文字
     */
    private Paint mTextPaint;

    /**
     * 目标线、目标值画笔
     */
    private Paint mGoalLinePaint;

    /**
     * 文字 默认颜色
     */
    private int mTextColor;

    /**
     * 文字 默认大小
     */
    private float mTextSize = 14;


    /**
     * 图表X轴高度
     */
    private int mXAxisHeight;


    /**
     * 图表数据类型
     */
    private BPUnitType dataType;

    /**
     * Y轴最大值
     */
    private double maxYAxisValue;

    /**
     * Y轴最小值
     */
    private double minYAxisValue;

    /**
     * 当前选中的值
     */
    private double selectYAxisValueMax;

    private boolean showMiddleLine = true;


    public BPChartMarkView(Context context) {
        this(context, null);
    }

    public BPChartMarkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BPChartMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化一些参数
        mLineWidth = Screen.dp2px(context,1);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineWidth);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.BPChartMarkView);
        int color = ContextCompat.getColor(context,R.color.color_73);
        mTextColor = t.getColor(R.styleable.BPChartMarkView_textColor,color);
        mTextSize = t.getDimensionPixelSize(R.styleable.BPChartMarkView_textSize,14);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        mGoalLinePaint = new Paint();
        mGoalLinePaint.setStyle(Paint.Style.FILL);
        mGoalLinePaint.setTextAlign(Paint.Align.CENTER);
        mGoalLinePaint.setAntiAlias(true);

        mGoalLineColor = Color.parseColor("#ccffffff");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showMiddleLine) {
            //画中线
            drawMiddleLine(canvas);
        }

        //绘制目标线 目标值
        if (dataType == BPUnitType.MMHG) {
            drawGoalLine(canvas, 0);
            drawGoalLine(canvas, 100);
            drawGoalLine(canvas, 200);
            drawGoalLine(canvas, 300);
        } else {
            drawGoalLine(canvas, 0);
            drawGoalLine(canvas, 10);
            drawGoalLine(canvas, 20);
            drawGoalLine(canvas, 30);
            drawGoalLine(canvas, 40);
        }
    }

    /**
     * @param canvas
     * @des 绘制目标值
     */
    private void drawGoalValueText(Canvas canvas, double yAxisValue) {
        mGoalLinePaint.setTextSize(mTextSize);
        mGoalLinePaint.setColor(mTextColor);
        mGoalLinePaint.setTextAlign(Paint.Align.RIGHT);
        StringBuilder stringBuilder = new StringBuilder();
        String goalValueStr;

        goalValueStr = stringBuilder.append((int) yAxisValue).toString();
        float  y = getYAxis(yAxisValue);

        canvas.drawText(goalValueStr, getLineChartPadding(25), y + getLineChartPadding(5), mGoalLinePaint);
    }

    /**
     * 绘制虚线：目标线
     *
     * @param canvas
     * @des 加上偏移量  ChartViewUtil.getDp2int(this.getContext(), 50) 是因为MarkView的高度比ChartView高50dp  再减去12dp , 因为ChartView paddingBottom="12dp"
     */
    private void drawGoalLine(Canvas canvas, double yAxisValue) {
        Paint paint = new Paint();
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mLineWidth);
        paint.setColor(getGoalLineColor());
        paint.setAntiAlias(true);
        float  y = getYAxis(yAxisValue);

        canvas.drawLine(getLineChartPadding(32), y, getWidth(), y, paint);
        drawGoalValueText(canvas, yAxisValue);
    }

    //获取目标值坐标
    private float getYAxis(double yAxisValue) {
        // 获取view的高度 减去所有控件的高度 得到 图表的高度 16代表预留出来的边距
        int height = getHeight() - getXAxisHeight() - getLineChartPadding(16);
        float top = 0;
        double scale;
        double deltaValue;

        if (yAxisValue >= getMinYAxisValue() && yAxisValue <= getMaxYAxisValue()) { //目标值在最值区间
            deltaValue = getMaxYAxisValue() - getMinYAxisValue();
            scale = (yAxisValue - getMinYAxisValue()) / deltaValue;

            top = (float) (5 * height / 5 - (5 * height / 5 * scale));

            top = top + getLineChartPadding(4);
        }

        return top;
    }

    //设置图表预留边距
    private int getLineChartPadding(int dp) {
        return Screen.dp2px(getContext(),dp);
    }


    private void drawMiddleLine(Canvas canvas) {
        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 10}, 1);
        mLinePaint.setPathEffect(pathEffect);
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_4daaf7));
        int left = getWidth() / 2 + getLineChartPadding(14);
        int top = 0;
        float bottom = getYAxis(0);
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        canvas.drawPath(path, mLinePaint);
    }

    public int getGoalLineColor() {
        return mGoalLineColor;
    }

    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }


    public int getXAxisHeight() {
        return mXAxisHeight;
    }

    public void setXAxisHeight(int xAxisHeight) {
        mXAxisHeight = xAxisHeight;
    }


    public BPUnitType getDataType() {
        return dataType;
    }

    public void setDataType(BPUnitType dataType) {
        this.dataType = dataType;
    }

    public void setMaxYAxisAndMinYAxis(double max, double min) {
        this.maxYAxisValue = max;
        this.minYAxisValue = min;
        invalidate();
    }

    public double getMaxYAxisValue() {
        return maxYAxisValue;
    }

    public void setMaxYAxisValue(double maxYAxisValue) {
        this.maxYAxisValue = maxYAxisValue;
    }

    public double getMinYAxisValue() {
        return minYAxisValue;
    }

    public void setMinYAxisValue(double minYAxisValue) {
        this.minYAxisValue = minYAxisValue;
    }

    public double getSelectYAxisValueMax() {
        return selectYAxisValueMax;
    }

    public void setSelectYAxisValueMax(double selectYAxisValueMax) {
        this.selectYAxisValueMax = selectYAxisValueMax;
        invalidate();
    }

    public void showMiddleLine(boolean isShow) {
        this.showMiddleLine = isShow;
    }
}


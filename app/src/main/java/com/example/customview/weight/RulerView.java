package com.example.customview.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Author: Hynsonhou
 * Date: 2022/3/25 15:38
 * Description: 滑动刻度尺
 * 参考：https://github.com/WangcWj/AndroidScrollRuler
 * https://juejin.cn/post/6844903811723558920
 * https://github.com/811xun/ScrollRulerView-master
 * 1.遇到的问题是怎么让他滚动?
 * 2.滚动的时候绘制的内容不会错乱掉?
 * 3.怎么判断滑动的边界?
 * 4.怎么让它Fling?
 * 5.fling之后怎么去判断中间坐标的位置?
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/3/25   1.0       首次创建
 */
public class RulerView extends View {
    private boolean isInCanScrollView = true;

    private final String TAG = RulerView.class.getSimpleName();

    // 负责平缓滑动
    private Scroller mScroller;


    // 滑动速度跟踪器
    private VelocityTracker mTracker;

    private Paint mPaint;

    // 刻度间距
    private int mRulerSpace = 50;
    // 刻度半径
    private float mRulerRadius = 5f;
    private float mRulerLineSize = 2f;

    private int mRulerColor = Color.BLUE;
    private int mTextColor = Color.BLACK;
    private float mTextSize = 20f;
    private int mTextRulerSpace = 12;

    // 总长度
    private int mCountWidth;
    // 除去当前屏幕之外的剩余的px长度.
    private int mCountRange = -1;

    // 过滤标志，避免频繁调用
    private float mPreDistance = -1;

    /**
     * 说明现在的操作是手指抬起之后.
     */
    private boolean mPressUp = false;

    /**
     * 表示目前处于fling的滑动中.
     */
    private boolean isFling = false;

    // Fling（惯性滑动）最小速度
    private int mMinVelocity;

    // 触摸初始X坐标
    private float startX;


    public RulerView(Context context) {
        super(context);
        init(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray arry = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
            mRulerSpace = arry.getDimensionPixelOffset(R.styleable.RulerView_ruler_space, 50);
            mRulerRadius = arry.getDimension(R.styleable.RulerView_ruler_radius, 5f);
            mRulerLineSize = arry.getDimension(R.styleable.RulerView_ruler_line_size, 2f);
            mTextColor = arry.getColor(R.styleable.RulerView_text_color, Color.BLACK);
            mTextSize = arry.getDimension(R.styleable.RulerView_text_size, 14f);
            mTextRulerSpace = arry.getDimensionPixelOffset(R.styleable.RulerView_text_ruler_space, 12);
            mRulerColor = arry.getColor(R.styleable.RulerView_ruler_color, Color.BLUE);
            arry.recycle();
        } else {
            mTextSize = 14f;
            mRulerRadius = 5f;
            mRulerLineSize = 2f;
        }
        mTexts = new ArrayList<>();
        mPoints = new ArrayList<>();
        mValues = new ArrayList<>();
        mPaint = new Paint();
        setLinePaint(PaintType.CIRCLE);

        mScroller = new Scroller(getContext());

        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        mMinVelocity = viewConfig.getScaledMinimumFlingVelocity();
    }

    private void setLinePaint(PaintType type) {
        switch (type) {
            case CIRCLE: // 圆点
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(mRulerRadius);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mRulerColor);
                break;
            case CENTER_POINT: // 圆点
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(mRulerRadius);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.RED);
                break;
            case RING: // 圆环
                mPaint.setStrokeWidth(mRulerLineSize * 2);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.WHITE);
                break;
            case LINE: // 线
                mPaint.setStrokeWidth(mRulerLineSize);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mRulerColor);
                break;
            case TEXT: // 文本
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextSize(mTextSize);
                mPaint.setColor(mTextColor);
                mPaint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    /**
     * 设置刻度尺的范围, 比如: 100 - 2000
     *
     * @param start
     * @param end
     */
    public void setScope(int start, int end, int step,RulerType type) {
        if(step == 0) {
            return;
        }
        else {
            this.mStep = step;
            mRulerCounter = (end - start) / (this.mStep);

            for (int i = start; i <= end; i += this.mStep) {
                mValues.add(i);
                if (type == RulerType.TIME) {
                    int hr = i / 60;
                    int min = i % 60;
                    mTexts.add(String.format(Locale.US,"%02d:%02d", hr,min));
                } else {
                    float div = 1f / RULE_SIZE;
                    float value = i * div / step;
                    if((value - (int)value) == 0){
                        mTexts.add(String.valueOf((int)value));
                    }
                    else {
                        mTexts.add(String.valueOf(value));
                    }
                }
            }

            mCountWidth = mRulerCounter * mRulerSpace;// + rulerSize * mLineWidth;
            invalidate();
        }
    }

    private int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = (int) (mRulerRadius + mTextRulerSpace + mTextSize) *2;
        setMeasuredDimension(widthSize, measureSize(heightMode,heightSize,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRulerCounter > 0) {
            if (mCountRange == -1) {
                mCountRange = mCountWidth - getWidth();
                mCenterPointX = getWidth() / 2;
            }
            float diameter = mRulerRadius * 2;
            drawLine(canvas,diameter);
            drawRuler(canvas,diameter);
        }
    }

    /**
     * 画线条
     * @param canvas
     * @param diameter
     */
    private void drawLine(Canvas canvas,float diameter){
        float lineStart = getLeft();
        float lineEnd = getRight();
        float centerY = getHeight() * 0.5f;

        for (int index = 0; index <= mRulerCounter; index++) {
            float lineCount = diameter * index;
            float left = index * mRulerSpace + lineCount - mRulerRadius;
            float right = left + diameter;

            float centerX = (left + right) * 0.5f;

            if (index == 0) {
                lineStart = centerX;
            } else if (index == mRulerCounter) {
                lineEnd = centerX;
            }
        }
        setLinePaint(PaintType.LINE);
        canvas.drawLine(lineStart, centerY, lineEnd, centerY, mPaint);
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawRuler(Canvas canvas,float diameter) {
        float centerY = getHeight() * 0.5f;

        for (int index = 0; index <= mRulerCounter; index++) {
            float lineCount = diameter * index;
            float left = index * mRulerSpace + lineCount - mRulerRadius;
            float right = left + diameter;

            float centerX = (left + right) * 0.5f - mRulerRadius;

            if (!isFull()) {
                addPoint((int) left);
            }

            if (!isCenterPoint(index)) {
                String text = getTextByIndex(index);
                setLinePaint(PaintType.TEXT);
                canvas.drawText(text, centerX, centerY + mPaint.getTextSize() + mTextRulerSpace, mPaint);

                setLinePaint(PaintType.CIRCLE);
                canvas.drawCircle(centerX, centerY, mRulerRadius, mPaint);
                setLinePaint(PaintType.RING);
                canvas.drawCircle(centerX, centerY, mRulerRadius + mRulerLineSize, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInCanScrollView && null != getParent()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mScroller.forceFinished(true);
                mPressUp = false;
                isFling = false;
                startX = event.getX();
                mTracker.clear();
                break;
            case MotionEvent.ACTION_MOVE:
                mPressUp = false;
                float distance = event.getX() - startX;
                if (mPreDistance != distance) {
                    doScroll((int) -distance, 0, 0);
                }
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isInCanScrollView && null != getParent()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mPressUp = true;
                mTracker.computeCurrentVelocity(1000);
                float xVelocity = mTracker.getXVelocity();
                if (Math.abs(xVelocity) >= mMinVelocity) {
                    isFling = true;
                    int finalX = mScroller.getCurrX();
                    int velocityX = (int) (xVelocity * 0.35);
                    int maxX = (int) (mCountRange + mCenterPointX + mRulerRadius * mRulerCounter * 2);

                    mScroller.fling(finalX, 0, -velocityX, 0, -mCenterPointX, maxX, 0, 0);
                    invalidate();
                } else {
                    isFling = false;
                    scrollFinish();
                }

                mTracker.recycle();
                mTracker = null;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 滑动停止之后重新定位
     */
    public void scrollFinish() {
        int finalX = mScroller.getFinalX();
        int centerPointX = mCenterPointX;
        int currentX = centerPointX + finalX;
        int scrollDistance = getScrollDistance(currentX);
        if (0 != scrollDistance) {
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), -scrollDistance, 0, 300);
            invalidate();
            if (scrollSelected != null) {
                scrollSelected.selected(getCurrentText(),mCurrentValue);
            }
        }
    }

    private void doScroll(int dx, int dy, int duration) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX() && mPressUp && isFling) {
                mPressUp = false;
                isFling = false;
                scrollFinish();
            }
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        destroy();
    }

    private void destroy() {
        mScroller = null;
        scrollSelected = null;
        mPoints.clear();
        mTexts.clear();
        mValues.clear();

        /*mPoints = null;
        mTexts = null;
        mValues = null;*/
    }

    // 画笔类型
    enum PaintType {
        CIRCLE,
        RING,
        LINE,
        TEXT,
        CENTER_POINT
    }
    // 刻度含义，比如距离、时间
    public enum RulerType {
        DISTANCE,
        TIME
    }

    // 滑动到刻度监听
    private ScrollSelected scrollSelected;

    public void setScrollSelected(ScrollSelected scrollSelected) {
        this.scrollSelected = scrollSelected;
    }

    public interface ScrollSelected {
        void selected(String selected,int value);
    }

    // 几个刻度
    private final int RULE_SIZE = 2;

    private int mCurrentLine = -1;
    private int mStep = 500;
    private int mRulerCounter;
    private int mCurrentValue;

    private List<Integer> mPoints;
    private List<Integer> mValues;
    private List<String> mTexts;
    private int mCenterPointX;
    private String mCurrentText;

    public boolean isCenterPoint(int index) {
        int lineIndex = index / RULE_SIZE;
        if (mCurrentLine != lineIndex) {
            mCurrentLine = lineIndex;
            return false;
        }
        return true;
    }

    public String getTextByIndex(int index) {
        if (index < 0 || index >= mTexts.size()) {
            return "";
        }
        return mTexts.get(index);
    }

    private void setCurrentText(int index) {
        if (index >= 0 && index < mTexts.size()) {
            this.mCurrentText = mTexts.get(index);
        }
    }

    public void setCurrentValue(int value){
        mCurrentValue = value;
    }

    public String getCurrentText() {
        return mCurrentText;
    }

    private int getScrollDistance(int x) {
        for (int i = 0; i < mPoints.size(); i++) {
            int pointX = mPoints.get(i);
            if (0 == i && x < pointX) {
                //当前的x比第一个位置的x坐标都小 也就是需要往右移动到第一个长线的位置.
                setCurrentText(0);
                return x - pointX;
            } else if (i == mPoints.size() - 1 && x > pointX) {
                //当前的x比最后一个左边的x都大,也就是需要往左移动到最后一个长线位置.
                setCurrentText(mTexts.size() - 1);
                return x - pointX;
            } else {
                if (i + 1 < mPoints.size()) {
                    int nextX = mPoints.get(i + 1);
                    if (x > pointX && x <= nextX) {
                        int distance = (nextX - pointX) / 2;
                        int dis = x - pointX;
                        if (dis > distance) {
                            //说明往下一个移动
                            setCurrentText(i + 1);
                            return x - nextX;
                        } else {
                            setCurrentText(i);
                            //往前一个移动
                            return x - pointX;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private void addPoint(int x) {
        mPoints.add(x);
        if (mPoints.size() == mTexts.size()) {
            int index = mValues.indexOf(mCurrentValue);
            if (index < 0) {
                return;
            }
            int currentX = mPoints.get(index);
            if (currentX < mPoints.get(0)) {
                return;
            }
            doScroll(-(mCenterPointX - currentX), 0, 300);
            if (scrollSelected != null) {
                scrollSelected.selected(mTexts.get(index),mCurrentValue);
            }
        }
    }

    private boolean isFull() {
        return mTexts.size() == mPoints.size();
    }
}

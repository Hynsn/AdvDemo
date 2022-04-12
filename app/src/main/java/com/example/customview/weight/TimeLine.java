package com.example.customview.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.R;

import java.util.List;

public class TimeLine extends View {
    private int cookStepSum = 10;//圆的总数
    private int preCookCounter = 3;

    private int currentPos = 0;//实心圆数

    private Paint cPointPT,unpasedPT,pasedPT;

    private int radius = 12;

    private int defaultPreCookWidth = 40;

    private List<Integer> lineIntervals;

    public TimeLine(Context context) {
        super(context);
        initPoint();
    }

    public TimeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPoint();
    }

    public TimeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPoint();
    }

    private int sumSeconds = 1;

    public void setStep(int preCookCounter,List<Integer> stepSeconds){
        this.cookStepSum = stepSeconds.size();
        this.preCookCounter = preCookCounter;

        sumSeconds = 0;
        for (int i = preCookCounter; i < cookStepSum; i++) {
            sumSeconds += stepSeconds.get(i);
        }
        Log.i(TAG, "sumSeconds: "+sumSeconds);

        this.lineIntervals = stepSeconds;

        invalidate();
    }

    int dartGreen = Color.parseColor("#2DA6A2");
    int gray = Color.parseColor("#999999");

    private void initPoint(){
        cPointPT = new Paint();
        cPointPT.setStrokeWidth(8);

        unpasedPT = new Paint();
        unpasedPT.setStrokeWidth(8);
        unpasedPT.setStyle(Paint.Style.FILL);

        pasedPT = new Paint();
        pasedPT.setStrokeWidth(8);
        pasedPT.setStyle(Paint.Style.FILL);
        pasedPT.setColor(dartGreen);
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        if (currentPos < 0) {
            this.currentPos = 0;
        }

        if (currentPos > cookStepSum) {
            this.currentPos = cookStepSum;
        }
        invalidate();
    }

    /**
     * 播放图标
     */
    private BitmapDrawable playDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.timeline_play);
    /**
     * 播放图标宽度
     */
    private int playDrawableWidth = playDrawable.getIntrinsicWidth();
    /**
     * 播放图标高度
     */
    private int playDrawableHeight = playDrawable.getIntrinsicHeight();

    final static String TAG = TimeLine.class.getSimpleName();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int circlesWidth = (int) (cPointPT.getStrokeWidth()*4); // 原点总宽度

        int centerY = (int) getY() + playDrawableHeight;
        int preCookWidth = 0;
        int circleInterval = 0;
        if(preCookCounter > 0) {
            int defaultWidth = (int) (width * 0.4);
            preCookWidth = Math.min(defaultPreCookWidth * preCookCounter, defaultWidth);
            circleInterval =  preCookWidth / preCookCounter;
        }
        int cookWidth = width - circlesWidth - preCookWidth;

        int average = cookWidth / sumSeconds;

        Log.i(TAG, "average: "+average+","+cookWidth+","+sumSeconds);

        int lineInterval = cookWidth / (cookStepSum -1-preCookCounter);

        int circleStart = (int) cPointPT.getStrokeWidth() *2;
        //画线
        int lineX = (int) cPointPT.getStrokeWidth() *2+preCookWidth;
        int prePos = preCookCounter-1;
        if(currentPos > prePos){
            unpasedPT.setColor(dartGreen);
            // 间隔
            int lineDx = 0;
            for (int i = prePos+1; i < currentPos; i++) {
                lineDx += average * lineIntervals.get(i);
            }
            //int lineNum = currentPos-prePos -1;
            //Log.i(TAG, "start: "+lineX+",end :"+(lineX+lineInterval * lineNum)+",lineNum: "+lineNum);
//            canvas.drawLine(lineX, centerY, lineX+lineInterval * lineNum, centerY, unpasedPT);
//            lineX += lineInterval * lineNum;
            Log.i(TAG, "start: "+lineX+",end :"+(lineX + lineDx)+",lineDx: "+lineDx);
            canvas.drawLine(lineX, centerY, lineX + lineDx, centerY, unpasedPT);
            lineX += lineDx;
        }
        unpasedPT.setColor(gray);
        canvas.drawLine(lineX, centerY, width - cPointPT.getStrokeWidth() *2, centerY, unpasedPT);
        for (int i = 0; i < cookStepSum; i++) {
            if(i == currentPos) { // 当前
                cPointPT.setStyle(Paint.Style.STROKE);
                cPointPT.setColor(Color.WHITE);
                canvas.drawCircle(circleStart, centerY, radius, cPointPT);
                cPointPT.setStyle(Paint.Style.FILL);
                cPointPT.setColor(dartGreen);
                canvas.drawCircle(circleStart, centerY, radius, cPointPT);
            }
            else if(i < currentPos) { // 经过的
                canvas.drawCircle(circleStart, centerY, radius- cPointPT.getStrokeWidth()/2, pasedPT);
            }
            else { // 未经过的
                canvas.drawCircle(circleStart, centerY, radius- cPointPT.getStrokeWidth()/2, unpasedPT);
            }
            if(i<preCookCounter)
                circleStart += circleInterval;
            else {
                if(lineIntervals!=null) {
                    circleStart += average * lineIntervals.get(i);
                }
                else circleStart += lineInterval;
                //circleStart += lineInterval;
            }

            // 画图标
            if(i==preCookCounter && preCookCounter>0) canvas.drawBitmap(playDrawable.getBitmap(), preCookWidth-playDrawableWidth/2f+cPointPT.getStrokeWidth() *1.5f, getX(), unpasedPT);
        }
        canvas.drawCircle(circleStart, centerY, radius- cPointPT.getStrokeWidth()/2, unpasedPT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureDimension(playDrawableHeight+20, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}


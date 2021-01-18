package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class NumText extends View {
    private final StringBuilder mBuilder = new StringBuilder("768");
    private RectF bgRectf = new RectF();
    private final Paint mPaint = new Paint();

    private int bgColor = Color.parseColor("#FF4C6D99");
    private int bgRadius= 30;

    private int numLen = 4;
    private int space = 40;
    private int numSize = 90;

    public NumText(Context context) {
        super(context);
    }

    public NumText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(numSize);
        mPaint.setTextAlign(Paint.Align.RIGHT);

        String format = String.format("%0" + numLen + "d", Integer.parseInt(TextUtils.isEmpty(mBuilder.toString()) ? "0": mBuilder.toString()));
        //计算baseline
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;

        bgRectf.top = 0 ;
        bgRectf.bottom = getHeight();

        float baseline = bgRectf.centerY() + distance;
        for (int i = 0; i < numLen; i++){
            bgRectf.left = (numSize + space) * i;
            bgRectf.right = bgRectf.left + numSize + 20;

            mPaint.setColor(bgColor);
            canvas.drawRoundRect(bgRectf, bgRadius, bgRadius, mPaint);// 绘制背景 rx ry 圆角的X y轴半径

            //绘制数字
            mPaint.setColor( i <= format.lastIndexOf('0') ? Color.WHITE : Color.BLACK);
            canvas.drawText(format.toCharArray(), i, 1, bgRectf.centerX() +space/2.f, baseline, mPaint);
        }
    }

    public void del(){
        if(mBuilder.length() > 0) {
            mBuilder.deleteCharAt(mBuilder.length() - 1);
            invalidate();
        }
    }

    public void appCode(int code){
        mBuilder.appendCodePoint(code);
        if(mBuilder.length() > numLen){
            mBuilder.deleteCharAt(0);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heithtSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = 400;
        int minHeight = 200;
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidth, minHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidth, heithtSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, minHeight);
        }
    }
}

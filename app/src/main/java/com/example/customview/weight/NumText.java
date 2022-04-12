package com.example.customview.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.R;

import androidx.annotation.Nullable;

public class NumText extends View {
    private final StringBuilder mBuilder = new StringBuilder("768");
    private RectF bgRectf;
    private Paint mPaint;

    private int bgColor = Color.parseColor("#FF4C6D99");
    private int bgRadius= 30;

    private int numLen = 4;

    private float space;
    private float padding;
    private float numSize;

    private void init(Context c,AttributeSet attr){
        if(mPaint==null){
            mPaint = new Paint();
        }
        if(bgRectf==null)
            bgRectf  = new RectF();
        if(attr!=null){
            TypedArray arr;
            arr = c.obtainStyledAttributes(attr, R.styleable.nt);
            numSize = arr.getDimension(R.styleable.nt_size,90f);
            space = arr.getDimension(R.styleable.nt_space,5f);
            padding = arr.getDimension(R.styleable.nt_padding,10f);
        }
        else {
            numSize = 90f;
            space = 5f;
            padding = 10f;
        }
        mPaint.setTextSize(numSize);
        mPaint.setTextAlign(Paint.Align.RIGHT);
    }

    public NumText(Context context) {
        super(context);
        init(context,null);
    }

    public NumText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public NumText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String format = String.format("%0" + numLen + "d", Integer.parseInt(TextUtils.isEmpty(mBuilder.toString()) ? "0": mBuilder.toString()));
        //计算baseline
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;

        bgRectf.top = 0 ;
        bgRectf.bottom = getHeight();

        float baseline = bgRectf.centerY() + distance;

        int leftIndex = leftZeroIndex(format);
        for (int i = 0; i < numLen; i++){
            float width = mPaint.measureText(format.toCharArray(), i, 1) + padding;
            bgRectf.left = (width + space) * i;
            bgRectf.right = bgRectf.left + width;

            mPaint.setColor(bgColor);
            canvas.drawRoundRect(bgRectf, bgRadius, bgRadius, mPaint);// 绘制背景 rx ry 圆角的X y轴半径

            //绘制数字
            mPaint.setColor( i <= leftIndex ? Color.WHITE : Color.BLACK);
            canvas.drawText(format.toCharArray(), i, 1, bgRectf.left+width - padding * 0.5f, baseline, mPaint);
        }
    }

    private int leftZeroIndex(String str){
        int lastIndex = -1;
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if(chars[i] != '0') break;
            lastIndex = i;
        }
        return lastIndex;
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
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // match_parent和确定值
        int height = (int) (mPaint.getTextSize() + mPaint.getStrokeWidth()*2);
        setMeasuredDimension(wSpecSize,hSpecMode==MeasureSpec.EXACTLY ? hSpecSize : height);
    }
}

package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class NumberText extends EditText {
    private RoundBackgroundColorSpan bgColorSpan = new RoundBackgroundColorSpan(Color.parseColor("#4C6D99"),Color.parseColor("#FFFFFF"),0);
    private SpannableStringBuilder timerSpan = new SpannableStringBuilder();

    public NumberText(Context context) {
        super(context);
    }

    public NumberText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    Paint _paint = new Paint();
    RectF r2 = new RectF();

    int numberBit = 3;
/*
onDraw: RectF(38.4, 0.0, 422.4, 511.0)
onDraw: RectF(499.19998, 0.0, 883.19995, 511.0)
onDraw: RectF(960.0, 0.0, 1344.0, 511.0)
onDraw: RectF(1420.7999, 0.0, 1804.7999, 511.0)
    */
/*
onDraw: RectF(38.4, 0.0, 422.4, 511.0)
onDraw: RectF(499.19998, 0.0, 883.19995, 511.0)
onDraw: RectF(960.0, 0.0, 1344.0, 511.0)
onDraw: RectF(1420.7999, 0.0, 1804.7999, 511.0)
 */
    @Override
    protected void onDraw(Canvas canvas) {
        _paint.setColor(Color.parseColor("#FF4C6D99"));

        r2.top = 0 ;
        r2.bottom = getHeight();
        int length = getText().length();
        float textWidth = getTextSize();
        /*float start = getLetterSpacing() * textWidth / 2;
        for (int i=0; i<length; i++){
            r2.left = start + (getLetterSpacing() * textWidth + textWidth) * i;
            r2.right = r2.left + textWidth;
            Log.i("TAG", "onDraw: "+r2.toString());
            canvas.drawRoundRect(r2, 30, 30, _paint);// rx ry 圆角的X y轴半径
        }*/
        float end = getLetterSpacing() * textWidth / 2;
        for (int i=length; i>0; i--){
            r2.right = end + (getLetterSpacing() * textWidth + textWidth) * i;
            r2.left = r2.right + textWidth;
            Log.i("TAG", "onDraw: "+r2.toString());
            canvas.drawRoundRect(r2, 0, 0, _paint);// rx ry 圆角的X y轴半径
        }
        super.onDraw(canvas);
//        timerSpan.clear();
//        timerSpan.clearSpans();
//        timerSpan.append(getText());
//
//        if(timerSpan.length() >0)
//            timerSpan.setSpan(bgColorSpan,0, timerSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        setText(timerSpan);

        //新建一只画笔，并设置为绿色属性
    }
}

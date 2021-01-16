package com.example.customview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class RoundBackgroundColorSpan extends ReplacementSpan {

    private int mRadius = 30;
    private float mSpace = 15.0f;

    private int mSize;
    private int mColor;

    private int txtColor;

    private int mAddWidth;

    public RoundBackgroundColorSpan(int color, int txtColor, int addWidth) {
        this.mColor = color;
        this.txtColor = txtColor;
        this.mAddWidth = addWidth;
        rectF = new RectF();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius)+mAddWidth;
        return mSize;
    }

    private RectF rectF;

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        paint.setAntiAlias(true);
        float fontWidth = (mSize - (end -1) * mSpace) / end;
        float txtLeft = (paint.getTextSize()-fontWidth);
        //Log.i("TAG", "draw: "+start+","+end);
        for (int i = 0; i < end; i++) {
            float baseStart = x+(fontWidth+mSpace)*i;
            rectF.set(baseStart,y+paint.ascent(),baseStart+fontWidth,y+paint.descent());
            paint.setColor(mColor);
            canvas.drawRoundRect(rectF, mRadius, mRadius, paint);

            float txtStart = baseStart + mRadius+txtLeft;
            paint.setColor(txtColor);
            canvas.drawText(text, i, i+1, txtStart, y, paint);
        }
    }
}

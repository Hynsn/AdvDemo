package com.hynson.floatkkey;


import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 梦辛灵  实现按钮浮动工具
 */
public class FloatBtnUtil {

    final String TAG = FloatBtnUtil.class.getSimpleName();

    private static int displayHeight = 0;
    private int naviBarHeight = 0;
    private Activity mContext;
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private View root;

    public FloatBtnUtil(Activity context) {
        this.mContext = context;
        if (displayHeight == 0) {
            Display defaultDisplay = context.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            displayHeight = point.y;
        }
    }

    public void setFloatView(View root, View floatview) {
        this.root = root;    //视图根节点 floatview // 需要显示在键盘上的View组件
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                int heightDifference = displayHeight - r.height();
                boolean isKeyboardShowing = heightDifference > displayHeight / 3;
                if (isKeyboardShowing) {
                    int offsetY = -(heightDifference - naviBarHeight);
                    floatview.animate().translationY(offsetY).setDuration(0).start();
                    if (floatview.getVisibility() == View.GONE)
                        floatview.setVisibility(View.VISIBLE);
                } else {
                    naviBarHeight = heightDifference;
                    floatview.animate().translationY(0).start();
                    if (floatview.getVisibility() == View.VISIBLE)
                        floatview.setVisibility(View.GONE);
                }
                Log.i(TAG, "" + isKeyboardShowing + ",heightDifference:" + heightDifference + ",r.height:" + r.height() + ",height:" + displayHeight);

            }
        };
        root.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    public void clearFloatView() {
        if (listener != null && root != null)
            root.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

}


package com.hynson.alertwindow

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class MoveListener(val params: WindowManager.LayoutParams, val windowManager: WindowManager) :
    View.OnTouchListener {

    private var x = 0
    private var y = 0

    // 手指按下的坐标
    private var mViewDownX = 0f
    private var mViewDownY = 0f
    private var outRect = Rect()

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.rawX.toInt()
                y = event.rawY.toInt()
                // 记录按下的位置（相对 View 的坐标）
                mViewDownX = event.x
                mViewDownY = event.y

                view.getWindowVisibleDisplayFrame(outRect)
            }

            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX.toInt()
                val nowY = event.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY
                params.apply {
                    x += movedX
                    y += movedY
                }
                //更新悬浮球控件位置
                windowManager.updateViewLayout(view, params)
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                // 记录移动的位置（相对屏幕的坐标）
                val rawMoveX = event.rawX - getWindowInvisibleWidth()
                val rawMoveY = event.rawY - getWindowInvisibleHeight()
                val rawFinalX: Float
                // 获取当前屏幕的宽度
                val screenWidth: Int = getWindowWidth()
                rawFinalX = if (rawMoveX < screenWidth / 2f) {
                    // 回弹到屏幕左边
                    0f
                } else {
                    // 回弹到屏幕右边
                    screenWidth.toFloat()
                }
                // 从移动的点回弹到边界上
                startHorizontalAnimation(
                    view,
                    rawMoveX - mViewDownX,
                    rawFinalX - mViewDownX, rawMoveY - mViewDownY
                )
                view.performClick()
            }

            else -> {
                //                view.getWindowVisibleDisplayFrame()
            }
        }
        return false
    }

    private fun getWindowWidth(): Int {
        return outRect.right - outRect.left
    }

    private fun getWindowInvisibleHeight(): Int {
        return outRect.top
    }

    private fun getWindowInvisibleWidth(): Int {
        return outRect.left
    }

    private fun startHorizontalAnimation(view: View, startX: Float, endX: Float, y: Float) {
        val animator = ValueAnimator.ofFloat(startX, endX)
        animator.duration = calculateAnimationDuration(startX, endX)
        animator.addUpdateListener { animation: ValueAnimator ->
            updateLocation(
                view,
                (animation.animatedValue as Float).toInt(),
                y.toInt()
            )
        }
        animator.start()
    }

    private fun updateLocation(view: View, x: Int, y: Int) {
        // 屏幕默认的重心（一定要先设置重心位置为左上角）
        val screenGravity = Gravity.TOP or Gravity.START
        val params: WindowManager.LayoutParams = params
        // 判断本次移动的位置是否跟当前的窗口位置是否一致
        if (params.gravity == screenGravity && params.x == x && params.y == y) {
            return
        }
        params.x = x
        params.y = y
        params.gravity = screenGravity
        windowManager.updateViewLayout(view, params)
    }

    private fun calculateAnimationDuration(startCoordinate: Float, endCoordinate: Float): Long {
        // 为什么要根据距离来算出动画的时间？
        // issue 地址：https://github.com/getActivity/XToast/issues/36
        // 因为不那么做，如果悬浮球回弹的距离比较短的情况，加上 ValueAnimator 动画更新回调次数比较多的情况下
        // 会导致自动回弹的时候出现轻微卡顿，但这其实不是卡顿，而是一次滑动的距离太短的导致的
        var animationDuration = (Math.abs(endCoordinate - startCoordinate) / 2f).toLong()
        if (animationDuration > 800) {
            animationDuration = 800
        }
        return animationDuration
    }
}
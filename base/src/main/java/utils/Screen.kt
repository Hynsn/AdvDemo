package utils

import android.content.Context

/**
 * Author: Hynsonhou
 * Date: 2021/8/24 14:21
 * Description: 屏幕
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/24   1.0       首次创建
 */
object Screen {
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    @JvmStatic
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
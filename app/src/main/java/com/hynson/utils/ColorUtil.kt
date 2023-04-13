package com.hynson.utils

import android.graphics.Color
import java.util.Random

object ColorUtil {
    /**
     * 随机生成深浅色颜色对
     */
    fun getColorPair(): Pair<Int, Int> {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        val color = Color.argb(255, r, g, b)
        val gray =
            (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114).toInt()
        val textColor = if (gray <= 128) {
            Color.WHITE
        } else {
            Color.BLACK
        }

        return Pair(color, textColor)
    }
}
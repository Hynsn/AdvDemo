package com.hynson.main

import android.view.View
import com.hynson.utils.ColorUtil

data class Content(
    val type: Int,
    val name: String? = null,
    val itemAction: ((Int) -> (Unit))? = null,
    val cells: List<Cell>? = null
) {
    companion object {
        const val ITEM_TYPE = 0
        const val SECTION_TYPE = 1
    }
}

data class Cell(
    val name: String,
    val resId: Int = 0,
    val colorPair: Pair<Int, Int> = ColorUtil.getColorPair(),
    val action: ((v: View, position: Int, cell: Cell) -> (Unit))? = null,
)

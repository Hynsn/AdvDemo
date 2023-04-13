package com.hynson.main

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
    val action: ((Int) -> (Unit))
)

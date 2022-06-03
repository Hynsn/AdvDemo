package com.example.set

import android.view.ViewGroup

interface VHInf<T> {
    val type: ViewEum
    fun convert(parent: ViewGroup): T
}
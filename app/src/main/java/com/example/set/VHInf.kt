package com.example.set

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface VHInf<T> {
    val layout: Int
    fun convert(parent: ViewGroup): T
    fun getView(parent: ViewGroup, layout: Int): View =
        LayoutInflater.from(parent.context).inflate(layout, parent, false)
}
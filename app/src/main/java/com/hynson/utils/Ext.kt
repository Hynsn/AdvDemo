package com.hynson.utils

import java.util.Locale

fun Float.fm(): String {
    return String.format(Locale.US, "%.2f", this)
}
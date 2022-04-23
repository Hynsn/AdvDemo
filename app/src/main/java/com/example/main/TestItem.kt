package com.example.main

import android.os.Bundle

data class TestItem(
    val title: String,
    val intentClass: Class<*>? = null,//intent跳转
    val resId: Int? = null,// navigation跳转
    val bundle: Bundle? = null,
    val clickAction: (() -> Unit)? = null
) {
    constructor(
        title: String,
        intentClass: Class<*>?,
        bundle: Bundle?,
        clickAction: (() -> Unit)?
    ) : this(title, intentClass, null, bundle, clickAction)

    constructor(
        title: String,
        resId: Int?,
        bundle: Bundle?,
        clickAction: (() -> Unit)?
    ) : this(title, null, resId, bundle, clickAction)
}
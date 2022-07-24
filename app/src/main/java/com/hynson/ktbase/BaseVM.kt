package com.hynson.ktbase

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class BaseVM : ViewModel() {
    abstract fun registLifeOwner(owner: LifecycleOwner?)
}
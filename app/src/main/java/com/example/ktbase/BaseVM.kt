package com.example.ktbase

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class BaseVM : ViewModel() {
    abstract fun registLifeOwner(owner: LifecycleOwner?)
}
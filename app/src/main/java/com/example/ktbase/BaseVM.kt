package com.example.ktbase

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class BaseVM<F:Fragment> : ViewModel() {
    protected var mView: F? = null

    fun getView(): F? {
        return mView
    }

    fun setView(mView: F?) {
        this.mView = mView
    }

    abstract fun registLifeOwner(owner: LifecycleOwner?)
}
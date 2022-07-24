package com.hynson.customview

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.hynson.ktbase.BaseVM
import kotlinx.coroutines.launch

class CustomViewVM : BaseVM() {
    // TODO: Implement the ViewModel
//    private val mutableLiveData: MutableLiveData<Boolean>? = null
    override fun registLifeOwner(owner: LifecycleOwner?) {
//        mutableLiveData!!.observe(owner!!, Observer { })
    }

    fun loadNews() {
        viewModelScope.launch {

        }
    }

}
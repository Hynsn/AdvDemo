package com.example.navigation.setting

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.ktbase.BaseVM
import kotlinx.coroutines.launch

class SetVM : BaseVM() {
    // TODO: Implement the ViewModel
    private val mutableLiveData: MutableLiveData<Boolean>? = null
    override fun registLifeOwner(owner: LifecycleOwner?) {
//        mutableLiveData!!.observe(owner!!, Observer { })
    }

    fun loadNews() {
        viewModelScope.launch {

        }
    }

}
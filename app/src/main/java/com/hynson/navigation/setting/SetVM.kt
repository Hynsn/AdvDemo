package com.hynson.navigation.setting

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.fastdroid.ktbase.BaseVM
import kotlinx.coroutines.launch

class SetVM : BaseVM() {
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
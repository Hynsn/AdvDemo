package com.hynson.mvvm

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.fastdroid.ktbase.BaseFragment
import com.hynson.databinding.FragmentMvvmBinding

class TestMvvmFragment : BaseFragment<FragmentMvvmBinding, TestVM>() {
    override fun getVm(provider: ViewModelProvider) = provider[TestVM::class.java]

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        super.initData(owner, savedInstanceState)
        Log.i(TAG, "Activity HashCode: ${System.identityHashCode(vm)}")
        bind.tvHash.text = System.identityHashCode(vm).toString()
    }

//    override fun activityOwner(): ViewModelStoreOwner = requireActivity()

    companion object {
        private const val TAG = "BaseFragment"
    }
}
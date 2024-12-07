package com.hynson.mvvm

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.fastdroid.ktbase.BaseMvvmActivity
import com.hynson.databinding.ActivityMvvmBinding

class TestMVVMActivity : BaseMvvmActivity<ActivityMvvmBinding, TestVM>() {
    override fun getVm(provider: ViewModelProvider): TestVM {
        val viewModel = provider[TestVM::class.java]
        Log.i(TAG, "Activity HashCode: ${System.identityHashCode(viewModel)}")
        return viewModel
    }

    companion object {
        private const val TAG = "TestMVVMActivity"
    }
}
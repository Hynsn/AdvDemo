package com.hynson.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.databinding.FragCustomviewMyviewBinding
import com.fastdroid.ktbase.BaseFragment

class MyViewFragment : BaseFragment<FragCustomviewMyviewBinding, CustomViewVM>(),
    View.OnClickListener {
    override val layoutId: Int = R.layout.frag_customview_myview

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_del -> {

            }
            R.id.btn_append -> {

            }
        }
    }
}
package com.example.customview

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.R
import com.example.databinding.FragCustomviewTimelineBinding
import com.example.ktbase.BaseFragment

class TimelineFragment : BaseFragment<FragCustomviewTimelineBinding,CustomViewVM>(){
    override val layoutId: Int = R.layout.frag_customview_timeline

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun bindView() {

    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }
}
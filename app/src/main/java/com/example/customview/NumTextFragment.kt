package com.example.customview

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.R
import com.example.customview.weight.RulerView
import com.example.databinding.FragCustomviewNumtextBinding
import com.example.databinding.FragCustomviewScrollRuleBinding
import com.example.ktbase.BaseFragment

class NumTextFragment : BaseFragment<FragCustomviewNumtextBinding,CustomViewVM>(){
    override val layoutId: Int = R.layout.frag_customview_numtext

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun bindView() {

    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }
}
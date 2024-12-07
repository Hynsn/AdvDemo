package com.hynson.customview

import androidx.lifecycle.ViewModelProvider
import com.fastdroid.ktbase.BaseFragment
import com.hynson.R
import com.hynson.databinding.FragCustomviewExpandtextviewBinding

class ExpandTextViewFragment : BaseFragment<FragCustomviewExpandtextviewBinding, CustomViewVM>() {
    override val layoutId: Int = R.layout.frag_customview_expandtextview

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)
}
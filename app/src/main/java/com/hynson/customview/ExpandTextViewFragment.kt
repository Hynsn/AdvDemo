package com.hynson.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.databinding.FragCustomviewExpandtextviewBinding
import com.fastdroid.ktbase.BaseFragment

class ExpandTextViewFragment : BaseFragment<FragCustomviewExpandtextviewBinding, CustomViewVM>() {
    override val layoutId: Int = R.layout.frag_customview_expandtextview

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragCustomviewExpandtextviewBinding.inflate(inflater, container, false)

    override fun bindView() {
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }
}
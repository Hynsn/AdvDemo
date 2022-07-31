package com.hynson.customview

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.customview.weight.NumSpan
import com.hynson.databinding.FragCustomviewMyviewBinding
import com.hynson.databinding.FragCustomviewNumtextBinding
import com.hynson.ktbase.BaseFragment

class MyViewFragment : BaseFragment<FragCustomviewMyviewBinding, CustomViewVM>(),
    View.OnClickListener {
    override val layoutId: Int = R.layout.frag_customview_myview

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragCustomviewMyviewBinding.inflate(inflater, container, false)

    override fun bindView() {

    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_del -> {

            }
            R.id.btn_append -> {

            }
        }
    }
}
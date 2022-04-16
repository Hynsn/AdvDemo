package com.example.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.R
import com.example.customview.weight.RulerView
import com.example.databinding.FragCustomviewScrollRuleBinding
import com.example.ktbase.BaseFragment

class RulerViewFragment : BaseFragment<FragCustomviewScrollRuleBinding,CustomViewVM>(), View.OnClickListener{
    override val layoutId: Int = R.layout.frag_customview_scroll_rule
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragCustomviewScrollRuleBinding.inflate(inflater,container,false)

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun bindView() {
        bind.rulerWeight.apply {
            setScope(1000,13000,500, RulerView.RulerType.DISTANCE)
            setCurrentValue(1000)
            setScrollSelected { s,v ->
                bind.tvText.text = s
            }
        }

        bind.rulerTime.apply {
            setScope(10,600,5,RulerView.RulerType.TIME)
            setCurrentValue(600)
            setScrollSelected { s,v ->
                bind.tvTime.text = s.toString()
            }
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View?) {
    }
}
package com.hynson.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.databinding.FragChartItemBinding
import com.fastdroid.ktbase.BaseFragment

class ChartFragment : BaseFragment<FragChartItemBinding, ChartVM>(), View.OnClickListener {
    override val layoutId: Int = R.layout.frag_chart_item
    override fun getVm(provider: ViewModelProvider) = provider.get(ChartVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragChartItemBinding.inflate(inflater, container, false)

    override fun bindView() {

    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
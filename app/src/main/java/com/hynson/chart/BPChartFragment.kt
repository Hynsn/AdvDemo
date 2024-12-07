package com.hynson.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.databinding.FragChartBpchartBinding
import com.fastdroid.ktbase.BaseFragment

class BPChartFragment : BaseFragment<FragChartBpchartBinding, ChartVM>(), View.OnClickListener {
    override val layoutId: Int = R.layout.frag_chart_bpchart
    override fun getVm(provider: ViewModelProvider) = provider.get(ChartVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragChartBpchartBinding.inflate(inflater, container, false)

    override fun bindView() {

        val d1 = BPChartData(
            dpInMin = 85, dpInMax = 120,
            spInMin = 12, spInMax = 24,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            timeStr = "2022-01-10"
        )
        val d2 = BPChartData(
            dpInMin = 1, dpInMax = 10,
            spInMin = 30, spInMax = 40,
            maxTimestamp = 1657261877, minTimestamp = 1657261836,
            timeStr = "2022-01-11"
        )

        val d3 = BPXaxisChart.ChartData(
            dpInMin = 85, dpInMax = 120,
            spInMin = 12, spInMax = 24,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            timeStr = "2022-01-10"
        )
        val d4 = BPXaxisChart.ChartData(
            dpInMin = 1, dpInMax = 10,
            spInMin = 30, spInMax = 40,
            maxTimestamp = 1657261877, minTimestamp = 1657261836,
            timeStr = "2022-01-11"
        )
        bind.bpChart.apply {
            setYaxisMaxMin(300f,0f)
            setTargetMaxMin(140f,90f)
        }
        bind.bpChart1.apply {
            setYaxisMaxMin(300f,0f)
            setTargetMaxMin(140f,90f)
        }

        bind.bpChart2.apply {
            setYaxisMaxMin(300f,0f)
            setData(BPXaxisChart.DateType.DAY,arrayListOf(d3,d4))
        }
        bind.chartBp.apply {
            setMaxMinYAxis(300,0)
            initChartData(BPQueryTimeType.DAY, arrayListOf(d1,d2), true)
            setOnScaleListener { position, yAxisValue, data ->

            }
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
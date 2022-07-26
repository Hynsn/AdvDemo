package com.hynson.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.customview.weight.RulerView
import com.hynson.databinding.FragChartBpchartBinding
import com.hynson.ktbase.BaseFragment

class BPChartFragment : BaseFragment<FragChartBpchartBinding, ChartVM>(), View.OnClickListener {
    override val layoutId: Int = R.layout.frag_chart_bpchart
    override fun getVm(provider: ViewModelProvider) = provider.get(ChartVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragChartBpchartBinding.inflate(inflater, container, false)

    override fun bindView() {

        val d1 = BloodPressureChartData(
            dpInMmHgMin = 85, dpInMmHgMax = 120,
            spInMmHgMin = 12, spInMmHgMax = 24,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            dpInKpaMin = 113, dpInKpaMax = 113,
            spInKpaMin = 16, spInKpaMax = 16,
            timeStr = "2022-01-10"
        )
        val d2 = BloodPressureChartData(
            dpInMmHgMin = 1, dpInMmHgMax = 9,
            spInMmHgMin = 1, spInMmHgMax = 20,
            maxTimestamp = 1657261877, minTimestamp = 1657261836,
            dpInKpaMin = 3, dpInKpaMax = 12,
            spInKpaMin = 1, spInKpaMax = 5,
            timeStr = "2022-01-11"
        )
        bind.chartBp.apply {
            initChartData(BPQueryTimeType.DAY, BPUnitType.KPA, arrayListOf(d1,d2), true)
            setOnScaleListener { position, yAxisValue, data ->

            }
        }
        val d3 = BPYaxisChart.ChartData(
            dpInMin = 85, dpInMax = 120,
            spInMin = 12, spInMax = 24,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            timeStr = "2022-01-10"
        )
        val d4 = BPYaxisChart.ChartData(
            dpInMin = 1, dpInMax = 10,
            spInMin = 30, spInMax = 40,
            maxTimestamp = 1657261877, minTimestamp = 1657261836,
            timeStr = "2022-01-11"
        )
        bind.bpChart.apply {
            setYaxisMaxMin(300f,0f)
            setTargetMaxMin(140f,90f)
            setData(BPYaxisChart.DateType.DAY,arrayListOf(d3,d4))
            XaxisHeight = bind.chartBp.shadowMarginHeight
        }

        bind.bpChart1.apply {
            setYaxisMaxMin(300f,0f)
            setTargetMaxMin(140f,90f)
            setData(BPYaxisChart.DateType.DAY,arrayListOf(d3,d4))
            XaxisHeight = 0
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
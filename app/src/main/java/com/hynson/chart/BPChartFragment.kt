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
//[Data{spInMmHgMax=12, spInMmHgMin=12, dpInMmHgMax=85, dpInMmHgMin=85, spInKpaMax=16, spInKpaMin=16, dpInKpaMax=113, dpInKpaMin=113},
// Data{spInMmHgMax=4, spInMmHgMin=1, dpInMmHgMax=9, dpInMmHgMin=2, spInKpaMax=5, spInKpaMin=1, dpInKpaMax=12, dpInKpaMin=3}]

        val d1 = BloodPressureChartData(
            dpInMmHgMin = 85, dpInMmHgMax = 85,
            dpInKpaMin = 113, dpInKpaMax = 113,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            spInKpaMin = 16, spInKpaMax = 16,
            spInMmHgMin = 12, spInMmHgMax = 12,
            timeStr = "2022-01-10"
        )
        val d2 = BloodPressureChartData(
            dpInMmHgMin = 2, dpInMmHgMax = 9,
            dpInKpaMin = 3, dpInKpaMax = 12,
            maxTimestamp = 1657261877, minTimestamp = 1657261836,
            spInKpaMin = 1, spInKpaMax = 5,
            spInMmHgMin = 1, spInMmHgMax = 4,
            timeStr = "2022-01-11"
        )
        bind.chartBp.apply {
            initChartData(BPQueryTimeType.DAY, BPUnitType.MMHG, arrayListOf(d1, d2), true)
            setOnScaleListener { position, yAxisValue, data ->

            }
        }
        bind.bpChart.apply {
            setYaxisMaxMin(300f,0f)
            XaxisHeight = bind.chartBp.shadowMarginHeight
        }
        bind.bpChart1.apply {
            setYaxisMaxMin(300f,0f)
            XaxisHeight = 0
        }
        bind.rulerWeight.apply {
            setScope(1000,13000,500, RulerView.RulerType.DISTANCE)
            setCurrentValue(1000)
            setScrollSelected { s,v ->

            }
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
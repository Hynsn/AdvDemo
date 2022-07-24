package com.hynson.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
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
            dpInMmHgMin = 80, dpInMmHgMax = 90,
            dpInKpaMin = 113, dpInKpaMax = 160,
            maxTimestamp = 1641744645, minTimestamp = 1641744645,
            spInKpaMin = 16, spInKpaMax = 16,
            spInMmHgMin = 12, spInMmHgMax = 12,
            timeStr = "2022-01-10"
        )
        val d2 = BloodPressureChartData(
            dpInMmHgMin = 40, dpInMmHgMax = 90,
            dpInKpaMin = 45, dpInKpaMax = 80,
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
            dataType = BPUnitType.MMHG
            if (dataType == BPUnitType.MMHG) {
                setMaxYAxisAndMinYAxis(300.0, 0.0)
            } else {
                setMaxYAxisAndMinYAxis(40.0, 0.0)
            }
            showMiddleLine(true)
            xAxisHeight = bind.chartBp.shadowMarginHeight
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
package com.hynson.bpchart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hynson.R
import com.hynson.chart.BPChartData

class ChartView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var rvData: RecyclerView
    private var chartAdapter:ChartAdapter

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.chart_layout, this)
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
            timeStr = "1"
        )
        val data = arrayListOf(d1,d2)
        for (i in 1..200){
            val item = BPChartData(
                dpInMin = 85, dpInMax = 120,
                spInMin = 12, spInMax = 24,
                maxTimestamp = 1641744645, minTimestamp = 1641744645,
                timeStr = "$i"
            )
            data.add(item)
        }


        rvData = root.findViewById(R.id.rv_data)
        val manager = LinearLayoutManager(context)
            .apply {
                isSmoothScrollbarEnabled = true
                orientation = LinearLayoutManager.HORIZONTAL
            }

        chartAdapter = ChartAdapter(data)
        rvData.apply {
            layoutManager = manager
            adapter = chartAdapter
        }
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvData)

        chartAdapter.emptyWidth = rvData.layoutParams.width / 2

        chartAdapter.notifyDataSetChanged()
    }
}
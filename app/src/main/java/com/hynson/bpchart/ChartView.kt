package com.hynson.bpchart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hynson.R
import com.hynson.chart.BPChartData
import com.hynson.chart.BPYaxisChart
import utils.Screen


class ChartView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var rvData: RecyclerView
    private val chartBg: BPYaxisChart
    private var chartAdapter: ChartAdapter
    private var tvTip:TextView

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.chart_layout, this)
        /*val d1 = BPChartData(
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
        )*/
        val data = ArrayList<BPChartData>()
        for (i in 1..10) {
            val item = BPChartData(
                dpInMin = 85, dpInMax = 120,
                spInMin = 12, spInMax = 24,
                maxTimestamp = 1641744645, minTimestamp = 1641744645,
                timeStr = "$i"
            )
            if(i== 2){
                item.isSelect = true
            }
            data.add(item)
        }

        tvTip = root.findViewById(R.id.tv_tip)
        rvData = root.findViewById(R.id.rv_data)
        chartBg = root.findViewById(R.id.bp_chart)
        /*chartBg.apply {
            setTargetMaxMin(140f,90f)
            setYaxisMaxMin(200f,0f)
        }*/
        val manager = LinearLayoutManager(context)
            .apply {
                isSmoothScrollbarEnabled = true
                orientation = LinearLayoutManager.HORIZONTAL
            }
        chartAdapter = ChartAdapter(data)
        val snapHelper = LinearSnapHelper()

        val listener = object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val manager = recyclerView.layoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    /*manager.let {
                        if(it is LinearLayoutManager){
                            val startPos = it.findFirstVisibleItemPosition()
                            val endPos = it.findLastVisibleItemPosition()

                            tvTip.text = "$startPos,$endPos,${data[startPos].timeStr},${data[endPos].timeStr}"
                        }
                    }*/
                    val childView = snapHelper.findSnapView(manager)
                    childView?.let {
                        val pos = recyclerView.getChildAdapterPosition(it)
                        tvTip.text = "$pos,${data[pos].timeStr}"

                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        rvData.apply {
            layoutManager = manager
            adapter = chartAdapter
            addOnScrollListener(listener)
        }
        snapHelper.attachToRecyclerView(rvData)

        val barWidth = getRvWidth(context) / BarType.WEEK.divisor
        chartAdapter.emptyWidth = ((getRvWidth(context)-barWidth) / 2)
        chartAdapter.otherWidth = barWidth
    }

    private fun getRvWidth(context: Context): Int {
        val width = context.resources.displayMetrics.widthPixels
        return (width - Screen.dp2px(context, 30f))
    }

    enum class BarType(val divisor: Int) {
        DAY(12),
        WEEK(6),
        MONTH(13)
    }
}
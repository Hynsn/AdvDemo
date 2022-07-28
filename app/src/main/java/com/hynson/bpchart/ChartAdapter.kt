package com.hynson.bpchart

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hynson.R
import com.hynson.chart.BPChartData

class ChartAdapter(val data: List<BPChartData>) : RecyclerView.Adapter<ChartAdapter.ChartVH>() {

    var emptyWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartAdapter.ChartVH {
        return ChartVH(
            LayoutInflater.from(parent.context).inflate(R.layout.chart_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChartAdapter.ChartVH, position: Int) {
        val info = data[position]
        with(holder) {
            tvItem.text = info.timeStr
            if(isEmpty(position)){
                llItem.apply {
                    val lp = layoutParams
                    lp.width = emptyWidth
                    layoutParams = lp
                    gravity = Gravity.END
                }
                bar.visibility = View.GONE
                tvItem.visibility = View.GONE
            }
            else{
                llItem.apply {
                    val lp = layoutParams
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams = lp
                    gravity = Gravity.CENTER
                }
                bar.visibility = View.VISIBLE
                tvItem.visibility = View.VISIBLE
                bar.setData(10,0,50,10)
            }
//            val color = if(isEmpty(position)) Color.WHITE else Color.GRAY
//            llItem.setBackgroundColor(color)
        }
    }

    override fun getItemCount(): Int = data.size

    private fun isEmpty(pos: Int): Boolean {
        return pos == 0 || pos == (itemCount - 1)
    }

    inner class ChartVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.tv_name)
        val llItem: LinearLayout = itemView.findViewById(R.id.ll_chart)
        val bar = itemView.findViewById<BarView>(R.id.bv_bar)
    }

//    inner class EmptyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun render() {
//            if (itemView.layoutParams is RecyclerView.LayoutParams) {
//                val params = itemView.layoutParams
//                params.width = itemView.width / 2
//                itemView.layoutParams = params
//            }
//        }
//    }

}
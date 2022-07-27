package com.hynson.bpchart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hynson.R
import com.hynson.chart.BPChartData

class ChartAdapter(val data: List<BPChartData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            EmptyVH(
                LayoutInflater.from(parent.context).inflate(R.layout.chart_empty, parent, false)
            )
        } else {
            ChartVH(
                LayoutInflater.from(parent.context).inflate(R.layout.chart_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val info = data[position]
        when (holder) {
            is ChartVH -> {
                with(holder) {
                    tvItem.text = info.timeStr
                }
            }
            is EmptyVH -> {
                holder.render()
            }
        }

    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (isEmpty(position)) 0
        else 1
    }

    private fun isEmpty(pos: Int): Boolean {
        return pos == 0 || pos == (itemCount - 1)
    }

    inner class ChartVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.tv_name)
    }

    inner class EmptyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render() {
            if (itemView.layoutParams is RecyclerView.LayoutParams) {
                val params = itemView.layoutParams
                params.width = itemView.width / 2
                itemView.layoutParams = params
            }
        }
//        val tvItem: TextView = itemView.findViewById(R.id.tv_name)
    }

}
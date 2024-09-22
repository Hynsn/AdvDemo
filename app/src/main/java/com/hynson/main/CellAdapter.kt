package com.hynson.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hynson.databinding.ItemContentCellBinding
import com.hynson.utils.ColorUtil


class CellAdapter : BaseQuickAdapter<Cell, CellAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val cell: ItemContentCellBinding = ItemContentCellBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(cell.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: Cell?) {
        holder.cell.tvName.text = item?.name
        item?.colorPair?.run {
            holder.cell.llCell.setBackgroundColor(first)
            holder.cell.tvName.setTextColor(second)
        }
    }
}
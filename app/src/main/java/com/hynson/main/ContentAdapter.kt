package com.hynson.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.hynson.databinding.ItemContentBinding
import com.hynson.databinding.ItemContentCellsBinding
import com.hynson.main.Content.Companion.ITEM_TYPE
import com.hynson.main.Content.Companion.SECTION_TYPE
import com.hynson.recyclerview.FlowLayoutManager


class ContentAdapter(data: List<Content>) :
    BaseMultiItemAdapter<Content>(data) {

    // 类型 1 的 viewholder
    class ItemVH(val viewBinding: ItemContentBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 类型 2 的 viewholder
    class HeaderVH(val viewBinding: ItemContentCellsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    // 在 init 初始化的时候，添加多类型
    init {
        addItemType(ITEM_TYPE, object : OnMultiItemAdapterListener<Content, ItemVH> { // 类型 1
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
                // 创建 viewholder
                val viewBinding =
                    ItemContentBinding.inflate(LayoutInflater.from(context), parent, false)
                return ItemVH(viewBinding)
            }

            override fun onBind(holder: ItemVH, position: Int, item: Content?) {
                val visible = if (item?.itemAction == null) View.GONE else View.VISIBLE
                item?.run {
                    holder.viewBinding.tvName.text = name
                    holder.viewBinding.ivValue.visibility = visible
                }
            }
        }).addItemType(
            SECTION_TYPE,
            object : OnMultiItemAdapterListener<Content, HeaderVH> { // 类型 2
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): HeaderVH {
                    val viewBinding =
                        ItemContentCellsBinding.inflate(LayoutInflater.from(context), parent, false)
                    return HeaderVH(viewBinding)
                }

                override fun onBind(holder: HeaderVH, position: Int, item: Content?) {
                    val cellsAdapter = CellAdapter()
                    item?.cells?.let {
                        cellsAdapter.items = it
                    }
                    holder.viewBinding.rvCells.apply {
                        layoutManager = FlowLayoutManager()
                        adapter = cellsAdapter
                    }
                    cellsAdapter.apply {
                        setOnItemClickListener { _, _, position ->
                            items[position].run {
                                action?.invoke(position, this)
                            }
                        }
                    }
                }

                override fun isFullSpanItem(itemType: Int): Boolean {
                    // 使用GridLayoutManager时，此类型的 item 是否是满跨度
                    return true;
                }

            }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            list[position].type
        }
    }
}
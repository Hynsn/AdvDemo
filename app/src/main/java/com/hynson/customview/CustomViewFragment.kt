package com.hynson.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hynson.R
import com.hynson.main.TestItem
import com.hynson.main.TestItemAdapter
import com.hynson.databinding.FragCustomviewBinding
import com.hynson.ktbase.BaseFragment

class CustomViewFragment : BaseFragment<FragCustomviewBinding, CustomViewVM>() {
    override val layoutId: Int = R.layout.frag_customview

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragCustomviewBinding.inflate(inflater, container, false)

    override fun bindView() {
        val itemList: MutableList<TestItem> = ArrayList()
        itemList.add(TestItem("TimeLine", R.id.timelineFragment, null, null))
        itemList.add(TestItem("NumText", R.id.numTextFragment, null, null))
        itemList.add(TestItem("RulerView", R.id.rulerViewFragment, null, null))
        itemList.add(TestItem("FlowLayout", R.id.flowLayoutFragment, null, null))
        itemList.add(TestItem("PieChart", R.id.pieChartFragment, null, null))
        itemList.add(TestItem("BPChart", R.id.BPChartFragment, null, null))
        itemList.add(TestItem("MyView", R.id.myViewFragment, null, null))
        itemList.add(TestItem("BarView",R.id.chartFragment,null,null))

        val itemAdapter = TestItemAdapter(requireContext(), itemList)
        bind.rvCustomView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = itemAdapter
        }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }
}
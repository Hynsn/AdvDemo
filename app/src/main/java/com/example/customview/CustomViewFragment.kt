package com.example.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.R
import com.example.main.TestItem
import com.example.main.TestItemAdapter
import com.example.databinding.FragCustomviewBinding
import com.example.ktbase.BaseFragment

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
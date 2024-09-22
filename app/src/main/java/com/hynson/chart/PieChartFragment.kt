package com.hynson.chart

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.customview.flowlayout.TagInfo
import com.hynson.customview.flowlayout.TagType
import com.hynson.databinding.FragChartPiechartBinding
import com.fastdroid.ktbase.BaseFragment
import com.hynson.view.chart.Chart

class PieChartFragment : BaseFragment<FragChartPiechartBinding, ChartVM>(),View.OnClickListener{
    override val layoutId: Int = R.layout.frag_customview_flowlayout
    override fun getVm(provider: ViewModelProvider) = provider.get(ChartVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragChartPiechartBinding.inflate(inflater,container,false)

    override fun bindView() {

        bind.pieChart.apply {
            dataTextTypeface = Typeface.createFromAsset(context.assets, "fonts/BEBAS.ttf")
            dataText = "99.5"
            charts = arrayListOf(
                Chart(33, resources.getColor(R.color.color_ffb432)),
                Chart(33, resources.getColor(R.color.color_ff7d5d)),
                Chart(34, resources.getColor(R.color.color_00c1bc)),
            )
        }
    }
    fun addTags(tagId: String, stringArray: Array<String>, type: TagType): List<TagInfo> {
        val list: MutableList<TagInfo> = ArrayList<TagInfo>()
        var tagInfo: TagInfo
        var name: String
        if (stringArray.isNotEmpty()) {
            for (i in stringArray.indices) {
                name = stringArray[i]
                tagInfo = TagInfo()
                tagInfo.type = type
                tagInfo.tagName = name
                tagInfo.tagId = tagId + i
                list.add(tagInfo)
            }
        }
        return list
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {

    }
}
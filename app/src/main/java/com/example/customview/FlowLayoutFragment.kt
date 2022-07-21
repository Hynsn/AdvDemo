package com.example.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.R
import com.example.customview.flowlayout.TagInfo
import com.example.databinding.FragCustomviewFlowlayoutBinding
import com.example.ktbase.BaseFragment

class FlowLayoutFragment : BaseFragment<FragCustomviewFlowlayoutBinding,CustomViewVM>(),View.OnClickListener{
    override val layoutId: Int = R.layout.frag_customview_flowlayout
    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragCustomviewFlowlayoutBinding.inflate(inflater,container,false)

    private val myTagInfos = java.util.ArrayList<TagInfo>()

    override fun bindView() {
        bind.btnAdd.setOnClickListener(this)
        bind.btnClear.setOnClickListener(this)

        val tagsDefault = resources.getStringArray(R.array.tags_default)
        val tagsRecommend = resources.getStringArray(R.array.tags_recommend)
        myTagInfos.addAll(addTags("fix", tagsDefault, TagInfo.TYPE_TAG_SERVICE))
        myTagInfos.addAll(addTags("default", tagsRecommend, TagInfo.TYPE_TAG_USER))
        bind.newsTag.setTags(myTagInfos)
    }
    fun addTags(tagId: String, stringArray: Array<String>, type: Int): List<TagInfo> {
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
    private fun initTagDrag() {
        bind.newsTag.apply {
            enableDragAndDrop()
            setIsEdit(true)
        }
    }

    private fun initTagDefault() {
        bind.newsTag.apply {
            setDefault()
            setIsEdit(false)
        }

    }
    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_add -> {
                initTagDefault()
            }
            R.id.btn_clear -> {
                initTagDrag()

            }
        }
    }
}
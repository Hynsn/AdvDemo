package com.hynson.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.databinding.FragCustomviewTimelineBinding
import com.fastdroid.ktbase.BaseFragment

class TimelineFragment : BaseFragment<FragCustomviewTimelineBinding, CustomViewVM>() {
    private var progress = 0
    val stepSeconds: ArrayList<Int> = ArrayList()

    override val layoutId: Int = R.layout.frag_customview_timeline

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun bindView() {
        bind.btnNext.setOnClickListener(this::click)
        bind.btnReset.setOnClickListener(this::click)
    }

    fun click(v: View) {
        when (v.id) {
            R.id.btn_reset -> {
                progress = 0
                bind.timePb.setCurrentPos(progress)
            }
            R.id.btn_next -> {
                progress++
                if (progress > stepSeconds.size) {
                    progress = 0
                }
                bind.timePb.setCurrentPos(progress)
            }
        }
    }

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        stepSeconds.add(2)
        stepSeconds.add(2)
        stepSeconds.add(2)
        stepSeconds.add(2)
        stepSeconds.add(2)

        stepSeconds.add(2)
        stepSeconds.add(2)
        stepSeconds.add(4)
        stepSeconds.add(4)
        stepSeconds.add(4)
        stepSeconds.add(4)
        stepSeconds.add(4)

        bind.timePb.setStep(9, stepSeconds)
    }
}
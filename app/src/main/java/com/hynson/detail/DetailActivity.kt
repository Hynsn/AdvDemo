package com.hynson.detail

import android.view.View
import android.widget.TextView
import com.base.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityDetailBinding
import com.hynson.detail.chefsnote.ChefsNoteView

/**
 * Author: Hynsonhou
 * Date: 2023/5/18 10:10
 * Description: 详情
 * History:
 * <author> <time> <version> <desc>
 * Hynsonhou 2023/5/18 1.0 首次创建
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    override fun getLayout() = R.layout.activity_detail

    override fun bindView() {
        val tv = TextView(this)
        tv.text = "动态添加的"
        binding.llParent.addView(tv)
        val view = ChefsNoteView(this).apply {
            viewModel.notes = "这是在顶部用"
        }
        binding.btnHide.setOnClickListener {
            view.visibility = View.INVISIBLE
        }
        binding.btnShow.setOnClickListener {
            view.visibility = View.VISIBLE
        }
        binding.btnGone.setOnClickListener {
            view.visibility = View.GONE
        }
        binding.llParent.addView(view)
        binding.llParent.addView(ChefsNoteView(this))
        binding.llParent.addView(ChefsNoteView(this))
    }
}
package com.hynson.coordinatorlayout

import android.graphics.Color
import android.view.View
import com.base.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityCoordinatorlayoutBinding

class CoordinatorActivity : BaseActivity<ActivityCoordinatorlayoutBinding>(), View.OnClickListener {

    override fun getLayout() = R.layout.activity_coordinatorlayout

    override fun bindView() {
        binding.clRoot.setBackgroundColor(Color.TRANSPARENT)
        binding.clRoot.invalidate()
    }

    override fun onClick(p0: View) {
        when (p0.id) {

        }
    }
}
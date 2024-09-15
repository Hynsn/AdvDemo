package com.hynson.coordinatorlayout

import android.graphics.Color
import android.util.Log
import android.view.View
import com.fastdroid.base.BaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.hynson.R
import com.hynson.databinding.ActivityCoordinatorlayoutBinding
import kotlin.math.abs

class CoordinatorActivity : BaseActivity<ActivityCoordinatorlayoutBinding>(), View.OnClickListener {

    override fun getLayout() = R.layout.activity_coordinatorlayout

    private var mState: CollapsingToolbarLayoutState = CollapsingToolbarLayoutState.COLLAPSED

    private enum class CollapsingToolbarLayoutState {
        EXPANDED, COLLAPSED, INTERNEDIATE
    }

    override fun bindView() {
        window.decorView.setBackgroundColor(Color.TRANSPARENT)
        binding.ablBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (mState != CollapsingToolbarLayoutState.EXPANDED) {
                    mState = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    Log.i(TAG, "1")
                    window.decorView.setBackgroundColor(Color.TRANSPARENT)
                    binding.clRoot.setBackgroundColor(Color.TRANSPARENT)
                    binding.clRoot.invalidate()
                }
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                if (mState != CollapsingToolbarLayoutState.COLLAPSED) {
                    mState = CollapsingToolbarLayoutState.COLLAPSED
                    Log.i(TAG, "2")
                    window.decorView.setBackgroundColor(Color.TRANSPARENT)
                    binding.clRoot.setBackgroundColor(Color.TRANSPARENT)
                }
            } else {
                if (mState != CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (mState == CollapsingToolbarLayoutState.COLLAPSED) {
                        Log.i(TAG, "3")
                        window.decorView.setBackgroundColor(Color.TRANSPARENT)
                        window.decorView.setBackgroundColor(Color.TRANSPARENT)
                        binding.clRoot.setBackgroundColor(Color.TRANSPARENT)
                    }
                    Log.i(TAG, "4")
                    mState = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                }
            }
        })
    }

    override fun onClick(p0: View) {
        when (p0.id) {

        }
    }

    companion object {
        private const val TAG = "CoordinatorActivity"
    }
}
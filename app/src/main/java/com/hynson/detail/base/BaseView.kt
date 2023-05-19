package com.hynson.detail.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 *
 */
abstract class BaseView<VIEW : ViewDataBinding, DATA : BaseViewModel>(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs),
    LifecycleOwner {
    val binding: VIEW
    var viewModel: DATA

    private val registry by lazy {
        LifecycleRegistry(this)
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, layoutId(), this, false)
        viewModel = createViewModel(this)
        registry.currentState = Lifecycle.State.STARTED
        addView(binding.root)
    }

    private fun bindViewModel() {
        binding.setVariable(variableId, viewModel)
        binding.executePendingBindings()
    }

    /**
     * 布局加载器从xml加载完成所有的子View触发
     */
    override fun onFinishInflate() {
        super.onFinishInflate()

        Log.i(TAG, "onFinishInflate: ")
    }

    /**
     * 关联到窗口时
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i(TAG, "onAttachedToWindow: ")
        registry.currentState = Lifecycle.State.CREATED
        setDataToView(viewModel)
        bindViewModel()
    }

    /**
     * 从窗口分离时
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.i(TAG, "onDetachedFromWindow: ")
        registry.currentState = Lifecycle.State.DESTROYED
        viewModel.onCleared()
    }

    /**
     * 窗口中可见的view发生变化时
     */
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)

        Log.i(TAG, "onWindowVisibilityChanged: $visibility")

        if (visibility == VISIBLE) {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else if (visibility == GONE || visibility == INVISIBLE) {
            registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    /**
     * 主动调用setVisible时或者Activity生命周期onPause、onResume时触发
     */
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        Log.i(TAG, "onVisibilityChanged: $visibility")
    }

    /**
     * 窗口中包含的view获取或失去焦点时
     */
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)

        Log.i(TAG, "onWindowFocusChanged: $hasWindowFocus")
    }

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    abstract fun layoutId(): Int
    abstract val variableId: Int
    abstract fun setDataToView(vm: DATA)
    abstract fun createViewModel(parent: LinearLayout): DATA

    companion object {
        private const val TAG = "BaseView"
    }
}
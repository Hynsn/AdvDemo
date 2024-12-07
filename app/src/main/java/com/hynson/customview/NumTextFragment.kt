package com.hynson.customview

import android.animation.Animator
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hynson.R
import com.hynson.customview.weight.NumSpan
import com.hynson.databinding.FragCustomviewNumtextBinding
import com.fastdroid.ktbase.BaseFragment

class NumTextFragment : BaseFragment<FragCustomviewNumtextBinding, CustomViewVM>(), View.OnClickListener {
    private val annimListener = AnimatorCounter(1000L)

    override val layoutId: Int = R.layout.frag_customview_numtext

    override fun getVm(provider: ViewModelProvider) = provider.get(CustomViewVM::class.java)

    override fun bindView() {
        bind.btnAppend.setOnClickListener(this)
        bind.btnDel.setOnClickListener(this)
        bind.btnShow.setOnClickListener(this)
        bind.btnHide.setOnClickListener(this)
        bind.btnPlay1.setOnClickListener(this)
        bind.btnPause.setOnClickListener(this)
        updateAlarmMinuteView(mBuilder.toString())
    }

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {

    }

/*    fun click(v: View) {
        Log.i(CustomViewActivity.TAG, "click: ")
        when (v.id) {
            R.id.btn_del -> {
                bind.ntText.del()
                if (mBuilder.isNotEmpty()) {
                    mBuilder.deleteCharAt(mBuilder.length - 1)
                    updateAlarmMinuteView(mBuilder.toString())
                }
            }
            R.id.btn_append -> {
                val max = 57
                val min = 48
                val random = (Math.random() * (max - min) + min).toInt()
                bind.ntText.appCode(random)
                mBuilder.appendCodePoint(random)
                if (mBuilder.length > numbLen) {
                    mBuilder.deleteCharAt(0)
                }
                updateAlarmMinuteView(mBuilder.toString())
            }
            R.id.btn_show -> translateVisibility(bind.ntText, true, true)
            R.id.btn_hide -> translateVisibility(bind.ntText, false, false)
        }
    }*/

    private fun translateVisibility(view: View, visible: Boolean, downup: Boolean) {
        // 下降 fY -0.5f tY 0
        // 上升 fY 0 tY -0.5f
        val fY = if (downup) -0.5f else 0.0f
        val tY = if (downup) 0.0f else 0.5f
        val animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, fY,
                Animation.RELATIVE_TO_SELF, tY
        )
        animation.duration = 400
        view.startAnimation(animation)
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    private val mBuilder = StringBuilder()
    private val numbLen = 4

    private fun updateAlarmMinuteView(text: String) {
        val format =
                String.format("%0${numbLen}d", if (TextUtils.isEmpty(text)) 0 else text.toInt())
        val builder = SpannableStringBuilder()
        builder.append(format).append("\b")
        val lastIndex = leftZeroIndex(format)
        Log.i(TAG, "updateAlarmMinuteView: $format,$lastIndex")
        for (i in format.indices) {
            val hotSpan = NumSpan(
                    context,
                    if (lastIndex < i) R.color.numColor else R.color.zeroColor
            )
            hotSpan.setRightMarginDpValue(10)
            Log.i(TAG, "updateAlarmMinuteView: " + i + "," + (i + 1))
            builder.setSpan(hotSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        bind.tvNumb.text = builder
    }

    private fun leftZeroIndex(str: String): Int {
        var lastIndex = -1
        val chars = str.toCharArray()
        for (i in str.indices) {
            if (chars[i] != '0') break
            lastIndex = i
        }
        return lastIndex
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_del -> {
                bind.ntText.del()
                if (mBuilder.isNotEmpty()) {
                    mBuilder.deleteCharAt(mBuilder.length - 1)
                    updateAlarmMinuteView(mBuilder.toString())
                }
            }
            R.id.btn_append -> {
                val max = 57
                val min = 48
                val random = (Math.random() * (max - min) + min).toInt()
                bind.ntText.appCode(random)
                mBuilder.appendCodePoint(random)
                if (mBuilder.length > numbLen) {
                    mBuilder.deleteCharAt(0)
                }
                updateAlarmMinuteView(mBuilder.toString())
            }
            R.id.btn_show -> translateVisibility(bind.ntText, true, true)
            R.id.btn_hide -> translateVisibility(bind.ntText, false, false)
            R.id.btn_play_1 -> {
//                bind.animationView.repeatCount = 5
                bind.animationView.loop(true)
                bind.animationView.playAnimation()
                annimListener.isFinished = { isFinished, couter ->
                    Log.i(TAG, "isFinished:$isFinished,$couter ")
                    if (isFinished) {
                        bind.animationView.pauseAnimation()
                    }
                }
                bind.animationView.removeAnimatorListener(annimListener)
                bind.animationView.addAnimatorListener(annimListener)
            }
            R.id.btn_pause -> {
                bind.animationView.cancelAnimation()
            }
        }
    }

    /**
     * @param 总时长
     *
     */
    class AnimatorCounter(val durtion: Long) : Animator.AnimatorListener {
        var isFinished: ((finished: Boolean, counter: Int) -> (Unit))? = null
            set(value) {
                start = System.currentTimeMillis()
                field = value
            }
        private var start = System.currentTimeMillis()
        private var counter = 0
        override fun onAnimationStart(p0: Animator) {
            start = System.currentTimeMillis()
            Log.i(TAG, "onAnimationStart")
        }

        override fun onAnimationEnd(p0: Animator) {
            Log.i(TAG, "onAnimationEnd")
        }

        override fun onAnimationCancel(p0: Animator) {
            Log.i(TAG, "onAnimationCancel")
            start = System.currentTimeMillis()
            counter = 0
        }

        override fun onAnimationRepeat(p0: Animator) {
            Log.i(TAG, "onAnimationRepeat")
            counter++
            val now = System.currentTimeMillis()
            val ret = (now - start) >= durtion
            isFinished?.invoke(ret, counter)
        }
    }

    companion object {
        private const val TAG = "animationView"
    }
}
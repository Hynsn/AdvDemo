package com.hynson.detail.chefsnote

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.hynson.BR
import com.hynson.R
import com.hynson.databinding.RecipeViewChefsNoteBinding
import com.hynson.detail.base.BaseView

/**
 * ChefsNoteView
 */
class ChefsNoteView(context: Context, attrs: AttributeSet? = null) :
    BaseView<RecipeViewChefsNoteBinding, ChefsNoteVM>(context, attrs) {
    override fun layoutId() = R.layout.recipe_view_chefs_note
    override val variableId = BR.viewModel
    override fun createViewModel(parent: LinearLayout): ChefsNoteVM {
        return ChefsNoteVM()
    }

    override fun setDataToView(vm: ChefsNoteVM) {
        binding.stvChefsNotes.text = vm.notes
        binding.stvChefsNotes.setOnClickListener {
            vm.title.value = "这是动态更新的标题"
            // binding.tvChefsNotes.visibility = GONE
            //showBottomDialog()
        }
        Log.i("TAG", "setDataToView: ${vm},${vm.title}")
        vm.title.observe(this) {
            binding.tvChefsNotes.text = it
        }
    }

    /**
     * 自定义Dialog实现
     */
    private fun showBottomDialog() {
        val dialog = Dialog(context, R.style.DialogTheme).apply {
            val view = View.inflate(context, R.layout.dialog_bottom, null)
            setContentView(view)
        }
        dialog.window?.apply {
            setGravity(Gravity.BOTTOM) //设置弹出位置
            setWindowAnimations(R.style.main_menu_animStyle) //设置弹出动画
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ) //设置对话框大小
        }

        dialog.show()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val edit = dialog.findViewById<EditText>(R.id.edt_name)
            edit.requestFocus()
            (context.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edit, 0)
        }, 200)

        dialog.findViewById<View>(R.id.tv_take_photo).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.tv_take_pic).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.tv_cancel).setOnClickListener { dialog.dismiss() }
    }
}
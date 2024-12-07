package com.hynson.shortcut

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.fastdroid.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityShortcutBinding
import com.hynson.main.MainActivity
import com.hynson.webview.WebviewActivity

/**
 * Author: Hynsonhou
 * Date: 2022/6/1 13:33
 * Description: 设置页
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/6/1   1.0       首次创建
 */
class ShortcutActivity : BaseActivity<ActivityShortcutBinding>() {
    override fun getLayout() = R.layout.activity_shortcut

    override fun bindView() {
        binding.btnAdd.setOnClickListener {
            val intent = ShortcutUtil.getLaunchIntent(
                this,
                WebviewActivity::class.java,
                "CS158",
                "12:34:56:78"
            )
            val ret = ShortcutUtil.addShortcut(
                this,
                "112",
                R.drawable.ic_launcher_background,
                "test",
                intent
            )
            Toast.makeText(this, "创建结果$ret", Toast.LENGTH_SHORT).show()
        }
        binding.btnDel.setOnClickListener {
            ShortcutUtil.delShortCut(this, MainActivity::class.java, "112", "test")
        }
        binding.btnQuery.setOnClickListener {
            testShortCut()
        }
        binding.btnOppo.setOnClickListener {
            startActivity(oppoApi(this))
        }
    }

    private fun oppoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        //OPPO R9S 6.0.1,由于OPPO手机在"应用列表-xx应用-权限"下的权限列表并没有创建快捷方式一项，只能到"权限隐私-创建快捷方式"中设置。
        intent.setClassName(
            "com.oppo.launcher",
            "com.oppo.launcher.shortcut.ShortcutSettingsActivity"
        )
        if (hasActivity(
                context,
                intent
            )
        ) return intent
        intent.component = ComponentName(
            "com.color.safecenter",
            "com.color.safecenter.permission.PermissionManagerActivity"
        )
        return intent
    }

    private fun hasActivity(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        return packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        ).size > 0
    }

    private fun testShortCut() {
        val intent =
            ShortcutUtil.getLaunchIntent(this, MainActivity::class.java, "CS158", "12:34:56:78")
        ShortcutUtil.hasShortCut(this, "112", "test") { ret, err ->
            if (ret) {
                /*val update = ShortcutUtil.updateShortCut(this, "112", R.drawable.ic_launcher_background, "test1", intent)
                Toast.makeText(this, "修改结果${update},已经存在", Toast.LENGTH_SHORT).show()*/
            } else {
                /*
                val ret = ShortcutUtil.addShortcut(this, "112", R.drawable.ic_launcher_background, "test", intent)
                Toast.makeText(this, "创建结果$ret", Toast.LENGTH_SHORT).show()
                if (granted) {

                } else {
                    Toast.makeText(this, "未授权权限", Toast.LENGTH_SHORT).show()
                }*/
            }
            err?.let {
                Toast.makeText(this, "${it.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG = ShortcutActivity::class.java.simpleName
    }
}
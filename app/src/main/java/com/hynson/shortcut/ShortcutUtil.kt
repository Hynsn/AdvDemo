package com.hynson.shortcut

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.hynson.shortcut.Const.TAG


/**
 * Author: Hynsonhou
 * Date: 2022/8/9 15:19
 * Description: 快捷方式
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/8/9   1.0       首次创建
 */
object ShortcutUtil {

    fun addShortcut(
        context: Context,
        id: String,
        icon: Int,
        title: String,
        intent: Intent
    ): Boolean {
        val shortcut = ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(title)
            .setIcon(IconCompat.createWithResource(context, icon))
            .setIntent(intent)
            .build()
        val bundle = Bundle().apply {
            putString(Const.EXTRA_SHORTCUT_ID, id)
            putString(Const.EXTRA_SHORTCUT_LABEL, title)
        }
        val sender = IntentSenderHelper.getDefaultIntentSender(
            context,
            ShortcutBroadcastReceiver.ACTION, ShortcutBroadcastReceiver::class.java, bundle
        )
        val isReqPinShortcut = ShortcutManagerCompat.isRequestPinShortcutSupported(context)
        when {
            /*isReqPinShortcut && (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) -> {
                val addIntent = Intent(Const.ACTION_INSTALL_SHORTCUT).apply {
                    putExtra("duplicate", false)
                    putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
                    putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon))
                    putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent)
                }
                context.sendBroadcast(addIntent)
                return true
            }*/
            isReqPinShortcut -> {
//                val granted =
//                    (ContextCompat.checkSelfPermission(
//                        context,
//                        "com.android.launcher.permission.INSTALL_SHORTCUT"
//                    ) == PackageManager.PERMISSION_GRANTED)
//                Log.i(TAG, "isReqPinShortcut ==>$granted")
                val ret = ShortcutManagerCompat.requestPinShortcut(context, shortcut, sender)
                Log.i(TAG, "isReqPinShortcut ==>$ret")
                return ret
            }
            else -> {
                return false
            }
        }
    }

    fun updateShortCut(
        context: Context,
        id: String,
        icon: Int,
        title: String,
        intent: Intent
    ): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            /*val addIntent = Intent(Const.ACTION_INSTALL_SHORTCUT).apply {
                    putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
                    putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon))
                    putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent)
                }
                context.sendBroadcast(addIntent)*/
            false
        } else {
            val shortcut = ShortcutInfoCompat.Builder(context, id)
                .setShortLabel(title)
                .setIcon(IconCompat.createWithResource(context, icon))
                .setIntent(intent)
                .build()
            ShortcutManagerCompat.enableShortcuts(context, arrayListOf(shortcut))
            ShortcutManagerCompat.updateShortcuts(context, arrayListOf(shortcut))
        }
    }

    /**
     * 是否已创建快捷方式
     * @param result 是否有快捷方式、是否有权限、异常信息
     *
     */
    fun hasShortCut(
        context: Context,
        id: String,
        title: String,
        result: ((Boolean, Exception?) -> (Unit))
    ) {
        val isReqPinShortcut =
            ShortcutManagerCompat.isRequestPinShortcutSupported(context) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
        val shortCuts =
            ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_PINNED)
        if (shortCuts.isNotEmpty() || isReqPinShortcut) {
            var hasShortcut = false
            shortCuts.find {
                it.id == id
            }?.let {
                hasShortcut = true
            }
            result.invoke(hasShortcut, null)
        } else {
            val resolver = context.contentResolver
            for (s in getCheckPackages(context)) {
                try {
                    val url = Uri.parse("content://${s}.settings/favorites?notify=true")
                    val cursor = resolver.query(
                        url,
                        arrayOf("title", "iconResource"),
                        "title=?",
                        arrayOf(title.trim()),
                        null
                    )
                    cursor?.run {
                        result.invoke((count > 0), null)
                        return
                    }
                } catch (e: Exception) {

                    e.printStackTrace()
                    result.invoke(false, e)
                }
            }
            result.invoke(false, null)
        }
    }

    private fun getCheckPackages(context: Context): Array<String> {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val res = context.packageManager.resolveActivity(intent, 0)
        val pkgName = res?.activityInfo?.packageName
        return if (pkgName.isNullOrEmpty()) {
            arrayOf(Const.LAUNCHER_SETTINGS)
        } else {
            arrayOf(Const.LAUNCHER_SETTINGS, pkgName)
        }
    }


    /**
     * 删除快捷方式
     */
    fun delShortCut(context: Context, clz: Class<*>, id: String, title: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            val mainIntent = Intent(Intent.ACTION_MAIN).apply {
                setClass(context.applicationContext, clz)
                addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val delIntent = Intent(Const.ACTION_UNINSTALL_SHORTCUT).apply {
                putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
                putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent)
            }
            context.sendBroadcast(delIntent)
        } else {
            ShortcutManagerCompat.disableShortcuts(
                context,
                arrayListOf(id),
                "$title has been removed."
            )
        }
    }

    fun getLaunchIntent(
        context: Context,
        clz: Class<*>,
        configModel: String,
        macId: String
    ): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(context, clz)
//            setClass(context.applicationContext, clz)
            addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            putExtra("duplicate", false) //不允许重复创建
            putExtra("configModule", configModel)
            putExtra("macID", macId)
        }
    }

    private fun matchFlags(): Int {
        return ShortcutManagerCompat.FLAG_MATCH_PINNED or ShortcutManagerCompat.FLAG_MATCH_CACHED or ShortcutManagerCompat.FLAG_MATCH_DYNAMIC
    }
}
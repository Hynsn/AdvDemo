package com.hynson.main

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.content.pm.ShortcutManagerCompat.FLAG_MATCH_PINNED
import androidx.core.graphics.drawable.IconCompat
import com.hynson.R
import com.hynson.shortcut.Const
import com.hynson.shortcut.IntentSenderHelper
import com.hynson.shortcut.NormalCreateBroadcastReceiver

/**
 * Author: Hynsonhou
 * Date: 2022/8/4 16:54
 * Description: 快捷方式工具类
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/8/4   1.0       首次创建
 */
object ShortCut {
    fun add(context: Context) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.mysite.example.com/")
        )
        val shortcut = ShortcutInfoCompat.Builder(context, "id1")
            .setShortLabel("Website")
            .setLongLabel("Open the website")
            .setIcon(IconCompat.createWithResource(context, R.drawable.icon_delete))
            .setIntent(intent)
            .build()
        val bundle = Bundle()
        bundle.putString(Const.EXTRA_ID, shortcut.id)
        bundle.putString(Const.EXTRA_LABEL, shortcut.shortLabel.toString())
        val sender = IntentSenderHelper.getDefaultIntentSender(context,NormalCreateBroadcastReceiver.ACTION,NormalCreateBroadcastReceiver::class.java,bundle)
        //val sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT).intentSender
        ShortcutManagerCompat.getShortcuts(context, FLAG_MATCH_PINNED).forEach {
            Log.i(TAG,"ShortcutInfoCompat->${it.shortLabel}")
        }
        ShortcutManagerCompat.getShortcuts(context.applicationContext, matchFlags()).find { it.id == shortcut.id }?.let {
            Toast.makeText(context, "已经存在${shortcut.shortLabel}快捷方式 支持修改", Toast.LENGTH_SHORT).show()
            return
        }
        val isRequestPinShortcut = ShortcutManagerCompat.isRequestPinShortcutSupported(context)
        if (isRequestPinShortcut) {
            val ret = ShortcutManagerCompat.requestPinShortcut(context, shortcut, sender)
            Toast.makeText(context, "支持请求快捷方式:${ret}", Toast.LENGTH_SHORT).show()

//            ShortcutManagerCompat.createShortcutResultIntent(context,shortcut)
        } else {
            Toast.makeText(context, "不支持请求快捷方式", Toast.LENGTH_SHORT).show()
        }
    }

    fun matchFlags() : Int{
        return ShortcutManagerCompat.FLAG_MATCH_PINNED or ShortcutManagerCompat.FLAG_MATCH_CACHED or ShortcutManagerCompat.FLAG_MATCH_DYNAMIC
    }

    val TAG = ShortCut::class.simpleName
}
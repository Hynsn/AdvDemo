package com.hynson.shortcut

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.hynson.shortcut.Const.TAG

/**
 * Author: Hynsonhou
 * Date: 2022/8/9 15:19
 * Description: 广播接收
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/8/9   1.0       首次创建
 */
class ShortcutBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION == intent.action) {
            val id = intent.getStringExtra(Const.EXTRA_SHORTCUT_ID)
            val label = intent.getStringExtra(Const.EXTRA_SHORTCUT_LABEL)
            Log.w(TAG, "onReceive: id = $id, label = $label")
            if(label.isNullOrEmpty() || id.isNullOrEmpty()){
                return
            }
            ShortcutUtil.hasShortCut(context,id,label) { ret, err ->
                Toast.makeText(context,"create result:$ret",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ACTION = "com.vesync.shortcut.create"
    }
}
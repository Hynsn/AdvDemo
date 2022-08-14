package com.hynson.shortcut

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle

/**
 * Author: Hynsonhou
 * Date: 2022/8/9 15:19
 * Description: 快捷方式
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2022/8/9   1.0       首次创建
 */
object IntentSenderHelper {
    fun getDefaultIntentSender(context: Context, action: String): IntentSender {
        return PendingIntent.getBroadcast(
            context, 0, Intent(action),
            PendingIntent.FLAG_ONE_SHOT
        ).intentSender
    }

    fun getDefaultIntentSender(
        context: Context,
        action: String,
        clz: Class<*>,
        bundle: Bundle?
    ): IntentSender {
        val intent = Intent(action)
        intent.component = ComponentName(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_ONE_SHOT).intentSender
    }
}
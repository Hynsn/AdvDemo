package com.hynson.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat

class DownloadNotification(val context: Context) {

    private var notify: NotificationCompat.Builder? = null

    private fun getNotificationManager(): NotificationManager {
        return context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun init(
        title: String,
        text: String? = null,
        channelId: String,
        channelName: String,
        notifyId: Int? = null
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(context.applicationInfo.icon)
        builder.setContentTitle(title)
        builder.setAutoCancel(false)
        builder.setOngoing(true)
        builder.setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级
        builder.setContentText(text)

        // Android8.0及以后的方式
        if (Build.VERSION.SDK_INT >= 26) {
            // 创建通知渠道
            val notificationChannel: NotificationChannel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableLights(false) //关闭闪光灯
            notificationChannel.enableVibration(false) //关闭震动
            notificationChannel.setSound(null, null) //设置静音
            getNotificationManager().createNotificationChannel(notificationChannel)
        }
        notify = builder
        if (notifyId != null) {
            notify(notifyId)
        }
    }

    fun setProgress(max: Int, progress: Int, indeterminate: Boolean) {
        notify?.setProgress(max, progress, indeterminate)
    }

    fun setContentTitle(title: String) {
        notify?.setContentText(title)
    }

    fun setContentText(text: String) {
        notify?.setContentText(text)
    }

    fun notify(id: Int) {
        getNotificationManager().notify(id, notify?.build())
    }

    fun cancel(id: Int) {
        getNotificationManager().cancel(id)
    }

    companion object {
        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "download_channel"
        const val CHANNEL_NAME = "DownloadChannel"
    }
}
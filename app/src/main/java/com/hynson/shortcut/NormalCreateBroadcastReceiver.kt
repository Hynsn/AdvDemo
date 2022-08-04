package com.hynson.shortcut

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NormalCreateBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i(TAG, "onReceive: $action")
        if (ACTION == action) {
            val id = intent.getStringExtra(Const.EXTRA_ID)
            val label = intent.getStringExtra(Const.EXTRA_LABEL)
            Log.i(TAG, "Shortcut normal create callback," +
                    " id = $id, label = $label")
            if (id != null && label != null) {
                Toast.makeText(context," id = $id, label = $label",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ACTION = "com.shortcut.core.normal_create"
        val TAG = NormalCreateBroadcastReceiver::class.simpleName
    }
}
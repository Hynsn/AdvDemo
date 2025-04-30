package com.hynson.mvvm

import android.os.Bundle
import android.util.Log
import com.fastdroid.ktbase.BaseActivity
import com.hynson.databinding.ActivityDialogBinding

class DialogActivity: BaseActivity<ActivityDialogBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "onCreate: ")
    }

    companion object{
        const val TAG = "DialogActivity"
    }
}
package com.hynson.nfc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.fastdroid.ktbase.BaseMvvmActivity
import com.hynson.databinding.ActivityNfcBinding

class NFCActivity : BaseMvvmActivity<ActivityNfcBinding, NFCViewModel>() {

    override fun onPause() {
        super.onPause()
        NFCUtil.disableForegroundDispatch(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent: ${intent.action}")
        NFCUtil.handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (NFCUtil.isEnabled()) {
            Log.d(TAG, "NFC is enabled")
        } else {
            NFCUtil.openNFCSettings(this)
            Log.d(TAG, "NFC is disabled")
        }
    }


    override fun getVm(provider: ViewModelProvider) = provider[NFCViewModel::class.java]

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        super.initData(owner, savedInstanceState)

        NFCUtil.init(this)

        bind.btnRead.setOnClickListener {
            NFCUtil.enableForegroundDispatch(this, true)
        }
        bind.btnWrite.setOnClickListener {
            NFCUtil.enableForegroundDispatch(this, false)
        }
        val key = KEY.fitByteArray(32)
        bind.btnEncrypt.setOnClickListener {
            val ret = AESUtil.encrypt("lat=125.125403&lng=-116.124653", key)
            Log.i(TAG, "decrypt: ${ret}")
        }
        bind.btnDecrypt.setOnClickListener {
            val text = AESUtil.decrypt("z83k7mz9ue9aJo6umDBaz+lyRjI49+KwopbXrs/+PwM=", key)
            Log.i(TAG, "decrypt: ${text}")
        }
    }

    companion object {
        private const val TAG = "NFCActivity"
        private const val KEY = "ZW5kcmlkZVdpdGhORkM="
    }
}
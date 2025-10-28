package com.hynson.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.fastdroid.ktbase.BaseMvvmActivity
import com.hynson.databinding.ActivityNfcBinding

class NFCActivity : BaseMvvmActivity<ActivityNfcBinding, NFCViewModel>() {
    private val messageParse: ((List<NdefMessage>) -> (Unit)) = {
        for (message in it) {
            val message = NdefMessageParser.parse(message)
            Log.i(TAG, "message: $message")
            for (record in message) {
                if (record is UriRecord) {
                    val geo = record.uri.query
                    Log.i(TAG, "URI:geo ${geo}")
                    val key = "ZW5kcmlkZVdpdGhORkM=".fitByteArray(32)
                    if (!geo.isNullOrEmpty()) {
                        val test = AESUtil.decrypt(geo, key)
                        Log.i(TAG, "URI:ase ${test}")
                    }
                } else if (record is TextRecord) {
                    Log.i(TAG, "Text: ${record.text}")
                }
                Log.i(TAG, "record: $record")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        NFCUtil.disableForegroundDispatch(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent: ${intent.action}")
        val uid = NFCUtil.getUid(intent)
        if (uid != null) {
            val pwdPair = AESUtil.generatePwdPair(uid)
            val key = "ZW5kcmlkZVdpdGhORkM=".fitByteArray(32)
            val newNdefRecord = mutableListOf<NdefRecord>()
            for (record in NFCUtil.ndefRecords) {
                val url = record.toUri()
                // action=endride&lat=125.215403&lng=-135.20025
                val start = "action=endride&lat=125.215403&lng=-135.20025"
                val data = "$start&cardId=${uid.toHexString()}"
                val geo = AESUtil.encrypt(data, key)
                val newUrl = "${url.toString().substringBefore('?')}?${geo}"
                Log.i(TAG, "newUrl: $newUrl")
                newNdefRecord.add(NdefRecord.createUri(newUrl))
            }
            NFCUtil.handleIntent(
                intent,
                newNdefRecord.toTypedArray(),
                pwdPair,
                messages = messageParse
            )
        }
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

    @OptIn(ExperimentalStdlibApi::class)
    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        super.initData(owner, savedInstanceState)

        NFCUtil.init(this)

        bind.btnRead.setOnClickListener {
            NFCUtil.enableForegroundDispatch(this)
        }
        bind.btnWrite.setOnClickListener {
            NFCUtil.enableForegroundDispatch(
                this,
                record = NdefRecord.createUri("veo://hynson.com"),
                action = NFCUtil.WRITE
            )
        }
        val key = KEY.fitByteArray(32)
        bind.btnEncrypt.setOnClickListener {
            val ret = AESUtil.encrypt("lat=125.125403&lng=-116.124653", key)
            Log.i(TAG, "decrypt: ${ret}")
        }
        bind.btnDecrypt.setOnClickListener {
            val oldUrl =
                "https://veo.go.link/llt4N?thfnkaJWlZ7h4USoW/asrwcNZlbUga2t8PBr5/77rqxHGpOHliFCFFhQOeokFNLywfxI4Pgmt2nQTZgWwtRJwJqWIwM/xmvEGDZvxkTBMYY=".toUri()
            val geo = oldUrl.query
            if (geo != null) {
                val text = AESUtil.decrypt(geo, key)
                Log.i(TAG, "decrypt: ${text}")
            }
        }
        bind.btnSetpwd.setOnClickListener {
            val message = NdefRecord.createUri("https://veo.go.link/llt4N?params=")
            NFCUtil.enableForegroundDispatch(
                this,
                message,
                action = NFCUtil.WRITE_WITH_PWD
            )
        }
        bind.btnDeletepwd.setOnClickListener {
            NFCUtil.enableForegroundDispatch(this, action = NFCUtil.CLEAR_PWD)
        }
    }

    companion object {
        private const val TAG = "NFCActivity"
        private const val KEY = "ZW5kcmlkZVdpdGhORkM="
    }
}
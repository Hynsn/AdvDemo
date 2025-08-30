package com.hynson.remoteconfig

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.hynson.R


object RemoteConfigManager {
    private const val TAG = "RemoteConfigManager"
    private const val KEY_UNIT = "weight_unit"
    private val remoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    fun fetchAndActivate() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val units = remoteConfig.getString(KEY_UNIT)
                Log.i(TAG, "fetchAndActivate: $units")
            }
            else{
                Log.i(TAG, "fetchAndActivate: error")
            }
        }
    }
}
package com.hynson.wifi;

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.SoftApConfiguration
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Build
import android.os.Handler
import android.util.Log


class WifiIotPlugin(context: Context) {

    companion object{
        private const val TAG = "WifiIotPlugin"
    }

    private var localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_DISABLED

    private val moWiFiAPManager: WifiApManager = WifiApManager(context)

    private val moWiFi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var apReservation: LocalOnlyHotspotReservation? = null

    @SuppressLint("MissingPermission")
    fun setWiFiAPEnabled(enabled:Boolean) {
        //**//** Using LocalOnlyHotspotCallback when setting WiFi AP state on API level >= 29  *//**//*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val result = moWiFiAPManager.setWifiApEnabled(null, enabled)
            Log.i(TAG, "setWiFiAPEnabled: $result")
        } else {
            if (enabled) {
                localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_ENABLING
                moWiFi.startLocalOnlyHotspot(
                    object : LocalOnlyHotspotCallback() {
                        override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                            super.onStarted(reservation)
                            apReservation = reservation
                            localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_ENABLED
                            Log.i(TAG, "setWiFiAPEnabled: true")
                        }

                        override fun onStopped() {
                            super.onStopped()
                            apReservation?.close()
                            apReservation = null
                            localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_DISABLED
                            Log.d(WifiIotPlugin::class.java.simpleName, "LocalHotspot Stopped.")
                        }

                        override fun onFailed(reason: Int) {
                            super.onFailed(reason)
                            apReservation?.close()
                            apReservation = null
                            localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_FAILED
                            Log.d(
                                WifiIotPlugin::class.java.simpleName,
                                "LocalHotspot failed with code: $reason"
                            )
                            Log.i(TAG, "setWiFiAPEnabled: false")
                        }
                    },
                    Handler()
                )
            } else {
                localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_DISABLING
                if (apReservation != null) {
                    apReservation?.close()
                    apReservation = null
                    Log.i(TAG, "setWiFiAPEnabled: true")
                } else {
                    Log.e(
                        WifiIotPlugin::class.java.simpleName,
                        "Can't disable WiFi AP, apReservation is null."
                    )
                    Log.i(TAG, "setWiFiAPEnabled: false")
                }
                localOnlyHotspotState = WIFI_AP_STATE.WIFI_AP_STATE_DISABLED
            }
        }
    }

    fun showWritePermissionSettings(force: Boolean) {
        moWiFiAPManager.showWritePermissionSettings(force)
    }

    fun getWiFiAPSSID(): String? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val oWiFiConfig: WifiConfiguration = moWiFiAPManager.getWifiApConfiguration()

            if (oWiFiConfig.SSID != null) {
                return oWiFiConfig.SSID
            }
            Log.e(TAG, "Exception [getWiFiAPSSID] SSID not found")
        } else {
            if (apReservation != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    val wifiConfiguration: WifiConfiguration? = apReservation?.wifiConfiguration
                    if (wifiConfiguration != null) {
                        return wifiConfiguration.SSID
                    } else {
                        Log.e(
                            TAG,
                            "Exception [getWiFiAPSSID] Security type is not WifiConfiguration.KeyMgmt.None or WifiConfiguration.KeyMgmt.WPA2_PSK"
                        )
                    }
                } else {
                    val softApConfiguration: SoftApConfiguration? = apReservation?.softApConfiguration
                    return softApConfiguration?.ssid
                }
            } else {
                Log.e(TAG, "Exception [getWiFiAPSSID] Hotspot is not enabled.")
            }
        }
        return null
    }

    fun isWiFiAPEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                return moWiFiAPManager.isWifiApEnabled
            } catch (e: SecurityException) {
                Log.e(WifiIotPlugin::class.java.simpleName, e.message, null)
                return false
            }
        } else {
            return apReservation != null
        }
    }
}
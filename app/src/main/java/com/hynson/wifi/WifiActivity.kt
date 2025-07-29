package com.hynson.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.app.ActivityCompat
import com.fastdroid.ktbase.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityWifiBinding

class WifiActivity: BaseActivity<ActivityWifiBinding>(), OnClickListener {

    private val permissions by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
    }

    private fun checkPermissions(context: Context): Boolean {
        for (s in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    s
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private val wifiApManager by lazy { WifiIotPlugin(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.i(TAG, "onCreate: ${wifiApManager.wifiApConfiguration.SSID}")
        bind.btnAp.setOnClickListener(this)
        wifiApManager.setWiFiAPEnabled(true)

//        wifiApManager.showWritePermissionSettings(true)
/*        if (checkPermissions(this)){
            wifiApManager.setWiFiAPEnabled(true)
        }
        else{
            ActivityCompat.requestPermissions(this,permissions,200)
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 200){
            var result = true
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED){
                    result = false
                }
            }
            if (result){
                wifiApManager.setWiFiAPEnabled(true)
            }
        }
    }

    companion object{
        const val TAG = "WifiActivity"
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_ap -> {
                Log.i(TAG, "onCreate: ${wifiApManager.getWiFiAPSSID()}")
            }
        }
    }
}
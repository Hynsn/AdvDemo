package com.hynson.activityresult

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fastdroid.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityGetresultBinding

class GetResultActivity : BaseActivity<ActivityGetresultBinding>() {
    var hasCamera = false
    var requestCounter = 0

    override fun getLayout() = R.layout.activity_getresult

    override fun bindView() {
        binding.btnCheck.setOnClickListener {
            val ret = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            Toast.makeText(this, "checkSelfPermission: $ret", Toast.LENGTH_SHORT).show()
        }
        val launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                requestCounter++
                when {
                    isGranted -> {
                        tolog("申请成功！")
                    }

                    !isGranted && requestCounter == 1 -> {
                        tolog("首次拒绝失败！")
                    }

                    else -> {
                        tolog("第二次申请失败跳转")
//                        gotoDetailsSettings(this)
                    }
                }

                Toast.makeText(this, "RequestPermission: $isGranted", Toast.LENGTH_SHORT).show()
            }
        val launcherMultiple =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
                tolog(map.toString())
//                Toast.makeText(this, "RequestPermission: $isGranted", Toast.LENGTH_SHORT).show()
            }
        binding.btnRequest.setOnClickListener {
            launcher.launch(Manifest.permission.CAMERA)
//            launcherMultiple.launch(
//                arrayOf(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            )
        }
        binding.btnGotosettings.setOnClickListener {
            gotoDetailsSettings(this)
        }
        binding.cbCamera.isChecked = hasPermission(this)
        binding.cbCamera.apply {
            setOnClickListener {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
//        requestCounter = 0
        hasCamera = hasPermission(this)
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun tolog(log: String) {
        binding.tvLog.text = log
    }

    private fun gotoDetailsSettings(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        tolog("code:$requestCode,${permissions[0]},${grantResults[0]}")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val TAG = "GetResultActivity"
    }
}


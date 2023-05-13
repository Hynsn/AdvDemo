package com.hynson.customview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.base.base.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityCustomviewBinding

class CustomViewActivity : BaseActivity<ActivityCustomviewBinding>() {
    override fun getLayout() = R.layout.activity_customview

    override fun bindView() {
        var start = R.id.customViewFragment
        intent.extras?.run {
            start = getInt(EXTRA_KEY_START_RES)
        }

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController.apply {
            val navGraph = navInflater.inflate(R.navigation.nav_customview)
            navGraph.setStartDestination(start)
            setGraph(navGraph, null)
        }
        NavigationUI.setupActionBarWithNavController(this, navController = navController)
    }

    companion object {
        private const val EXTRA_KEY_START_RES = "startDestination"

        fun start(context: Context, startId: Int) {
            val bundle = Bundle().apply {
                putInt(EXTRA_KEY_START_RES, startId)
            }
            val starter = Intent(context, CustomViewActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }
    }
}
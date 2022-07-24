package com.hynson.navigation;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.base.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityNavigationBinding;

public class NavigationActivity extends BaseActivity<ActivityNavigationBinding> {
    final String TAG = NavigationActivity.class.getSimpleName();

    private ActivityVM naviVM;
    @Override
    protected int getLayout() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void bindView() {
        naviVM = new ViewModelProvider(this).get(ActivityVM.class);
        Log.i(TAG, "bindView: "+System.identityHashCode(naviVM));

        naviVM.getName().postValue("我是传过来的");
//        EditText
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        naviVM.getName().observe(this,s -> {
            Log.i(TAG, "bindView: "+s);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i(TAG, "onSupportNavigateUp: ");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp();
    }
}

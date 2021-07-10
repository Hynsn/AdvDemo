package com.example.navigation;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityNavigationBinding;
import com.example.navigation.detail.DetailFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends BaseActivity<ActivityNavigationBinding> {
    final String TAG = NavigationActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void bindView() {
//        EditText
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i(TAG, "onSupportNavigateUp: ");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp();
    }
}

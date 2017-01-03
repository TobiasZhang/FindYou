package com.tt.findyou;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tt.findyou.ui.fragment.SettingsFragment;

/**
 * Created by TT on 2016/12/22.
 */
public class SettingActivity extends AppCompatActivity {
    public static Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_setting);

        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft .add(R.id.container,settingsFragment)
                .commit();
    }
}

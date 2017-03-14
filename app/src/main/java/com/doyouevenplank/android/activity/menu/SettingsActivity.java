package com.doyouevenplank.android.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;

import butterknife.ButterKnife;

public class SettingsActivity extends DoYouEvenPlankActivity {

    public static void start(Context caller) {
        Intent intent = new Intent(caller, SettingsActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.settings);
    }

}

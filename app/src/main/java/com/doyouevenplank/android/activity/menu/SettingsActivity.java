package com.doyouevenplank.android.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.SharedPreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.should_show_progress_bar_in_video_activity_switch) Switch mShouldShowVideoActivityProgressBarSwitch;

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

        mShouldShowVideoActivityProgressBarSwitch.setChecked(SharedPreferencesManager.getInstance(this).getShouldShowProgressBarInVideoActivity());
        mShouldShowVideoActivityProgressBarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferencesManager.getInstance(SettingsActivity.this).setShouldShowProgressBarInVideoActivity(isChecked);
            }
        });
    }

}

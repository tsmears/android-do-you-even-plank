package com.doyouevenplank.android.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.doyouevenplank.android.R;

public class RequiresNetworkActivity extends DoYouEvenPlankActivity {

    public static void start(Context caller) {
        Intent intent = new Intent(caller, RequiresNetworkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requires_network);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // We don't want users to be able to escape this screen, so we slurp up this event
        return;
    }

    public void goToNetworkSettings(View view) {
        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        finish();
    }

}

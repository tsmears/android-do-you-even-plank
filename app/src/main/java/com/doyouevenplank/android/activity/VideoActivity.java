package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;

import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;

public class VideoActivity extends DoYouEvenPlankActivity {

    public static void start(Context caller) {
        Intent intent = new Intent(caller, VideoActivity.class);
        caller.startActivity(intent);
    }

}

package com.doyouevenplank.android.app;

import android.app.Application;

import com.doyouevenplank.android.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

public class DoYouEvenPlankApp extends Application {

    public static final boolean IS_PROD = !BuildConfig.DEBUG;

    public static final String YOUTUBE_API_KEY = "AIzaSyDjPkbaxgKE86qsTg-t7Z04ufIya7yAlRE";

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}

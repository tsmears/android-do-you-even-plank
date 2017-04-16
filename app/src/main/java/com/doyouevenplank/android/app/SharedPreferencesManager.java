package com.doyouevenplank.android.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String HAS_SHOWN_COUNTDOWN_INFO_POPUP = "has_shown_countdown_info_popup_key";
    private static final String SHOULD_SHOW_PROGRESS_BAR_IN_VIDEO_ACTIVITY = "should_show_progress_bar_in_video_activity_key";

    private static SharedPreferencesManager sInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPreferencesManager(Context applicationContext) {
        mSharedPreferences = applicationContext.getSharedPreferences(Config.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public boolean getHasShownCountdownInfoPopup() {
        return mSharedPreferences.getBoolean(HAS_SHOWN_COUNTDOWN_INFO_POPUP, false);
    }
    public void setHasShownCountdownInfoPopup() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(HAS_SHOWN_COUNTDOWN_INFO_POPUP, true);
        editor.apply();
    }

    public boolean getShouldShowProgressBarInVideoActivity() {
        return mSharedPreferences.getBoolean(SHOULD_SHOW_PROGRESS_BAR_IN_VIDEO_ACTIVITY, false);
    }
    public void setShouldShowProgressBarInVideoActivity(boolean shouldShow) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SHOULD_SHOW_PROGRESS_BAR_IN_VIDEO_ACTIVITY, shouldShow);
        editor.apply();
    }

}

package com.doyouevenplank.android.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String HAS_SHOWN_COUNTDOWN_INFO_POPUP = "has_shown_countdown_info_popup_key";

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

}

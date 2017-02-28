package com.doyouevenplank.android.app;

import com.doyouevenplank.android.model.Session;

public class SessionManager {

    private static SessionManager sInstance;

    private Session mSession;

    private SessionManager() {

    }

    public static synchronized SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

}

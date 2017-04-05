package com.doyouevenplank.android.db;

import android.provider.BaseColumns;

public final class HistoryContract {

    private HistoryContract() {}

    public static class HistoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_VIDEO_ID = "videoId";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_DURATION_SECONDS = "durationSeconds";

    }
}
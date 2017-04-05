package com.doyouevenplank.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doyouevenplank.android.util.StringUtils;

import java.util.Date;

public class HistoryDbAccessor {

    private static HistoryDbAccessor sInstance;

    private HistoryDbHelper mHistoryDbHelper;

    private HistoryDbAccessor(Context context) {
        mHistoryDbHelper = new HistoryDbHelper(context.getApplicationContext());
    }

    public static synchronized HistoryDbAccessor getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HistoryDbAccessor(context);
        }
        return sInstance;
    }

    /**
     * "getters"
     */
    public Cursor getHistoryItems() {
        SQLiteDatabase db = mHistoryDbHelper.getReadableDatabase();

        String[] projection = {
                HistoryContract.HistoryEntry._ID,
                HistoryContract.HistoryEntry.COLUMN_NAME_VIDEO_ID,
                HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP,
                HistoryContract.HistoryEntry.COLUMN_NAME_START_TIME_SECONDS,
                HistoryContract.HistoryEntry.COLUMN_NAME_DURATION_SECONDS,
        };

        String selection = null;
        String[] selectionArgs = new String[]{};
        String groupBy = null;
        String having = null;
        String sortOrder =
                HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                HistoryContract.HistoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                sortOrder
        );
        return cursor;
    }

    /**
     * "setters"
     */
    public long insertHistoryItem(String videoId, Date timestamp, int durationSeconds) {
        SQLiteDatabase db = mHistoryDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VIDEO_ID, videoId);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, StringUtils.dateToIsoString(timestamp));
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_DURATION_SECONDS, durationSeconds);

        long newRowId = db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values);
        return newRowId;
    }

}

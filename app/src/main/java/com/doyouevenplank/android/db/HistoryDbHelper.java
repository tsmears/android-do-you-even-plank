package com.doyouevenplank.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "History.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryContract.HistoryEntry.TABLE_NAME + " (" +
                    HistoryContract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_VIDEO_ID + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " TEXT," + // SQLite doesn't have a datetime type, so we store as ISO 8601 strings
                    HistoryContract.HistoryEntry.COLUMN_NAME_START_TIME_SECONDS + " INTEGER," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_DURATION_SECONDS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryContract.HistoryEntry.TABLE_NAME;

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // We don't need to do anything here until we need a db migration. Hopefully never
    }

}

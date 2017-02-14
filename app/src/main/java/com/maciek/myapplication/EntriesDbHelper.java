package com.maciek.myapplication;


import static com.maciek.myapplication.DbConstants.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EntriesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE1 + " TEXT, " +
                    COLUMN_IMAGE2 + " TEXT, " +
                    COLUMN_USEREMAIL + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_ISSENT + " INT, " +
                    COLUMN_LATITUDE + " TEXT, "+
                    COLUMN_LONGITUDE + " TEXT);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public EntriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("Database", "Creating DB");
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

}

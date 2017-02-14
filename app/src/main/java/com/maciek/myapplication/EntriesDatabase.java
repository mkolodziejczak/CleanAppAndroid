package com.maciek.myapplication;


import static com.maciek.myapplication.DbConstants.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class EntriesDatabase {

    private EntriesDbHelper entriesDbHelper;

    public EntriesDatabase(Context context) {
        this.entriesDbHelper = new EntriesDbHelper(context);
    }

    public int addEntry(Entry entry) {


        try {


            int id= this.getEntriesNumber()+1;
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, id);
            values.put(COLUMN_CATEGORY, entry.getCategory());
            values.put(COLUMN_DESCRIPTION, entry.getDescription());
            values.put(COLUMN_IMAGE1, entry.getImage1());
            values.put(COLUMN_IMAGE2, entry.getImage2());
            values.put(COLUMN_USEREMAIL, entry.getUserEmail()+"");
            values.put(COLUMN_DATE, entry.getDate());
            values.put(COLUMN_ISSENT, entry.getIsSent());
            values.put(COLUMN_LATITUDE, entry.getLatitude());
            values.put(COLUMN_LONGITUDE, entry.getLongitude());

            SQLiteDatabase db2 = entriesDbHelper.getWritableDatabase();


            db2.insert(TABLE_NAME, null, values);

            return id;
        } finally {
            Log.v("Database", "Closing");
        }
    }

    public void setEntrySent(int updateId) {
        SQLiteDatabase db = entriesDbHelper.getWritableDatabase();

        try {
            ContentValues newValues = new ContentValues();
            newValues.put(COLUMN_ISSENT, 1);

            db.update(TABLE_NAME, newValues, COLUMN_ID + "=" + updateId, null);

        } finally {
            db.close();
        }
    }

    public Entry getEntry(int position) {
        SQLiteDatabase db = entriesDbHelper.getReadableDatabase();

        try {
            String[] projection = { COLUMN_CATEGORY, COLUMN_DESCRIPTION, COLUMN_DATE, COLUMN_ISSENT, COLUMN_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE};

            String sortOrder = COLUMN_DATE + " DESC";

            Cursor cursor = db.query(TABLE_NAME,  // The table to query
                    projection,                   // The columns to return
                    null,                         // The columns for the WHERE clause
                    null,                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    sortOrder                     // The sort order
            );

            return getEntryFromCursor(position, cursor);
        } finally {
            db.close();
        }
    }

    public Entry getEntry(int position, String whereColumn, String[] whereValues) {
        SQLiteDatabase db = entriesDbHelper.getReadableDatabase();

        try {
            String[] projection = { COLUMN_CATEGORY, COLUMN_DESCRIPTION, COLUMN_DATE, COLUMN_ISSENT, COLUMN_ID};

            String sortOrder = COLUMN_DATE + " DESC";

            Cursor cursor = db.query(TABLE_NAME,  // The table to query
                    projection,                   // The columns to return
                    whereColumn,                         // The columns for the WHERE clause
                    whereValues,                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    sortOrder                     // The sort order
            );

            return getEntryFromCursor(position, cursor);
        } finally {
            db.close();
        }
    }

    private Entry getEntryFromCursor(int position, Cursor cursor) {
        try {
            cursor.moveToPosition(position);

            String category = cursor.getString(0);
            String description = cursor.getString(1);
            String date = cursor.getString(2);
            int issent = cursor.getInt(3);
            int id = cursor.getInt(4);
            String latitude = cursor.getString(5);
            String longitude = cursor.getString(6);

            cursor.close();

            return new Entry(category, description, date, null, issent, id, latitude, longitude);
        } finally {
            cursor.close();
        }
    }

    public int getEntriesNumber() {
        SQLiteDatabase db = entriesDbHelper.getReadableDatabase();

        try {
            return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, null, null);
        } finally {
            db.close();
        }
    }
}

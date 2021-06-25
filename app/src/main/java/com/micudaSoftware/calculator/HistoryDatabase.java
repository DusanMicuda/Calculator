package com.micudaSoftware.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

public class HistoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "History.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "History";
    private static final String COLUMN_NAME_ID = "ID";
    private static final String COLUMN_NAME_MATH = "Math";
    private static final String COLUMN_NAME_RESULT = "Result";

    public HistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_MATH + " TEXT," +
                    COLUMN_NAME_RESULT + " FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String math, Float result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_MATH, math);
        values.put(COLUMN_NAME_RESULT, result);
        long newRowId = db.insert(TABLE_NAME, null, values);
        return newRowId != -1;
    }

    private Cursor getData(String COLUMN_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME, null);
    }

    public ArrayList<String> getMaths() {
        Cursor data = getData(COLUMN_NAME_MATH);
        ArrayList<String> math =  new ArrayList<>();
        while (data.moveToNext()) {
            math.add(data.getString(data.getColumnIndexOrThrow(COLUMN_NAME_MATH)));
        }
        data.close();
        return math;
    }

    public ArrayList<Float> getResults() {
        Cursor data = getData(COLUMN_NAME_RESULT);
        ArrayList<Float> result =  new ArrayList<>();
        while (data.moveToNext()) {
            result.add(data.getFloat(data.getColumnIndexOrThrow(COLUMN_NAME_RESULT)));
        }
        data.close();
        return result;
    }
}

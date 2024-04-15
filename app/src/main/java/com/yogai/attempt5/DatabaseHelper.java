package com.yogai.attempt5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YogaApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSE_TABLE = "CREATE TABLE Pose (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pose_name TEXT," +
                "pose_details TEXT," +
                "benefits TEXT" +
                ")";
        String CREATE_PROGRESS_TABLE = "CREATE TABLE Progress (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pose_id INTEGER," +
                "pose_performed_count INTEGER," +
                "date_time TEXT," +
                "time_performed INTEGER," +
                "set_reps INTEGER," +
                "accuracy REAL," +
                "FOREIGN KEY(pose_id) REFERENCES Pose(id)" +
                ")";

        db.execSQL(CREATE_POSE_TABLE);
        db.execSQL(CREATE_PROGRESS_TABLE);
    }
    public void addPoseDuration(int poseId, int poseDuration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pose_id", poseId);
        contentValues.put("time_performed", 100);
        db.insert("Progress", null, contentValues);
        db.close();
    }
    public String getPoseDuration(int poseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time_performed FROM Progress WHERE pose_id = ?", new String[]{String.valueOf(poseId)});
        if (cursor != null && cursor.moveToFirst()) {
            String poseDuration = cursor.getString(0);
            cursor.close();
            return poseDuration;
        }
        return null;
    }
    public long getTotalPoseDuration() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time_performed FROM Progress", null);
        long totalPoseDuration = 0;
        if (cursor.moveToFirst()) {
            do {
                long timePerformed = cursor.getLong(0);
                totalPoseDuration += timePerformed;
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(null,"Total Pose Duration: " + totalPoseDuration);
        return totalPoseDuration;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Pose");
        db.execSQL("DROP TABLE IF EXISTS Progress");
        onCreate(db);
    }
}

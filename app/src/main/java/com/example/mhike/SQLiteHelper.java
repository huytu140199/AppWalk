package com.example.mhike;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "MHike";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Hike";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_PARKING = "parking";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_WEATHER = "weather";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_OBSERVE = "observe";
    private static final String COLUMN_OBSERVE_DATE = "observe_date";
    private static final String COLUMN_NOTE_OTHER = "note_other";
    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_POSITION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LENGTH + " REAL (100), " +
                COLUMN_PARKING + " INTEGER (10), " +
                COLUMN_IMAGE + " TEXT (255), " +
                COLUMN_WEATHER + " INTEGER (10), " +
                COLUMN_LEVEL + " INTEGER (10), " +
                COLUMN_NOTE + " TEXT (255), " +
                COLUMN_OBSERVE + " TEXT (255), " +
                COLUMN_OBSERVE_DATE + " TEXT (255), " +
                COLUMN_NOTE_OTHER + " TEXT (255));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addHike(Hike hike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, hike.getName());
        cv.put(COLUMN_POSITION, hike.getPosition());
        cv.put(COLUMN_DATE, hike.getDate());
        cv.put(COLUMN_LENGTH, hike.getLength());
        cv.put(COLUMN_PARKING, hike.getParking());
        cv.put(COLUMN_IMAGE, hike.getImage());
        cv.put(COLUMN_WEATHER, hike.getWeather());
        cv.put(COLUMN_LEVEL, hike.getLevel());
        cv.put(COLUMN_NOTE, hike.getNote());
        cv.put(COLUMN_OBSERVE, hike.getObserve());
        cv.put(COLUMN_OBSERVE_DATE, hike.getObserve_date());
        cv.put(COLUMN_NOTE_OTHER, hike.getNote_other());

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor getData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
           cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateHike(Hike hike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, hike.getName());
        cv.put(COLUMN_POSITION, hike.getPosition());
        cv.put(COLUMN_IMAGE, hike.getImage());
        cv.put(COLUMN_NOTE, hike.getNote());
        cv.put(COLUMN_LEVEL, hike.getLevel());
        cv.put(COLUMN_WEATHER, hike.getWeather());
        cv.put(COLUMN_OBSERVE, hike.getObserve());
        cv.put(COLUMN_OBSERVE_DATE, hike.getObserve_date());
        cv.put(COLUMN_NOTE_OTHER, hike.getNote_other());
        cv.put(COLUMN_PARKING, hike.getParking());
        cv.put(COLUMN_LENGTH, hike.getLength());
        cv.put(COLUMN_DATE, hike.getDate());

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{String.valueOf(hike.getId())});
        if(result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteHike(String rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{rowId});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}

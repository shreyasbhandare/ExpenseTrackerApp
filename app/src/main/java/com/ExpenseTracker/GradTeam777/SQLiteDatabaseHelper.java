package com.ExpenseTracker.GradTeam777;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vinya on 12/2/2016.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "OCRData.db";
    public static final String TABLE_NAME = "BillInfo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "Date" ;
    public static final String COLUMN_AMOUNT =  "Amount";
    public static final String COLUMN_IMAGE =  "Image";

    public SQLiteDatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);

    }


    // OnCreate method. Called when helper is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {


        // Note: do not use AUTO INCREMENT on a primary key integer
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_DATE + " TEXT, " + COLUMN_AMOUNT + " DOUBLE, " + COLUMN_IMAGE + " TEXT);");
        System.out.println("Database is created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    // Custom method to insert stuff
    public void insertEntry(String date, double amount, String image){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COLUMN_DATE,date);
        content.put(COLUMN_AMOUNT,amount);
        content.put(COLUMN_IMAGE,image);
        db.insert(TABLE_NAME,null,content);
    }

    // Custom method to return entries
    public Cursor getEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return cursor;
    }

}

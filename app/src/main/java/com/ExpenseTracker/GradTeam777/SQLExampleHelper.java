package com.ExpenseTracker.GradTeam777;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by shreyas on 12/2/16.
 */

public class SQLExampleHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ExampleDB.db";
    public static final String TABLE_NAME = "data_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME =  "month";
    public static final String COLUMN_VALUE = "value";


    // Superclass constructor that requires the application context.
    // Note: you can modify it and pass your own DB name.
    // However, I didn't do it that way. I left it embedded in the helper.
    public SQLExampleHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    // OnCreate method. Called when helper is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Note: CREATE TABLE IF NOT EXISTS might be better, right?
        // Otherwise, run risk of deleting your table.
        // Think about that whne doing your HW.

        // Note: do not use AUTO INCREMENT on a primary key integer
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_NAME + " text, " + COLUMN_VALUE + " text);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do stuff
        // Generally in here you want some event handling for the new versions
        // However, I do not anticipate needing this for your assignment
        // For your project, maybe? Look into it if need be.
    }

    // Custom method to insert stuff
    public void insertEntry(String name, String value){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COLUMN_NAME,name);
        content.put(COLUMN_VALUE,value);
        db.insert(TABLE_NAME,null,content);
    }

    // Custom method to return entries
    public Cursor getEntryById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + id,null);
        return cursor;
    }

    // Custom method to retrieve data
    public ArrayList<String> getEntireColumn(String columnName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        cursor.moveToFirst();

        ArrayList<String> values = new ArrayList<>();

        do{
            String value = (String) cursor.getString(cursor.getColumnIndex(columnName));
            values.add(value);


        }while (cursor.moveToNext());

        return values;
    }

    // Custom SQL query
    public Cursor customQuery(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public void delete()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

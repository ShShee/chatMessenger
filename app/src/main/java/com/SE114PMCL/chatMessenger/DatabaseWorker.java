package com.SE114PMCL.chatMessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseWorker extends SQLiteOpenHelper {
    public DatabaseWorker(Context context) {
        super(context, "Logup.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(username text primary key, password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    //inserting database
    public boolean insert(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        if(ins == -1) return false;
        else return  true;
    }

    //checking if username exists
    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username = ?", new String []{username});
        if(cursor.getCount() > 0) return false;
        else return true;
    }

    //checking the username and password
    public Boolean usernamepassword(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username=? and password=?", new String[]{username, password});
        if(cursor.getCount() > 0) return true;
        else return false;
    }
}


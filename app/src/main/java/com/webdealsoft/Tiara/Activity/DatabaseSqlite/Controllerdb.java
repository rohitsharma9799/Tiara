package com.webdealsoft.Tiara.Activity.DatabaseSqlite;

import  android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
import  android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Shree on 10/22/2016.
 */
public class Controllerdb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="SqliteDB";
    public Controllerdb(Context applicationcontext) {
        super(applicationcontext, DATABASE_NAME, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table to insert data
        String query;
        query = "CREATE TABLE IF NOT EXISTS UserDetails(Id INTEGER PRIMARY KEY AUTOINCREMENT,Stockid VARCHAR,Sku VARCHAR,Item VARCHAR,Metal VARCHAR,Title VARCHAR,Pcs VARCHAR,Gwt DOUBLE,Nwt DOUBLE);";
        db.execSQL(query);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query ;
        query = "DROP TABLE IF EXISTS UserDetails";
        db.execSQL(query);
        onCreate(db);
    }
}
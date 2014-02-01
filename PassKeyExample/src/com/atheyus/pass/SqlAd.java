package com.atheyus.pass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlAd extends SQLiteOpenHelper {

   final String sqlCreate = "CREATE TABLE MasterPass (id INTEGER,key_pass TEXT);";

   public SqlAd(Context context, String name,
                              CursorFactory factory, int version) {
       super(context, name, factory, version);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
       db.execSQL(sqlCreate);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int prev_ver, int next_ver) {
	   db.execSQL("DROP TABLE IF EXISTS MasterPass");
       db.execSQL(sqlCreate);
   }
}
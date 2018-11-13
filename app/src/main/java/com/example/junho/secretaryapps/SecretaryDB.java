package com.example.junho.secretaryapps;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class SecretaryDB {

    private final String dbName = "secretary";
    Activity activity;
    SQLiteDatabase sqliteDB;

    public SecretaryDB(Activity activity){
            this.activity = activity;
    }

    public void dbOpen(){
        sqliteDB = activity.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
    }

    /*public SQLiteDatabase dbOpen(){
        sqliteDB = activity.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        return sqliteDB;
    }*/

    public void dbClose(){
        sqliteDB.close();
    }

}

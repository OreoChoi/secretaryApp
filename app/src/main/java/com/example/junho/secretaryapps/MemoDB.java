package com.example.junho.secretaryapps;

import android.app.Activity;
import android.database.Cursor;

public class MemoDB extends SecretaryDB {


    public MemoDB(Activity activity) {
        super(activity);
    }

    public void memoCreate() {
        sqliteDB.execSQL("create table if not exists memo " +
                "(memoIndex integer primary key autoincrement," + "location VARCHAR(30)," +
                "date VARCHAR(20), content VARCHAR(200), title VARCHAR(50));");

    }

    public void memoInsert(String c1, String c2, String c3, String c4) {
        sqliteDB.execSQL("insert into memo (location, date, content, title) " +
                         "Values ('" + c1 + "','" + c2 + "','" + c3 + "','" + c4 + "');");

    }

    public void memoDelect(int num){
        sqliteDB.execSQL("delete from memo where memoIndex = " + num);

    }

    /* DB의 가장 마지막 Index를 찾습니다. */
    public int lastIndex() {
        int lastIndex;
        Cursor c = sqliteDB.rawQuery("select memoIndex from memo order by memoIndex desc", null);

        c.moveToFirst();
        lastIndex = c.getInt(0);
        c.close();

        return lastIndex;

    }

    public Cursor listCursor(String query){
        Cursor cursor = sqliteDB.rawQuery(query, null);
        return cursor;
    }
}

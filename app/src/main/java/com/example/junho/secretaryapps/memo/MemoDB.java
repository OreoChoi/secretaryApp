package com.example.junho.secretaryapps.memo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.junho.secretaryapps.ApplicationClass;

public class MemoDB{
    private final String dbName = "secretary";
    ApplicationClass applicationClass;
    SQLiteDatabase sqliteDB;

    public MemoDB(ApplicationClass applicationClass){
        this.applicationClass = applicationClass;
        sqliteDB = applicationClass.getSqliteDB();
    }

    /* DB 선언 영역 */

    public void dbClose(){
        sqliteDB.close();
    }

    public void memoCreate() {
        sqliteDB.execSQL("create table if not exists memo " +
                "(memoIndex integer primary key autoincrement," + "location VARCHAR(30)," +
                "date VARCHAR(20), content VARCHAR(200), title VARCHAR(50),color integer);");

    }

    public void memoInsert(String c1, String c2, String c3, String c4, int c5) {
        sqliteDB.execSQL("insert into memo (location, date, content, title, color) " +
                "Values ('" + c1 + "','" + c2 + "','" + c3 + "','" + c4 + "','" + c5+ "');");
    }

    public void memoUpdate(int index, String c1, String c2, int c3) {
        sqliteDB.execSQL("update memo set content = '" + c1 + "', title = '" + c2 + "', color = '" + c3 + "' where memoindex = " + index + ";");
    }

    public void memoDelect(int num) {
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

    public Cursor listCursor(String query) {
        Cursor cursor = sqliteDB.rawQuery(query, null);
        return cursor;
    }
}







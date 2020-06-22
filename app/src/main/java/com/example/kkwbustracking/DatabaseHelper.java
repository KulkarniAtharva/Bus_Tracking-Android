package com.example.kkwbustracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public final static String DATABASE_NAME = "AKMAP Bus Tracker";
    public final static String TABLE_NAME = "UserInfo";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "name";
    public static final String COL_3 = "phone";
    public static final String COL_4 = "licence";
    public static final String COL_5 = "email";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
       // db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+COL_1+"INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+"TEXT,"+COL_3+"TEXT,"+COL_4+"TEXT,"+COL_5+"TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY,NAME TEXT,PHONE TEXT,LICENCE TEXT,EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String phone,String licence_no,String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, name);
        cv.put(COL_3, phone);
        cv.put(COL_4, licence_no);
        cv.put(COL_5, email);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE ID='"+id+"'";
        Cursor  cursor = db.rawQuery(query,null);
        return cursor;
    }
}

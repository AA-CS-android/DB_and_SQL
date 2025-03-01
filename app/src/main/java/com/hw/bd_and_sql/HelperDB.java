package com.hw.bd_and_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.hw.bd_and_sql.Users.*;

public class HelperDB extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME  = "dbexam.db";
    private static final int DATABASE_VERSION = 1;
    private String strCreate, strDelete;

    public HelperDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        strCreate="CREATE TABLE " + TABLE_USERS;
        strCreate+=" ("+Users.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Users.NAME+" TEXT,";
        strCreate+=" "+Users.PASSWORD+" TEXT,";
        strCreate+=" "+Users.AGE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        strDelete = "DROP TABLE IF EXISTS " + TABLE_USERS;
        db.execSQL(strDelete);
        onCreate(db);
    }
}

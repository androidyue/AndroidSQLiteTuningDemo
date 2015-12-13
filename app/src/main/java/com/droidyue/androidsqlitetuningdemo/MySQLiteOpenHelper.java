package com.droidyue.androidsqlitetuningdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by androidyue on 11/29/15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "create table " + TableDefine.TABLE_RECORD + "("
                + TableDefine.COLUMN_RECORD_ID + " integer primary key autoincrement, "
                + TableDefine.COLUMN_INSERT_TIME + " integer" + ")";
        db.execSQL(createTableSQL);

        String createIndexInsertTime = "";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

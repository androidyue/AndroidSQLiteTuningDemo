package com.droidyue.androidsqlitetuningdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase mWritableDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "common");
        mWritableDb = helper.getWritableDatabase();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_insert) {
            insertRecord(mWritableDb);
        } else if (id == R.id.insert_statement) {
            insertWithPreCompiledStatement(mWritableDb);
        } else if (id == R.id.insert_transaction) {
            insertWithTransaction(mWritableDb);
        }

        return super.onOptionsItemSelected(item);
    }




    private void insertRecord(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableDefine.COLUMN_RECORD_ID, 1);
        values.put(TableDefine.COLUMN_INSERT_TIME, 3080869130l);
        db.insert(TableDefine.TABLE_RECORD, null, values);
    }

    private void insertWithPreCompiledStatement(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TableDefine.TABLE_RECORD + "( " + TableDefine.COLUMN_INSERT_TIME + ") VALUES(?)";
        SQLiteStatement  statement = db.compileStatement(sql);
        int count = 0;
        while (count++ < 100) {
            statement.clearBindings();
            statement.bindLong(1, System.currentTimeMillis());
            statement.executeInsert();
        }
    }

    private void insertWithTransaction(SQLiteDatabase db) {
        int count = 0;
        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();
            while (count++ < 100) {
                values.put(TableDefine.COLUMN_INSERT_TIME, System.currentTimeMillis());
                db.insert(TableDefine.TABLE_RECORD, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    private void badQuery(SQLiteDatabase db) {
        db.query(TableDefine.TABLE_RECORD, null, null, null, null, null, null) ;
    }

    private void badQueryWithLoop(SQLiteDatabase db) {
        Cursor cursor = db.query(TableDefine.TABLE_RECORD, new String[]{TableDefine.COLUMN_INSERT_TIME}, null, null, null, null, null) ;
        while (cursor.moveToNext()) {
            long insertTime = cursor.getLong(cursor.getColumnIndex(TableDefine.COLUMN_INSERT_TIME));
        }
        cursor.close();
    }


    private void goodQueryWithLoop(SQLiteDatabase db) {
        Cursor cursor = db.query(TableDefine.TABLE_RECORD, new String[]{TableDefine.COLUMN_INSERT_TIME}, null, null, null, null, null) ;
        int insertTimeColumnIndex = cursor.getColumnIndex(TableDefine.COLUMN_INSERT_TIME);
        while (cursor.moveToNext()) {
            long insertTime = cursor.getLong(insertTimeColumnIndex);
        }
        cursor.close();
    }


}

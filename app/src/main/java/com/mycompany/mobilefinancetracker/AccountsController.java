package com.mycompany.mobilefinancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountsController {

    private AccountsHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public AccountsController(Context c) {
        ourcontext = c;
    }

    public AccountsController open() throws SQLException {
        dbHelper = new AccountsHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String type, String amount, String mlimit) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(AccountsHelper._id, name);
        contentValue.put(AccountsHelper.type, type);
        contentValue.put(AccountsHelper.amount,amount);
        contentValue.put(AccountsHelper.mylimit,mlimit);
        Log.i("Vals", String.valueOf(contentValue));
        database.insert(AccountsHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { AccountsHelper._id, AccountsHelper.type,
                AccountsHelper.amount, AccountsHelper.mylimit};
        Cursor cursor = database.query(AccountsHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getTypes() {
        String[] columns = new String[] {AccountsHelper.type};
        Cursor cursor = database.query(AccountsHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String old_name, String name, String type, String amount, String mlimit) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountsHelper._id, name);
        contentValues.put(AccountsHelper.type, type);
        contentValues.put(AccountsHelper.amount,amount);
        contentValues.put(AccountsHelper.mylimit,mlimit);
        int i = database.update(AccountsHelper.TABLE_NAME, contentValues,
                AccountsHelper._id + " = '" + old_name + "'", null);
        return i;


    }


    public void delete(String name) {
        database.delete(AccountsHelper.TABLE_NAME, AccountsHelper._id + " = '" + name + "'", null);
    }
}

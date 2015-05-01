package com.mycompany.mobilefinancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExpensesController {

    private ExpensesHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public ExpensesController(Context c) {
        ourcontext = c;
    }

    public ExpensesController open() throws SQLException {
        dbHelper = new ExpensesHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String amount, String type, String location,String year, String month, String day) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ExpensesHelper.account, name);
        contentValue.put(ExpensesHelper.amount, amount);
        contentValue.put(ExpensesHelper.type,type);
        contentValue.put(ExpensesHelper.location, location);
        contentValue.put(ExpensesHelper.year, year);
        contentValue.put(ExpensesHelper.month,month);
        contentValue.put(ExpensesHelper.day,day);
        database.insert(ExpensesHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { ExpensesHelper._id, ExpensesHelper.account,
                ExpensesHelper.amount, ExpensesHelper.type, ExpensesHelper.location,
                ExpensesHelper.year, ExpensesHelper.month, ExpensesHelper.day};
        Cursor cursor = database.query(ExpensesHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAccounts() {
        String[] columns = new String[] {ExpensesHelper.account};
        Cursor cursor = database.query(ExpensesHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getTypes() {
        String[] columns = new String[] {ExpensesHelper.type};
        Cursor cursor = database.query(ExpensesHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String id, String name, String amount, String type, String location,String year, String month, String day) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpensesHelper._id, id);
        contentValues.put(ExpensesHelper.account, name);
        contentValues.put(ExpensesHelper.amount, amount);
        contentValues.put(ExpensesHelper.type,type);
        contentValues.put(ExpensesHelper.location, location);
        contentValues.put(ExpensesHelper.year, year);
        contentValues.put(ExpensesHelper.month,month);
        contentValues.put(ExpensesHelper.day,day);
        int i = database.update(ExpensesHelper.TABLE_NAME, contentValues,
                ExpensesHelper._id + " = '" + id + "'", null);
        return i;


    }


    public void delete(String id) {
        database.delete(ExpensesHelper.TABLE_NAME, ExpensesHelper._id + " = '" + id + "'", null);
    }
}

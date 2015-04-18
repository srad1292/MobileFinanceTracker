package com.mycompany.mobilefinancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.ContentHandler;

public class ExpensesHelper extends SQLiteOpenHelper{

    static final String TABLE_NAME = "Expenses";
    static final String _id = "_id";
    static final String account = "account";
    static final String amount = "amount";
    static final String type = "type";
    static final String location = "location";
    static final String year = "year";
    static final String month = "month";
    static final String day = "day";

    static final String DB_NAME = "Expenses.DB";

    // database version
    static final int DB_VERSION = 3;

    // Creating table query


    public ExpensesHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String CREATE_TABLE = "CREATE TABLE Expenses (Name TEXT PRIMARY KEY, Type TEXT, Amount TEXT, Limit TEXT)";
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + account + " TEXT, "
                + amount + " TEXT, " + type + " TEXT, "
                + location + " TEXT, " + year + " TEXT, "
                + month + " TEXT, " + day + " TEXT" + ");";


        db.execSQL(CREATE_EXPENSES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
package com.mycompany.mobilefinancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.ContentHandler;

/**
 * Created by San on 4/6/2015.
 */
public class AccountsHelper extends SQLiteOpenHelper{

    static final String TABLE_NAME = "Accounts";
    static final String _id = "_id";
    static final String type = "type";
    static final String amount = "amount";
    static final String mylimit = "mylimit";

    static final String DB_NAME = "Accounts.DB";

    // database version
    static final int DB_VERSION = 7;

    // Creating table query


    public AccountsHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String CREATE_TABLE = "CREATE TABLE Accounts (Name TEXT PRIMARY KEY, Type TEXT, Amount TEXT, Limit TEXT)";
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + _id + " TEXT PRIMARY KEY, " + type
                + " TEXT, " + amount + " TEXT, " + mylimit + " TEXT" + ");";


        db.execSQL(CREATE_ACCOUNTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
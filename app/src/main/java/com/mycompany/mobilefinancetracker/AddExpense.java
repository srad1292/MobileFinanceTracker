package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class AddExpense extends Activity implements AdapterView.OnItemSelectedListener{
    private String expenseAccount;
    private AccountsController ac;
    private Cursor c;
    private EditText amountIN;
    private EditText locationIN;
    private EditText typeIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        ac = new AccountsController(this);
        ac.open();
        c = ac.fetch();
        Spinner dropdown = (Spinner)findViewById(R.id.account_spinner);
        List<String> items = new ArrayList<String>();
        if (c!=null && c.moveToFirst()){
            do{
                if(!items.contains(c.getString(c.getColumnIndex("_id")))){
                    items.add(c.getString(c.getColumnIndex("_id")));
                }
            }while(c.moveToNext());
        }
        expenseAccount = items.get(0);
        Log.i("zero",expenseAccount);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        amountIN = (EditText) findViewById(R.id.add_expense_amount);
        locationIN = (EditText) findViewById(R.id.add_expense_location);
        typeIN = (EditText) findViewById(R.id.add_expense_type);

    }


    public void onSaveClicked(View view){
        String a = String.valueOf(expenseAccount);
        Log.i("a",a);
        String expenseAmount = String.valueOf(amountIN.getText());
        String expenseLocation = String.valueOf(locationIN.getText());
        String expenseType = String.valueOf(typeIN.getText());

        Intent goingBack = new Intent();

        goingBack.putExtra("exp_account",a);
        goingBack.putExtra("exp_amount",expenseAmount);
        goingBack.putExtra("exp_location",expenseLocation);
        goingBack.putExtra("exp_type",expenseType);
        goingBack.putExtra("value","1");
        setResult(RESULT_OK,goingBack);

        finish();


    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        setResult(RESULT_OK,goingBack);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        expenseAccount = parent.getItemAtPosition(position).toString();
        Log.i("item", expenseAccount);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}



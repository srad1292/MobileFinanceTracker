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
import java.util.Calendar;
import java.util.*;

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
    private EditText yearIN, monthIN, dayIN;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        cal = Calendar.getInstance();
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
        yearIN = (EditText) findViewById(R.id.edit_text_year);
        monthIN = (EditText) findViewById(R.id.edit_text_month);
        dayIN = (EditText) findViewById(R.id.edit_text_day);
        int cal_mon = cal.get(java.util.Calendar.MONTH) + 1;
        yearIN.setText(String.valueOf(cal.get(java.util.Calendar.YEAR)));
        monthIN.setText(String.valueOf(cal_mon));
        dayIN.setText(String.valueOf(cal.get(java.util.Calendar.DATE)));

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
        goingBack.putExtra("year",String.valueOf(yearIN.getText()));
        goingBack.putExtra("month",String.valueOf(monthIN.getText()));
        goingBack.putExtra("day",String.valueOf(dayIN.getText()));
        goingBack.putExtra("value","1");
        c.close();
        setResult(RESULT_OK,goingBack);

        finish();


    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        c.close();
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



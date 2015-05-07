package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class EditExpense extends Activity implements AdapterView.OnItemSelectedListener{
    private String id;
    private String expenseAccount;
    private AccountsController ac;
    private Cursor c;
    private EditText amountIN;
    private EditText locationIN;
    private EditText typeIN;
    private EditText yearIN, monthIN, dayIN;
    private String old_account, old_amount;
    private String expenseAmount;
    private String expenseLocation;
    private String expenseType, o_year, o_month, o_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_expense);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        id = activityThatCalled.getExtras().getString("id");

        expenseAccount = activityThatCalled.getExtras().getString("account");
        old_account = expenseAccount;
        old_amount = activityThatCalled.getExtras().getString("amount");
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(adapter.getPosition(expenseAccount));
        dropdown.setOnItemSelectedListener(this);
        amountIN = (EditText) findViewById(R.id.edit_expense_amount);
        locationIN = (EditText) findViewById(R.id.edit_expense_location);
        typeIN = (EditText) findViewById(R.id.edit_expense_type);
        yearIN = (EditText) findViewById(R.id.edit_text_year);
        monthIN = (EditText) findViewById(R.id.edit_text_month);
        dayIN = (EditText) findViewById(R.id.edit_text_day);

        amountIN.setText(activityThatCalled.getExtras().getString("amount"));
        locationIN.setText(activityThatCalled.getExtras().getString("location"));
        typeIN.setText(activityThatCalled.getExtras().getString("type"));
        yearIN.setText(activityThatCalled.getExtras().getString("year"));
        monthIN.setText(activityThatCalled.getExtras().getString("month"));
        dayIN.setText(activityThatCalled.getExtras().getString("day"));

        expenseAmount = "";
        expenseLocation = "";
        expenseType ="";
        o_year="";
        o_month="";
        o_day="";
    }


    public void onSaveClicked(View view){
        TextView aE = (TextView) findViewById(R.id.amount_error);
        TextView cE = (TextView) findViewById(R.id.category_error);
        TextView lE = (TextView) findViewById(R.id.location_error);
        TextView dE = (TextView) findViewById(R.id.date_error);
        boolean good = true;
        if (amountIN.getText().toString().trim().length() != 0) {
            expenseAmount = String.valueOf(amountIN.getText());
            aE.setVisibility(View.GONE);
        } else {
            good = false;
            aE.setVisibility(View.VISIBLE);
        }

        if (typeIN.getText().toString().trim().length() != 0) {
            expenseType = String.valueOf(typeIN.getText());
            cE.setVisibility(View.GONE);
        } else {
            good = false;
            cE.setVisibility(View.VISIBLE);
        }

        if (locationIN.getText().toString().trim().length() != 0) {
            expenseLocation = String.valueOf(locationIN.getText());
            lE.setVisibility(View.GONE);
        } else {
            good = false;
            lE.setVisibility(View.VISIBLE);
        }


        if (yearIN.getText().toString().trim().length() != 0 && monthIN.getText().toString().trim().length() != 0 && dayIN.getText().toString().trim().length() != 0){
            o_year = String.valueOf(yearIN.getText());
            o_month = String.valueOf(monthIN.getText());
            o_day = String.valueOf(dayIN.getText());
            dE.setVisibility(View.GONE);
        }

        else{
            good = false;
            dE.setVisibility(View.VISIBLE);
        }


        String a = String.valueOf(expenseAccount);

        if(good) {
            Intent goingBack = new Intent();

            goingBack.putExtra("id", id);
            goingBack.putExtra("exp_account", expenseAccount);
            goingBack.putExtra("exp_amount", expenseAmount);
            goingBack.putExtra("exp_location", expenseLocation);
            goingBack.putExtra("exp_type", expenseType);
            goingBack.putExtra("year", o_year);
            goingBack.putExtra("month", o_month);
            goingBack.putExtra("day", o_day);
            goingBack.putExtra("old", old_account);
            goingBack.putExtra("old_amount", old_amount);
            goingBack.putExtra("value", "2");
            ac.close();
            setResult(RESULT_OK, goingBack);

            finish();
        }

    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        ac.close();
        setResult(RESULT_OK,goingBack);

        finish();
    }

    public void onDeleteClicked(View view) {
        Intent goingBack = new Intent();
        goingBack.putExtra("id",id);
        goingBack.putExtra("value","4");
        goingBack.putExtra("old",old_account);
        goingBack.putExtra("old_amount",old_amount);
        ac.close();
        setResult(RESULT_OK,goingBack);

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        expenseAccount = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}



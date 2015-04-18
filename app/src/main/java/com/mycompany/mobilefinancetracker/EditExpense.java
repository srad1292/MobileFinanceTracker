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

import java.util.ArrayList;
import java.util.List;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class EditExpense extends Activity implements AdapterView.OnItemSelectedListener{
    private String id;
    private String year;
    private String month;
    private String day;
    private String expenseAccount = "";
    private AccountsController ac;
    private Cursor c;
    private EditText amountIN;
    private EditText locationIN;
    private EditText typeIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_expense);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        id = activityThatCalled.getExtras().getString("id");
        year = activityThatCalled.getExtras().getString("year");
        month = activityThatCalled.getExtras().getString("month");
        day = activityThatCalled.getExtras().getString("day");
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
        dropdown.setOnItemSelectedListener(this);
        amountIN = (EditText) findViewById(R.id.edit_expense_amount);
        locationIN = (EditText) findViewById(R.id.edit_expense_location);
        typeIN = (EditText) findViewById(R.id.edit_expense_type);
        amountIN.setText(activityThatCalled.getExtras().getString("amount"));
        locationIN.setText(activityThatCalled.getExtras().getString("location"));
        typeIN.setText(activityThatCalled.getExtras().getString("type"));
    }


    public void onSaveClicked(View view){

        String expenseAmount = String.valueOf(amountIN.getText());
        String expenseLocation = String.valueOf(locationIN.getText());
        String expenseType = String.valueOf(typeIN.getText());

        Intent goingBack = new Intent();

        goingBack.putExtra("id",id);
        goingBack.putExtra("exp_account",expenseAccount);
        goingBack.putExtra("exp_amount",expenseAmount);
        goingBack.putExtra("exp_location",expenseLocation);
        goingBack.putExtra("exp_type",expenseType);
        goingBack.putExtra("year",year);
        goingBack.putExtra("month",month);
        goingBack.putExtra("day",day);
        goingBack.putExtra("value","2");

        setResult(RESULT_OK,goingBack);

        finish();


    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        setResult(RESULT_OK,goingBack);
        finish();
    }

    public void onDeleteClicked(View view) {
        Intent goingBack = new Intent();
        goingBack.putExtra("id",id);
        goingBack.putExtra("value","4");
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



package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class AddAccount extends Activity {
    private String accountName;
    private String accountType;
    private String accountAmount;
    private String accountLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        accountName = "";
        accountType="";
        accountAmount ="";
        accountLimit = "";
    }


    public void onSaveClicked(View view){
        EditText nameIN = (EditText) findViewById(R.id.add_account_name);
        TextView nE = (TextView) findViewById(R.id.name_error);
        TextView tE = (TextView) findViewById(R.id.type_error);
        TextView aE = (TextView) findViewById(R.id.amount_error);
        boolean good = true;
        if(nameIN.getText().toString().trim().length() != 0) {
            accountName = String.valueOf(nameIN.getText());
            nE.setVisibility(View.GONE);
        }
        else{
            good = false;
            nE.setVisibility(View.VISIBLE);
        }

        EditText typeIN = (EditText) findViewById(R.id.add_account_type);
        if(typeIN.getText().toString().trim().length() != 0) {
            accountType = String.valueOf(typeIN.getText()).toLowerCase();
            tE.setVisibility(View.GONE);
        }
        else {
            good = false;
            tE.setVisibility(View.VISIBLE);
        }

        EditText amountIN = (EditText) findViewById(R.id.add_account_amount);
        if(amountIN.getText().toString().trim().length() != 0) {
            accountAmount = String.valueOf(amountIN.getText());
            aE.setVisibility(View.GONE);
        }
        else {
            good = false;
            aE.setVisibility(View.VISIBLE);
        }

        EditText limitIN = (EditText) findViewById(R.id.add_account_limit);

        if(limitIN.getText().toString().trim().length() != 0){
            accountLimit = String.valueOf(limitIN.getText());

        }
        else {
            accountLimit = "-1";
        }


        if(good) {
            Intent goingBack = new Intent();

            goingBack.putExtra("acct_name", accountName);
            goingBack.putExtra("acct_type", accountType);
            goingBack.putExtra("acct_amount", accountAmount);
            goingBack.putExtra("acct_limit", accountLimit);
            goingBack.putExtra("value", "1");
            goingBack.putExtra("originalName", "");
            setResult(RESULT_OK, goingBack);

            finish();
        }

    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        setResult(RESULT_OK,goingBack);
        finish();
    }

}



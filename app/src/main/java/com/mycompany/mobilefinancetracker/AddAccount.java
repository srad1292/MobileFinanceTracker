package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class AddAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");

    }


    public void onSaveClicked(View view){
        EditText nameIN = (EditText) findViewById(R.id.add_account_name);
        String accountName = String.valueOf(nameIN.getText());

        EditText typeIN = (EditText) findViewById(R.id.add_account_type);
        String accountType = String.valueOf(typeIN.getText());

        EditText amountIN = (EditText) findViewById(R.id.add_account_amount);
        String accountAmount = String.valueOf(amountIN.getText());

        EditText limitIN = (EditText) findViewById(R.id.add_account_limit);
        String accountLimit = String.valueOf(limitIN.getText());



        Intent goingBack = new Intent();

        goingBack.putExtra("acct_name",accountName);
        goingBack.putExtra("acct_type",accountType);
        goingBack.putExtra("acct_amount",accountAmount);
        goingBack.putExtra("acct_limit",accountLimit);
        goingBack.putExtra("value","1");
        goingBack.putExtra("originalName","");
        setResult(RESULT_OK,goingBack);

        finish();


    }

    public void onCancelClicked(View view){
        Intent goingBack = new Intent();
        goingBack.putExtra("value","3");
        setResult(RESULT_OK,goingBack);
        finish();
    }

}



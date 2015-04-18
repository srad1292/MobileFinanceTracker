package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.mycompany.mobilefinancetracker.R.layout.add_account;


/**
 * Created by San on 3/2/2015.
 */
public class EditAccount extends Activity {
    private String b_name;
    private EditText nameIN;
    private EditText typeIN;
    private EditText amountIN;
    private EditText limitIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        b_name = activityThatCalled.getExtras().getString("name");
        TextView head = (TextView) findViewById(R.id.view_account_heading);
        head.setText(b_name);

        nameIN = (EditText) findViewById(R.id.edit_account_name);
        typeIN = (EditText) findViewById(R.id.edit_account_type);
        amountIN = (EditText) findViewById(R.id.edit_account_amount);
        limitIN = (EditText) findViewById(R.id.edit_account_limit);

        nameIN.setText(b_name);
        typeIN.setText(activityThatCalled.getExtras().getString("type"));
        amountIN.setText(activityThatCalled.getExtras().getString("amount"));
        limitIN.setText(activityThatCalled.getExtras().getString("limit"));
    }


    public void onSaveClicked(View view){

        String accountName = String.valueOf(nameIN.getText());
        String accountType = String.valueOf(typeIN.getText());
        String accountAmount = String.valueOf(amountIN.getText());
        String accountLimit = String.valueOf(limitIN.getText());

        Intent goingBack = new Intent();

        goingBack.putExtra("acct_name",accountName);
        goingBack.putExtra("acct_type",accountType);
        goingBack.putExtra("acct_amount",accountAmount);
        goingBack.putExtra("acct_limit",accountLimit);
        goingBack.putExtra("value","2");
        Log.i("b_name", b_name);
        goingBack.putExtra("originalName",b_name);

        setResult(RESULT_OK, goingBack);

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
        goingBack.putExtra("delName",b_name);
        goingBack.putExtra("value","4");
        setResult(RESULT_OK,goingBack);
        finish();
    }
}



package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

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
    private TextView cur;
    private Double total;
    private ExpensesController ec;
    private Cursor curs;
    private Calendar cal;
    private String cur_year;
    private String cur_month;
    private String exp_year;
    private String exp_month;
    private String cur_account;
    private String yes_amount;
    private String limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        b_name = activityThatCalled.getExtras().getString("name");
        limit = " ";
        total = 0.0;
        cal = Calendar.getInstance();
        cur_year = String.valueOf(cal.get(java.util.Calendar.YEAR));
        int cal_mon = cal.get(java.util.Calendar.MONTH) + 1;
        cur_month = String.valueOf(cal_mon);
        TextView head = (TextView) findViewById(R.id.view_account_heading);
        head.setText(b_name);
        ec = new ExpensesController(this);
        ec.open();
        curs = ec.fetch();
        cur_account = "";
        yes_amount = "";
        cur = (TextView) findViewById(R.id.cur_spent);
        nameIN = (EditText) findViewById(R.id.edit_account_name);
        typeIN = (EditText) findViewById(R.id.edit_account_type);
        amountIN = (EditText) findViewById(R.id.edit_account_amount);
        limitIN = (EditText) findViewById(R.id.edit_account_limit);
        if(activityThatCalled.getExtras().getString("limit")!=null){
            limit = activityThatCalled.getExtras().getString("limit");
        }
        nameIN.setText(b_name);
        typeIN.setText(activityThatCalled.getExtras().getString("type"));
        amountIN.setText(activityThatCalled.getExtras().getString("amount"));
        limitIN.setText(activityThatCalled.getExtras().getString("limit"));
        displayCurrent();
        ec.close();
    }


    public void displayCurrent(){
        if (curs != null && curs.moveToFirst()) {

            //Go through all expenses checking against current account name
            do {
                exp_year = curs.getString(curs.getColumnIndex("year"));
                exp_month = curs.getString(curs.getColumnIndex("month"));
                cur_account = curs.getString(curs.getColumnIndex("account"));
                //Check for if button should be displayed
                if(shouldDisplay()){
                    yes_amount = curs.getString(curs.getColumnIndex("amount"));
                    addToTotal();
                }


            } while (curs.moveToNext());
        }
        setTheText();
    }

    public Boolean shouldDisplay(){
        Boolean should = false;
        if(cur_account.equals(b_name)){
            if(exp_year.equals(cur_year) && exp_month.equals(cur_month)){
                should = true;
            }
        }
        return should;
    }

    public void addToTotal(){
        if(yes_amount.contains(".")){
            int position = yes_amount.indexOf(".");

            String de_st;
            if(yes_amount.startsWith(".")) {
                de_st = yes_amount.substring(1);
            }

            else{
                String v1 = yes_amount.substring(0, position);
                de_st = yes_amount.substring(position+1);
                total = total + Integer.parseInt(v1);
            }

            double dec = (double) Integer.parseInt(de_st);
            BigDecimal bd = new BigDecimal(dec/100.0).setScale(2, RoundingMode.HALF_UP);
            total = total + (bd.doubleValue());
        }
        else{
            total = total + Integer.parseInt(yes_amount);
        }
    }

    public void setTheText(){
        String pre_total = getTotalPrecision(total);


        if(!limit.equals(" ")){
            String pre_percent = getPercentPrecision(total);
            cur.setText("$" + pre_total + " | " + pre_percent + "%");
        }

        else{
            cur.setText("$" + pre_total);
        }
     }

    public String getTotalPrecision(Double val){

        String precision = String.valueOf(val);
        int pre_len = 0;
        int ind = 0;
        String good_precision = "";
        if(precision.contains(".")){
            pre_len = precision.length();
            ind = precision.indexOf('.');
            if(pre_len > (ind+3)){
                good_precision = precision.substring(0,ind+3);
            }
            else{
                good_precision = String.valueOf(val);
            }

        }

        return good_precision;
    }

    public String getPercentPrecision(Double val){
        double percent = 0.0;
        Double val_limit = limitToDoub(limit);
        percent = (val * 100) / val_limit;
        String precision = String.valueOf(percent);
        int pre_len = 0;
        int ind = 0;
        String good_precision = "";
        if(precision.contains(".")){
            pre_len = precision.length();
            ind = precision.indexOf('.');
            if(pre_len > (ind+3)){
                good_precision = precision.substring(0,ind+3);
            }
            else{
                good_precision = String.valueOf(percent);
            }

        }
        return good_precision;
    }

    public Double limitToDoub(String lim){
        Double value = 0.0;
        if(lim.contains(".")){
            int position = lim.indexOf(".");

            String de_st;
            if(lim.startsWith(".")) {
                de_st = lim.substring(1);
            }

            else{
                String v1 = lim.substring(0, position);
                de_st = lim.substring(position+1);
                value = value + Integer.parseInt(v1);
            }

            double dec = (double) Integer.parseInt(de_st);
            BigDecimal bd = new BigDecimal(dec/100.0).setScale(2, RoundingMode.HALF_UP);
            value = value + (bd.doubleValue());
        }
        else{
            value = value + Integer.parseInt(lim);
        }
        return value;
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



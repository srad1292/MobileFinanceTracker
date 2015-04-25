package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by San on 4/18/2015.
 */
public class ExpenseViewer extends Activity {
    private ExpensesController exps;

    private Cursor headingCursor;
    private Cursor e_curs;
    private Double total;
    private LinearLayout.LayoutParams textParams;
    private LinearLayout expenses;
    private Button expDet;
    private LinearLayout.LayoutParams buttonParams;
    private String cur_account;
    private StringBuilder dis_account;
    private Boolean head;
    private List<String> doneAccounts;
    private String start_year, start_month, start_day, end_year, end_month, end_day;
    private String year, month, day, id, account, amount, type, location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_viewer);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
        start_year = activityThatCalled.getExtras().getString("start_year");
        start_month = activityThatCalled.getExtras().getString("start_month");
        start_day = activityThatCalled.getExtras().getString("start_day");
        end_year = activityThatCalled.getExtras().getString("end_year");
        end_month = activityThatCalled.getExtras().getString("end_month");
        end_day = activityThatCalled.getExtras().getString("end_day");
        total = 0.0;
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        expenses = (LinearLayout) findViewById(R.id.linlay);
        expDet = new Button(this);
        buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        exps = new ExpensesController(this);
        exps.open();
        headingCursor = exps.getAccounts();
        e_curs = exps.fetch();
        cur_account = "";
        dis_account = new StringBuilder();
        head = false;
        doneAccounts = new ArrayList<>();
        String id = "";
        String account = "";
        String amount = "";
        String type = "";
        String location = "";
        displayExpensesByAccount();
    }


    public void displayExpensesByAccount(){

        if(headingCursor!=null && headingCursor.moveToFirst()) {
            //Go through the accounts column
            do {
                cur_account = headingCursor.getString(headingCursor.getColumnIndex("account"));
                //Check if that account name has been handled
                if(!doneAccounts.contains(cur_account)) {

                    if (e_curs != null && e_curs.moveToFirst()) {
                        head = false;
                        //Go through all expenses checking against current account name
                        do {
                            year = e_curs.getString(e_curs.getColumnIndex("year"));
                            month = e_curs.getString(e_curs.getColumnIndex("month"));
                            day = e_curs.getString(e_curs.getColumnIndex("day"));
                            //Check for if button should be displayed
                            if(shouldDisplay()){
                                if(!head) {displayHeading();}
                                getVals();
                                displayButton();
                                addToTotal();
                            }


                        } while (e_curs.moveToNext());
                    }
                }
            }while(headingCursor.moveToNext());
        }

        expDet = new Button(this);
        expDet.setText("Done");
        buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        expDet.setLayoutParams(buttonParams);
        expenses.addView(expDet);
        expDet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goingBack = new Intent();
                exps.close();
                goingBack.putExtra("value","1");

                setResult(RESULT_OK,goingBack);

                finish();


            }
        });

    }

    public void displayHeading(){
        doneAccounts.add(cur_account);
        dis_account = new StringBuilder();
        dis_account.append(cur_account.toLowerCase());
        dis_account.setCharAt(0,Character.toUpperCase(dis_account.charAt(0)));
        TextView acctN = new TextView(this);
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        acctN.setLayoutParams(textParams);
        acctN.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        acctN.setText(dis_account.toString());
        acctN.setGravity(Gravity.CENTER);
        expenses.addView(acctN);
        head = true;

    }

    public void displayButton(){
        expDet = new Button(this);

        String buttonText = String.format(location + " " + amount);

        expDet.setText(buttonText);
        buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        expDet.setLayoutParams(buttonParams);
        expenses.addView(expDet);
        final String o_id = id;
        final String o_account = account;
        final String o_amount = amount;
        final String o_type = type;
        final String o_location = location;
        final String o_year = year;
        final String o_month = month;
        final String o_day = day;
        final ExpenseViewer et = this;
        expDet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent editExpenseScreenIntent = new Intent(et, EditExpense.class);
                final int result = 1;
                editExpenseScreenIntent.putExtra("callingActivity", "MainActivity");
                editExpenseScreenIntent.putExtra("id", o_id);
                editExpenseScreenIntent.putExtra("account", o_account);
                editExpenseScreenIntent.putExtra("amount", o_amount);
                editExpenseScreenIntent.putExtra("type", o_type);
                editExpenseScreenIntent.putExtra("location", o_location);
                editExpenseScreenIntent.putExtra("year", o_year);
                editExpenseScreenIntent.putExtra("month", o_month);
                editExpenseScreenIntent.putExtra("day", o_day);
                startActivityForResult(editExpenseScreenIntent, result);

            }
        });
    }

    public void getVals(){
        id = e_curs.getString(e_curs.getColumnIndex("_id"));
        account = e_curs.getString(e_curs.getColumnIndex("account"));
        amount = e_curs.getString(e_curs.getColumnIndex("amount"));
        type = e_curs.getString(e_curs.getColumnIndex("type"));
        location = e_curs.getString(e_curs.getColumnIndex("location"));
    }


    public void addToTotal(){
        if(amount.contains(".")){
            int position = amount.indexOf(".");

            String de_st;
            if(amount.startsWith(".")) {
                de_st = amount.substring(1);
            }

            else{
                String v1 = amount.substring(0, position);
                de_st = amount.substring(position+1);
                total = total + Integer.parseInt(v1);
            }

            double dec = (double) Integer.parseInt(de_st);
            BigDecimal bd = new BigDecimal(dec/100.0).setScale(2, RoundingMode.HALF_UP);
            total = total + (bd.doubleValue());
        }
        else{
            total = total + Integer.parseInt(amount);
        }
    }

    public Boolean shouldDisplay(){
        Boolean should = false;
        if(e_curs.getString(e_curs.getColumnIndex("account")).equals(cur_account)){
            if(year.equals(start_year)){
                if(month.equals(start_month)){
                    if(Integer.parseInt(day) >= Integer.parseInt(start_day)){
                        should = true;
                    }
                }
                else if(Integer.parseInt(month)>Integer.parseInt(start_month)){
                    should = true;
                }
            }
            else if(Integer.parseInt(year) > Integer.parseInt(start_year)&& Integer.parseInt(year) < Integer.parseInt(end_year)){
                should = true;
            }

            else if(year.equals(end_year)){
                if(month.equals(end_month)){
                    if(Integer.parseInt(day) <= Integer.parseInt(end_day)){
                        should = true;
                    }
                }
                else if(Integer.parseInt(month)<Integer.parseInt(start_month)){
                    should = true;
                }
            }
        }
        return should;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onDoneClicked(View view) {
        Intent goingBack = new Intent();
        goingBack.putExtra("value","1");
        setResult(RESULT_OK,goingBack);

        finish();
    }
}

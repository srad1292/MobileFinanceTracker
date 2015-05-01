package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;

/**
 * Created by San on 4/18/2015.
 */
public class Habits extends Activity {

    private ExpensesController exps;
    private Cursor headingCursor;
    private Cursor e_curs;
    private Double total;
    private LinearLayout.LayoutParams textParams;
    private LinearLayout display_area,row;
    private TextView category;
    private TextView total_and_percent;
    private LinearLayout.LayoutParams viewParams;
    private String cur_type;
    private StringBuilder dis_type;
    private Boolean head, done;
    private String start_year, start_month, start_day, end_year, end_month, end_day;
    private String year, month, day, id, account, amount, type, location;
    private int size;
    private List<String> accts, done_types;
    private HashMap<String, Double> perCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up
        exps = new ExpensesController(this);
        //Get values from calling activity
        Intent activityThatCalled = getIntent();
        start_year = activityThatCalled.getExtras().getString("start_year");
        start_month = activityThatCalled.getExtras().getString("start_month");
        start_day = activityThatCalled.getExtras().getString("start_day");
        end_year = activityThatCalled.getExtras().getString("end_year");
        end_month = activityThatCalled.getExtras().getString("end_month");
        end_day = activityThatCalled.getExtras().getString("end_day");
        size = activityThatCalled.getExtras().getInt("size");
        accts = new ArrayList<String>();
        perCat = new HashMap<String, Double>();
        int x;
        if(size==1){
            x = 0;
        }
        else{
            x=1;
        }
        for (int y = x; y < size; y++){
            accts.add(activityThatCalled.getExtras().getString(("account"+y)));
        }
        for (int y = 0; y < size; y++){
            Log.i("Account: ", accts.get(y));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.habits);
        exps.open();
        headingCursor = exps.getTypes();
        e_curs = exps.fetch();
        cur_type = "";
        dis_type = new StringBuilder();
        done_types = new ArrayList<>();
        displayHandler();
        exps.close();

    }

    public void displayHandler(){
        boolean ex = false;
        if(headingCursor!=null && headingCursor.moveToFirst()) {
            //Go through the accounts column
            do {
                cur_type = headingCursor.getString(headingCursor.getColumnIndex("type"));
                //Check if that account name has been handled
                if (!done_types.contains(cur_type)) {

                    if (e_curs != null && e_curs.moveToFirst()) {

                        //Go through all expenses checking against current account name
                        do {
                            year = e_curs.getString(e_curs.getColumnIndex("year"));
                            month = e_curs.getString(e_curs.getColumnIndex("month"));
                            day = e_curs.getString(e_curs.getColumnIndex("day"));
                            //Check for if button should be displayed
                            if (shouldDisplay()) {
                                ex = true;
                                if (!head) {
                                    perCat.put(cur_type,0.0);
                                    done_types.add(cur_type);
                                    head = true;
                                }
                                getVals();
                                addToTypeTypeTotal();
                                addToTotal();
                            }
                        } while (e_curs.moveToNext());
                    }
                }
            } while (headingCursor.moveToNext());

            if (ex) {
                display();
            }
        }
    }

    public Boolean shouldDisplay(){
        Boolean should = false;
        if(e_curs.getString(e_curs.getColumnIndex("type")).equals(cur_type) && accts.contains(e_curs.getString(e_curs.getColumnIndex("account")))){
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

    public void getVals(){
        id = e_curs.getString(e_curs.getColumnIndex("_id"));
        account = e_curs.getString(e_curs.getColumnIndex("account"));
        amount = e_curs.getString(e_curs.getColumnIndex("amount"));
        type = e_curs.getString(e_curs.getColumnIndex("type"));
        location = e_curs.getString(e_curs.getColumnIndex("location"));
    }

    public void addToTypeTypeTotal(){
        Double tempTotal = 0.0;
        if(amount.contains(".")){
            int position = amount.indexOf(".");

            String de_st;
            if(amount.startsWith(".")) {
                de_st = amount.substring(1);
            }

            else{
                String v1 = amount.substring(0, position);
                de_st = amount.substring(position+1);
                tempTotal = tempTotal + Integer.parseInt(v1);
            }

            double dec = (double) Integer.parseInt(de_st);
            BigDecimal bd = new BigDecimal(dec/100.0).setScale(2, RoundingMode.HALF_UP);
            tempTotal = tempTotal + (bd.doubleValue());
        }
        else{
            tempTotal = tempTotal + Integer.parseInt(amount);
        }
        perCat.put(cur_type,perCat.get(cur_type)+tempTotal);
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

    public void display(){
        display_area = (LinearLayout) findViewById(R.id.display_area);
        viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        double percent = 0.0;
        for (HashMap.Entry<String, Double> entry : perCat.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            row = new LinearLayout(this);
            row.setLayoutParams(viewParams);
            category = new TextView(this);
            total_and_percent = new TextView(this);
            category.setLayoutParams(textParams);
            total_and_percent.setLayoutParams(textParams);
            category.setText(key + ": ");
            percent = (value * 100) / total;
            total_and_percent.setText("$" + value.toString() + " | " + String.valueOf(percent) + "%");
            display_area.addView(row);
        }

    }

    public void onDoneClicked(View view) {
        Intent goingBack = new Intent();
        goingBack.putExtra("value","1");
        setResult(RESULT_OK, goingBack);
        exps.close();
        finish();
    }
}

package com.mycompany.mobilefinancetracker;


import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TodaysExpenses extends Fragment {
    private AccountsController accts;
    private ExpensesController exps;
    private View rootView;
    //private Cursor a_curs;
    private Cursor headingCursor;
    private Cursor e_curs;
    private Double total;
    private Calendar cal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.todays_expenses, null);
        cal = Calendar.getInstance();
        //accts = new AccountsController(getActivity());
        //accts.open();
        exps = new ExpensesController(getActivity());
        exps.open();
        headingCursor = exps.getAccounts();
        //a_curs = accts.fetch();
        e_curs = exps.fetch();
        total = 0.0;

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!data.getStringExtra("value").equals("3")) {
            if (data.getStringExtra("value").equals("1")) {
                String a_name = data.getStringExtra("exp_account");
                String e_amount = data.getStringExtra("exp_amount");
                String e_type = data.getStringExtra("exp_type");
                String e_location = data.getStringExtra("exp_location");
                String e_year = String.valueOf(cal.get(Calendar.YEAR));
                String e_month = String.valueOf(cal.get(Calendar.MONTH));
                String e_day = String.valueOf(cal.get(Calendar.DATE));

                if(a_name != null){
                    Log.i("name",a_name);
                }else{
                    Log.i("name","name is null");
                }
                Log.i("year",e_year);
                Log.i("month",e_month);
                Log.i("day",e_day);
                exps.insert(a_name, e_amount, e_type, e_location, e_year, e_month, e_day);

            } else if (data.getStringExtra("value").equals("2")){
                String id = data.getStringExtra("id");
                String a_name = data.getStringExtra("exp_account");
                String e_amount = data.getStringExtra("exp_amount");
                String e_type = data.getStringExtra("exp_type");
                String e_location = data.getStringExtra("exp_location");
                String e_year = data.getStringExtra("year");
                String e_month = data.getStringExtra("month");
                String e_day = data.getStringExtra("day");

                exps.update(id, a_name, e_amount, e_type, e_location, e_year, e_month, e_day);

            } else if (data.getStringExtra("value").equals("4")){
                String del_id = data.getStringExtra("id");
                exps.delete(del_id);
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        total = 0.0;
        Button expDet = new Button(getActivity());
        LinearLayout expenses = (LinearLayout) rootView.findViewById(R.id.explay);


        String dy = String.valueOf(cal.get(Calendar.YEAR));
        String dm = String.valueOf(cal.get(Calendar.MONTH));
        String dd = String.valueOf(cal.get(Calendar.DATE));
        TextView display_date = new TextView(getActivity());
        LinearLayout.LayoutParams textParams;
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        display_date.setLayoutParams(textParams);
        display_date.setText((Integer.parseInt(dm)+1)+"-"+dd+"-"+dy);
        display_date.setGravity(Gravity.CENTER);
        expenses.addView(display_date);


        expDet.setText("Add Expense");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        expDet.setLayoutParams(buttonParams);
        expenses.addView(expDet);

        expDet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addExpenseScreenIntent = new Intent(getActivity(), AddExpense.class);

                final int result = 1;
                addExpenseScreenIntent.putExtra("callingActivity", "MainActivity");
                startActivityForResult(addExpenseScreenIntent, result);
            }
        });


        //a_curs = accts.fetch();
        e_curs = exps.fetch();
        headingCursor = exps.getAccounts();
        String cur_account;

        List<String> doneAccounts = new ArrayList<>();
        if(headingCursor!=null && headingCursor.moveToFirst()) {
            do {
                cur_account = headingCursor.getString(headingCursor.getColumnIndex("account"));
                if(!doneAccounts.contains(cur_account)) {
                    doneAccounts.add(cur_account);
                    TextView acctN = new TextView(getActivity());
                    textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    acctN.setLayoutParams(textParams);
                    acctN.setText(cur_account);
                    acctN.setGravity(Gravity.CENTER);
                    expenses.addView(acctN);
                    if (e_curs != null && e_curs.moveToFirst()) {
                        do {
                            String year = e_curs.getString(e_curs.getColumnIndex("year"));
                            String month = e_curs.getString(e_curs.getColumnIndex("month"));
                            String day = e_curs.getString(e_curs.getColumnIndex("day"));
                            if(e_curs.getString(e_curs.getColumnIndex("account")).equals(cur_account) && year.equals(dy)
                                    && month.equals(dm) && day.equals(dd)) {
                                String id = e_curs.getString(e_curs.getColumnIndex("_id"));
                                String account = e_curs.getString(e_curs.getColumnIndex("account"));
                                String amount = e_curs.getString(e_curs.getColumnIndex("amount"));
                                String type = e_curs.getString(e_curs.getColumnIndex("type"));
                                String location = e_curs.getString(e_curs.getColumnIndex("location"));
                                if(amount.contains(".")){
                                    int position = amount.indexOf(".");
                                    String v1 = amount.substring(0, position);
                                    String v2 = amount.substring(position+1);
                                    total = total + Integer.parseInt(v1);
                                    double dec = (double) Integer.parseInt(v2);
                                    BigDecimal bd = new BigDecimal(dec/100.0).setScale(2,RoundingMode.HALF_UP);
                                    total = total + (bd.doubleValue());
                                }
                                else{
                                    total = total + Integer.parseInt(amount);
                                }
                                expDet = new Button(getActivity());

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
                                expDet.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent editExpenseScreenIntent = new Intent(getActivity(), EditExpense.class);
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
                        } while (e_curs.moveToNext());
                    }
                }
            }while(headingCursor.moveToNext());
        }

        TextView display_total = new TextView(getActivity());
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        display_total.setLayoutParams(textParams);
        display_total.setText("Total: " + String.valueOf(total));
        display_total.setGravity(Gravity.CENTER);
        expenses.addView(display_total);
    }

}

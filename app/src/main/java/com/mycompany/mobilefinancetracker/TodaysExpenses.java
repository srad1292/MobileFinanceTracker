package com.mycompany.mobilefinancetracker;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    private Cursor a_curs;
    private Cursor headingCursor;
    private Cursor e_curs;
    private Double total;
    private Calendar cal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.todays_expenses, null);
        cal = Calendar.getInstance();
        accts = new AccountsController(getActivity());
        //accts.open();
        exps = new ExpensesController(getActivity());

        //exps.open();
        //headingCursor = exps.getAccounts();
        //a_curs = accts.fetch();
        //e_curs = exps.fetch();
        total = 0.0;

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        exps.open();
        accts.open();
        if (!data.getStringExtra("value").equals("3")) {
            if (data.getStringExtra("value").equals("1")) {
                String a_name = data.getStringExtra("exp_account");
                String e_amount = data.getStringExtra("exp_amount");
                String e_type = data.getStringExtra("exp_type");
                String e_location = data.getStringExtra("exp_location");
                String e_year = data.getStringExtra("year");
                String e_month = data.getStringExtra("month");
                String e_day = data.getStringExtra("day");
                exps.insert(a_name, e_amount, e_type, e_location, e_year, e_month, e_day);
                expenseAdded(a_name,e_amount);

            } else if (data.getStringExtra("value").equals("2")){
                String id = data.getStringExtra("id");
                String a_name = data.getStringExtra("exp_account");
                String e_amount = data.getStringExtra("exp_amount");
                String e_type = data.getStringExtra("exp_type");
                String e_location = data.getStringExtra("exp_location");
                String e_year = data.getStringExtra("year");
                String e_month = data.getStringExtra("month");
                String e_day = data.getStringExtra("day");
                String old = data.getStringExtra("old");
                String old_a = data.getStringExtra("old_amount");
                exps.update(id, a_name, e_amount, e_type, e_location, e_year, e_month, e_day);
                if(old.equals(a_name) && !old_a.equals(e_amount)){
                    changeAmount(old,e_amount,old_a);
                }
                else if(!old.equals(a_name) && old_a.equals(e_amount)){
                    changeAccount(a_name,old,old_a,e_amount);
                }
                else if(!old.equals(a_name) && !old_a.equals(e_amount)){
                    changeBoth(a_name,old,old_a,e_amount);
                }


            } else if (data.getStringExtra("value").equals("4")){
                String del_id = data.getStringExtra("id");
                String old = data.getStringExtra("old");
                String old_a = data.getStringExtra("old_amount");
                exps.delete(del_id);
                changeAmount(old,"0.0",old_a);
            }
        }
        exps.close();
        accts.close();

    }


    public void expenseAdded(String account, String amount){
        a_curs = accts.fetch();
        String a_amount = "";
        Boolean found = false;
        if (a_curs != null && a_curs.moveToFirst()) {
            do{
                if (a_curs.getString(a_curs.getColumnIndex("_id")).equals(account)){
                    a_amount = a_curs.getString(a_curs.getColumnIndex("amount"));
                    found = true;
                }
            }while (a_curs.moveToNext() && !found);
        }
        Double cur_amount = sToTotal(a_amount);
        Double exp_amount = sToTotal(amount);
        Double result = cur_amount - exp_amount;
        String new_amount = String.valueOf(result);
        accts.updateAmount(account,a_amount,new_amount);
    }

    public void changeAmount(String account, String new_amount, String old_amount){
        a_curs = accts.fetch();
        String a_amount = "";
        Boolean found = false;
        if (a_curs != null && a_curs.moveToFirst()) {
            do{
                if (a_curs.getString(a_curs.getColumnIndex("_id")).equals(account)){
                    a_amount = a_curs.getString(a_curs.getColumnIndex("amount"));
                    found = true;
                }
            }while (a_curs.moveToNext() && !found);
        }
        Double cur_amount = sToTotal(a_amount);
        Double v1 = sToTotal(new_amount);
        Double v2 = sToTotal(old_amount);
        Double subtract = 0.0;
        Double result = 0.0;
        if (v1 > v2){
            subtract = v1 - v2;
            result = cur_amount - subtract;
        }
        if (v1 < v2){
            subtract = v2 - v1;
            result = cur_amount + subtract;
        }
        String updated = String.valueOf(result);
        accts.updateAmount(account,a_amount,updated);
    }

    public void changeAccount(String account, String old_account,String cur_amount, String new_amount){
        changeAmount(old_account,"0.0",cur_amount);
        changeAmount(account,new_amount,"0.0");
    }

    public void changeBoth(String account, String old_account,String cur_amount, String new_amount){
        changeAmount(old_account,"0.0",cur_amount);
        changeAmount(account,new_amount,"0.0");
    }

    public Double sToTotal(String amount){
        Double total = 0.0;
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
        return total;
    }

    @Override
    public void onPause() {
        super.onPause();
        exps.close();
        accts.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout expenses = (LinearLayout) rootView.findViewById(R.id.explay);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(expenses.getWindowToken(),0);

        exps.open();
        accts.open();
        e_curs = exps.fetch();
        headingCursor = exps.getAccounts();
        total = 0.0;
        Button expDet = new Button(getActivity());



        String dy = String.valueOf(cal.get(Calendar.YEAR));
        int idm = cal.get(Calendar.MONTH)+1;
        String dm = String.valueOf(idm);
        String dd = String.valueOf(cal.get(Calendar.DATE));
        TextView display_date = new TextView(getActivity());
        LinearLayout.LayoutParams textParams;
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        display_date.setLayoutParams(textParams);
        display_date.setText((Integer.parseInt(dm) + 1) + "-" + dd + "-" + dy);
        display_date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        display_date.setTypeface(null, Typeface.BOLD);
        display_date.setGravity(Gravity.CENTER);

        expenses.addView(display_date);


        expDet.setText("Add Expense");
        expDet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        expDet.setBackgroundResource((R.drawable.my_button));
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0,5,0,10);
        expDet.setLayoutParams(buttonParams);
        expenses.addView(expDet);

        expDet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addExpenseScreenIntent = new Intent(getActivity(), AddExpense.class);
                exps.close();
                accts.close();
                final int result = 1;
                addExpenseScreenIntent.putExtra("callingActivity", "MainActivity");
                startActivityForResult(addExpenseScreenIntent, result);
            }
        });


        //a_curs = accts.fetch();

        String cur_account;
        StringBuilder dis_account;
        List<String> doneAccounts = new ArrayList<>();
        if(headingCursor!=null && headingCursor.moveToFirst()) {
            do {
                cur_account = headingCursor.getString(headingCursor.getColumnIndex("account"));
                if(!doneAccounts.contains(cur_account)) {

                    if (e_curs != null && e_curs.moveToFirst()) {
                        Boolean head = false;
                        do {
                            String year = e_curs.getString(e_curs.getColumnIndex("year"));
                            String month = e_curs.getString(e_curs.getColumnIndex("month"));
                            String day = e_curs.getString(e_curs.getColumnIndex("day"));
                            if(e_curs.getString(e_curs.getColumnIndex("account")).equals(cur_account) && year.equals(dy)
                                    && month.equals(dm) && day.equals(dd)) {
                                if(!head) {
                                    doneAccounts.add(cur_account);
                                    dis_account = new StringBuilder();
                                    dis_account.append(cur_account.toLowerCase());
                                    dis_account.setCharAt(0,Character.toUpperCase(dis_account.charAt(0)));

                                    TextView acctN = new TextView(getActivity());
                                    textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    acctN.setLayoutParams(textParams);
                                    acctN.setText(dis_account.toString());
                                    acctN.setGravity(Gravity.CENTER);
                                    acctN.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                                    expenses.addView(acctN);
                                    head = true;
                                }
                                String id = e_curs.getString(e_curs.getColumnIndex("_id"));
                                String account = e_curs.getString(e_curs.getColumnIndex("account"));
                                String amount = e_curs.getString(e_curs.getColumnIndex("amount"));
                                String type = e_curs.getString(e_curs.getColumnIndex("type"));
                                String location = e_curs.getString(e_curs.getColumnIndex("location"));
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
                                    BigDecimal bd = new BigDecimal(dec/100.0).setScale(2,RoundingMode.HALF_UP);
                                    total = total + (bd.doubleValue());
                                }
                                else{
                                    total = total + Integer.parseInt(amount);
                                }
                                expDet = new Button(getActivity());

                                String buttonText = String.format(location + ":\t$" + amount);
                                expDet.setBackgroundResource((R.drawable.my_button));
                                expDet.setText(buttonText);
                                buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                buttonParams.setMargins(0,10,0,10);
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
                                        exps.close();
                                        startActivityForResult(editExpenseScreenIntent, result);

                                    }
                                });
                            }
                        } while (e_curs.moveToNext());
                    }
                }
            }while(headingCursor.moveToNext());
        }


        int pre_len = 0;
        int ind = 0;
        String good_precision = " ";
        String precision = String.valueOf(total);
        if(precision.contains(".")){
            pre_len = precision.length();
            ind = precision.indexOf('.');
            if(pre_len > (ind+3)){
                good_precision = precision.substring(0,ind+3);
            }
            else{
                good_precision = String.valueOf(total);
            }

        }


        TextView display_total = new TextView(getActivity());
        textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0,15,0,0);
        display_total.setLayoutParams(textParams);
        display_total.setText("Total: $" + good_precision);
        display_total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        display_total.setGravity(Gravity.CENTER);
        expenses.addView(display_total);
        exps.close();
        accts.close();
    }

}


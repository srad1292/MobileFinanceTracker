package com.mycompany.mobilefinancetracker;



import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Accounts extends Fragment {

    private AccountsController accounts;
    private View rootView;
    private Cursor curs;
    private Cursor types;
    private String edit_name;
    private String edit_type;
    private String edit_amount;
    private String edit_limit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.accounts, null);
        accounts = new AccountsController(getActivity());
        //accounts.open();
        edit_name = "";
        edit_type = "";
        edit_amount = "";
        edit_limit = "";
        //curs = accounts.fetch();
        //types = accounts.getTypes();
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        accounts.open();
        if (!data.getStringExtra("value").equals("3")) {
            if (data.getStringExtra("value").equals("1")) {
                String a_name = String.format("%8s", data.getStringExtra("acct_name"));
                String a_type = String.format("%10s", data.getStringExtra("acct_type"));
                String a_amount = String.format("%7s", data.getStringExtra("acct_amount"));
                String a_limit = String.format("%10s", data.getStringExtra("acct_limit"));
                accounts.insert(a_name.trim(), a_type.trim(), a_amount.trim(), a_limit.trim());

            } else if (data.getStringExtra("value").equals("2")){

                edit_name = data.getStringExtra("acct_name");
                edit_type = data.getStringExtra("acct_type");
                edit_amount = data.getStringExtra("acct_amount");
                edit_limit = data.getStringExtra("acct_limit");
                String old_name = data.getStringExtra("originalName");
                accounts.update(old_name, edit_name, edit_type, edit_amount, edit_limit);

            } else if (data.getStringExtra("value").equals("4")){
                String del_name = data.getStringExtra("delName");
                accounts.delete(del_name);
            }
        }
        accounts.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        accounts.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        accounts.open();
        curs = accounts.fetch();
        types = accounts.getTypes();
        Button acctDet = new Button(getActivity());
        LinearLayout accts = (LinearLayout) rootView.findViewById(R.id.linlay);
        acctDet.setText("Add Account");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        acctDet.setLayoutParams(buttonParams);
        //acctDet.setBackgroundColor(Color.CYAN);
        acctDet.setBackgroundResource((R.drawable.my_button));
        accts.addView(acctDet);

        acctDet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent addAccountScreenIntent = new Intent(getActivity(),AddAccount.class);

                final int result = 1;
                addAccountScreenIntent.putExtra("callingActivity","MainActivity");
                startActivityForResult(addAccountScreenIntent,result);
            }
        });
        //accounts.close();
        //accounts = new AccountsController(getActivity());

        types = accounts.getTypes();
        String cur_type;
        StringBuilder dis_type;
        LinearLayout.LayoutParams textParams;
        List<String> doneTypes = new ArrayList<>();
        if(types!=null && types.moveToFirst()) {
            do {
                cur_type = types.getString(types.getColumnIndex("type"));
                if(!doneTypes.contains(cur_type)) {
                    doneTypes.add(cur_type);
                    dis_type = new StringBuilder();
                    dis_type.append(cur_type.toLowerCase());
                    dis_type.setCharAt(0, Character.toUpperCase(dis_type.charAt(0)));

                    TextView acctType = new TextView(getActivity());
                    textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    acctType.setLayoutParams(textParams);
                    acctType.setText(dis_type.toString());
                    acctType.setGravity(Gravity.CENTER);
                    acctType.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

                    accts.addView(acctType);
                    curs = accounts.fetch();
                    if (curs != null && curs.moveToFirst()) {
                        do {
                            if (curs.getString(curs.getColumnIndex("type")).equals(cur_type)) {
                                String n = curs.getString(curs.getColumnIndex("_id"));
                                String t = curs.getString(curs.getColumnIndex("type"));
                                String a = curs.getString(curs.getColumnIndex("amount"));
                                String l = curs.getString(curs.getColumnIndex("mylimit"));
                                acctDet = new Button(getActivity());

                                String bText = String.format(n + " " + a);

                                acctDet.setText(bText);
                                acctDet.setBackgroundResource((R.drawable.my_button));
                                buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                buttonParams.setMargins(0,10,0,10);
                                acctDet.setLayoutParams(buttonParams);

                                accts.addView(acctDet);
                                final String o_name = n;
                                final String o_type = t;
                                final String o_amount = a;
                                final String o_limit = l;
                                acctDet.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent editAccountScreenIntent = new Intent(getActivity(), EditAccount.class);
                                        final int result = 1;
                                        editAccountScreenIntent.putExtra("callingActivity", "MainActivity");
                                        editAccountScreenIntent.putExtra("name", o_name);
                                        editAccountScreenIntent.putExtra("type", o_type);
                                        editAccountScreenIntent.putExtra("amount", o_amount);
                                        editAccountScreenIntent.putExtra("limit", o_limit);

                                        startActivityForResult(editAccountScreenIntent, result);

                                    }
                                });
                            }
                        } while (curs.moveToNext());

                    }
                }
            }while(types.moveToNext());
        }
    }

}

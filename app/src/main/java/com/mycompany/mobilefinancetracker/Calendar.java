package com.mycompany.mobilefinancetracker;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by San on 4/4/2015.
 */
public class Calendar extends Fragment implements AdapterView.OnItemSelectedListener{
    View rootView;
    EditText sy;
    EditText sm;
    EditText sd;
    EditText ey;
    EditText em;
    EditText ed;
    Button db;
    AccountsController ac;
    Cursor c;
    List<String> selected;
    String selected_type;
    private int check;
    private List<String> items;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendar, null);
        ac = new AccountsController(getActivity());
        ac.open();
        c = ac.fetch();
        sy = (EditText) rootView.findViewById(R.id.start_year);
        sm = (EditText) rootView.findViewById(R.id.start_month);
        sd = (EditText) rootView.findViewById(R.id.start_day);
        ey = (EditText) rootView.findViewById(R.id.end_year);
        em = (EditText) rootView.findViewById(R.id.end_month);
        ed = (EditText) rootView.findViewById(R.id.end_day);
        db = (Button) rootView.findViewById(R.id.done_button);
        selected = new ArrayList<String>();
        selected_type = "";
        ac.close();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        ac = new AccountsController(getActivity());
        ac.open();
        c = ac.fetch();
        check = 0;
        Spinner dropdown = (Spinner) rootView.findViewById(R.id.account_spinner);
        items = new ArrayList<String>();
        if (c!=null && c.moveToFirst()){
            items.add("All Accounts");
            do{
                if(!items.contains(c.getString(c.getColumnIndex("_id")))){
                    items.add(c.getString(c.getColumnIndex("_id")));
                }
            }while(c.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);



        Spinner view_spin = (Spinner) rootView.findViewById(R.id.view_by_spinner);
        List<String> opts = new ArrayList<String>();
        opts.add("Account");
        opts.add("Category");
        selected_type = "";
        ArrayAdapter<String> view_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, opts);
        view_spin.setAdapter(view_adapter);
        view_spin.setOnItemSelectedListener(this);




        selected = new ArrayList<String>();
        sy = (EditText) rootView.findViewById(R.id.start_year);
        sm = (EditText) rootView.findViewById(R.id.start_month);
        sd = (EditText) rootView.findViewById(R.id.start_day);
        ey = (EditText) rootView.findViewById(R.id.end_year);
        em = (EditText) rootView.findViewById(R.id.end_month);
        ed = (EditText) rootView.findViewById(R.id.end_day);
        db = (Button) rootView.findViewById(R.id.done_button);

        db.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewerScreenIntent = new Intent(getActivity(), ExpenseViewer.class);
                if(check <= 1){
                    for (int iter = 0; iter < items.size(); iter++){
                        if(!selected.contains(items.get(iter))) {
                            selected.add(items.get(iter));
                        }
                    }
                }
                String start_year = sy.getText().toString();
                String start_month = sm.getText().toString();
                String start_day = sd.getText().toString();
                String end_year = ey.getText().toString();
                String end_month = em.getText().toString();
                String end_day = ed.getText().toString();
                final int result = 1;
                viewerScreenIntent.putExtra("callingActivity", "MainActivity");
                viewerScreenIntent.putExtra("start_year", start_year);
                viewerScreenIntent.putExtra("start_month", start_month);
                viewerScreenIntent.putExtra("start_day", start_day);
                viewerScreenIntent.putExtra("end_year", end_year);
                viewerScreenIntent.putExtra("end_month", end_month);
                viewerScreenIntent.putExtra("end_day", end_day);
                viewerScreenIntent.putExtra("size",selected.size());

                for(int x = 0; x<selected.size();x++){
                    String name = "account" + x;
                    viewerScreenIntent.putExtra(name,selected.get(x));
                }

                viewerScreenIntent.putExtra("type",selected_type);
                ac.close();
                startActivityForResult(viewerScreenIntent, result);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        ac.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.account_spinner:
                check++;
                if(check > 1) {
                    if (parent.getItemAtPosition(position).toString().equals("All Accounts")) {
                        for (int t_iter = 0; t_iter < items.size(); t_iter++) {
                            if (!selected.contains(parent.getItemAtPosition(t_iter).toString())) {
                                selected.add(parent.getItemAtPosition(t_iter).toString());
                            }
                        }
                    }
                    else {
                        if (!selected.contains(parent.getItemAtPosition(position).toString())) {
                            selected.add(parent.getItemAtPosition(position).toString());
                        }
                    }
                }

                break;
            case R.id.view_by_spinner:
                selected_type = parent.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


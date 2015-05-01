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
public class Statistics extends Fragment implements AdapterView.OnItemSelectedListener{
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.statistics, null);
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
        ac.close();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        ac = new AccountsController(getActivity());
        ac.open();
        c = ac.fetch();
        Spinner dropdown = (Spinner) rootView.findViewById(R.id.account_spinner);
        List<String> items = new ArrayList<String>();
        if (c!=null && c.moveToFirst()){
            do{
                if(!items.contains(c.getString(c.getColumnIndex("_id")))){
                    items.add(c.getString(c.getColumnIndex("_id")));
                }
            }while(c.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


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
                Intent habitsScreenIntent = new Intent(getActivity(), Habits.class);
                String start_year = sy.getText().toString();
                String start_month = sm.getText().toString();
                String start_day = sd.getText().toString();
                String end_year = ey.getText().toString();
                String end_month = em.getText().toString();
                String end_day = ed.getText().toString();
                final int result = 1;
                habitsScreenIntent.putExtra("callingActivity", "MainActivity");
                habitsScreenIntent.putExtra("start_year", start_year);
                habitsScreenIntent.putExtra("start_month", start_month);
                habitsScreenIntent.putExtra("start_day", start_day);
                habitsScreenIntent.putExtra("end_year", end_year);
                habitsScreenIntent.putExtra("end_month", end_month);
                habitsScreenIntent.putExtra("end_day", end_day);
                habitsScreenIntent.putExtra("size",selected.size());
                for(int x = 0; x<selected.size();x++){
                    String name = "account" + x;
                    habitsScreenIntent.putExtra(name,selected.get(x));
                }
                ac.close();
                startActivityForResult(habitsScreenIntent, result);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected.add(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPause() {
        super.onPause();
        ac.close();
    }
}


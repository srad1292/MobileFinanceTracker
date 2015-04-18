package com.mycompany.mobilefinancetracker;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by San on 4/4/2015.
 */
public class Statistics extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics, null);
        return rootView;
    }
}


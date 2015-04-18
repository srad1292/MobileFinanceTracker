package com.mycompany.mobilefinancetracker;



import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.mycompany.mobilefinancetracker.stab.SlidingTabLayout;


public class MainActivity extends FragmentActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);


    }



}

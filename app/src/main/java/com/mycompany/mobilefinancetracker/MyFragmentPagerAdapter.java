package com.mycompany.mobilefinancetracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by San on 4/5/2015.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Accounts","Today","View","Statistics"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return new Accounts();
        }
        else if(position==1){
            return new TodaysExpenses();
        }
        else if(position==2){
            return new Calendar();
        }
        else{
            return new Statistics();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

package com.mycompany.mobilefinancetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by San on 4/18/2015.
 */
public class Habits extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habits);
        Intent activityThatCalled = getIntent();
        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");
    }

    public void onDoneClicked(View view) {
        Intent goingBack = new Intent();
        goingBack.putExtra("value","1");
        setResult(RESULT_OK,goingBack);

        finish();
    }
}

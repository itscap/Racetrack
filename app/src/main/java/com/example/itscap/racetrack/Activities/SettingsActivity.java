package com.example.itscap.racetrack.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itscap.racetrack.Helpers.SharedPreferencesHelper;
import com.example.itscap.racetrack.R;

public class SettingsActivity extends AppCompatActivity {

    public final String TAG = "debugTag";

    private LinearLayout llTutorialSwitch;
    private TextView tvTutorialOn,tvTutorialOff;
    private SharedPreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        llTutorialSwitch = (LinearLayout) findViewById(R.id.llTutorialSwitch);
        tvTutorialOn = (TextView) findViewById(R.id.tvOn);
        tvTutorialOff = (TextView) findViewById(R.id.tvOff);

        preferencesHelper = new SharedPreferencesHelper(this);
        initValues();

        llTutorialSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean currentValue = preferencesHelper.getPrefBool(SharedPreferencesHelper.TUTORIAL_PREF_KEY);
                preferencesHelper.savePrefBool(SharedPreferencesHelper.TUTORIAL_PREF_KEY,!currentValue);
                setupSwitchState(tvTutorialOn,tvTutorialOff,!currentValue);
            }
        });

    }

    private void initValues(){

        Boolean isTutorialON = preferencesHelper.getPrefBool(SharedPreferencesHelper.TUTORIAL_PREF_KEY);
        setupSwitchState(tvTutorialOn,tvTutorialOff,isTutorialON);
    }


    private void setupSwitchState(TextView tvON,TextView tvOFF, Boolean isON){

        int selected = getResources().getColor(R.color.colorAccent);
        int unselected = getResources().getColor(R.color.colorPrimaryDark);

        if(isON) {
            tvON.setTextColor(selected);
            tvOFF.setTextColor(unselected);
        } else {
            tvON.setTextColor(unselected);
            tvOFF.setTextColor(selected);
        }
    }

}

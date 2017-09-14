package com.example.itscap.racetrack.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itscap.racetrack.Helpers.SharedPreferencesHelper;
import com.example.itscap.racetrack.R;

public class SettingsActivity extends AppCompatActivity {

    public final String TAG = "debugTag";

    private CharSequence mapStyles[];
    private LinearLayout llTutorialSwitch;
    private TextView tvTutorialOn,tvTutorialOff, tvMapStyle;
    private SharedPreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        llTutorialSwitch = (LinearLayout) findViewById(R.id.llTutorialSwitch);
        tvTutorialOn = (TextView) findViewById(R.id.tvOn);
        tvTutorialOff = (TextView) findViewById(R.id.tvOff);
        tvMapStyle = (TextView) findViewById(R.id.tvMapStyle);

        mapStyles = new CharSequence[] {"Standard", "Retro", "Night", "Hybrid"};
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

        tvMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMultipleChoiceMenu();
            }
        });

    }

    private void initValues() {

        Boolean isTutorialON = preferencesHelper.getPrefBool(SharedPreferencesHelper.TUTORIAL_PREF_KEY);
        setupSwitchState(tvTutorialOn,tvTutorialOff,isTutorialON);

        int mapStylePreference = preferencesHelper.getPrefInt(SharedPreferencesHelper.MAP_STYLE_PREF_KEY);
        setupMapStyleTextView(mapStylePreference);

    }

    private void showMultipleChoiceMenu() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose a map style:");
        builder.setItems(mapStyles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onMapStyleSelected(which);
            }
        });
        builder.show();
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

    private void setupMapStyleTextView(int mapStylePreference){

        switch (mapStylePreference){
            case R.raw.map_style_standard:
                tvMapStyle.setText("Standard");
                break;
            case R.raw.map_style_retro:
                tvMapStyle.setText("Retro");
                break;
            case R.raw.map_style_night:
                tvMapStyle.setText("Night");
                break;
            default:
                tvMapStyle.setText("Hybrid");
        }
    }

    private void onMapStyleSelected(int which){

        int mapStyle = -1;

        switch (which){
            case 0:
                mapStyle = R.raw.map_style_standard;
                break;
            case 1:
                mapStyle = R.raw.map_style_retro;
                break;
            case 2:
                mapStyle = R.raw.map_style_night;
                break;
            default:
                mapStyle = -1;//Hybrid is not actyally a style
        }

        preferencesHelper.savePrefInt(SharedPreferencesHelper.MAP_STYLE_PREF_KEY,mapStyle);
        setupMapStyleTextView(mapStyle);
    }

}

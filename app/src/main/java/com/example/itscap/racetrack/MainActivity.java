package com.example.itscap.racetrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity  {

    public final String TAG = "debugTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * TODO: implement an app introduction...
         */
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }


}

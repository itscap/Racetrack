package com.example.itscap.racetrack.Activities;

import android.os.Bundle;

import com.example.itscap.racetrack.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by itscap on 9/13/17.
 */

public class TutorialActivity extends IntroActivity {


    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title("Racetrack")
                .description("Discover world main racing tracks")
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Slide to change track")
                .description("Slide with your finger on the bottom part of the screen to change location")
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Let's Race!")
                .background(R.color.colorAccent2)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
    }
}
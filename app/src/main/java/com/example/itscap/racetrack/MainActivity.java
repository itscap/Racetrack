package com.example.itscap.racetrack;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TrackAsyncTask.IHttpRequest{

    public final String TAG = "debugTag";

    private RecyclerView recyclerView;
    private PagerSnapHelper snapHelper;
    private TrackAsyncTask trackAsyncTask;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.calendarRecyclerView);

        context = getApplicationContext();
        snapHelper = new PagerSnapHelper();

        trackAsyncTask = new TrackAsyncTask(this);
        trackAsyncTask.getTracks();

        String key = new ApiKeyHelper(this).getApiKeyValue("maps");
        Log.d(TAG, "onCreate: KEY = " + key);
    }




    private void showTracks(List<Track> tracks){


        if (tracks != null && tracks.size() > 0) {

            TrackAdapter calendarAdapter = new TrackAdapter(context,tracks);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(calendarAdapter);
            //Scroll like gallery
            snapHelper.attachToRecyclerView(recyclerView);

        } else {
            //TODO: throw error
            Log.d(TAG, "Error = no Data in list.");
        }
    }



    @Override
    public void onRequestCompleted(ArrayList<Track> tracks) {


        Log.d(TAG, "onRequestCompleted: " + tracks.size());
        showTracks(tracks);
    }
}

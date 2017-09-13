package com.example.itscap.racetrack;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TrackAsyncTask.IHttpRequest {

    public final String TAG = "debugTag";
    public final int CAMERA_SPEED = 600;//ms

    private ArrayList<Track> tracks;
    private RecyclerView recyclerView;
    private PagerSnapHelper snapHelper;
    private TrackAsyncTask trackAsyncTask;
    private Context context;
    private RecyclerView.LayoutManager mLayoutManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng currentMapPosition;
    private CameraUpdate cameraLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        recyclerView = (RecyclerView) findViewById(R.id.calendarRecyclerView);

        context = getApplicationContext();
        tracks = new ArrayList<>();
        snapHelper = new PagerSnapHelper();
        trackAsyncTask = new TrackAsyncTask(this);
        trackAsyncTask.getTracks();
        initMap(mapFragment);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View currentView = snapHelper.findSnapView(mLayoutManager);
                    int pos = mLayoutManager.getPosition(currentView);
                    setMapPosition(tracks.get(pos));
                }
            }
        });


    }

    private void initMap(SupportMapFragment mapFragment){
        mapFragment.getMapAsync(this);

        //Fixing map long loading deelay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mapview = new MapView(getApplicationContext());
                    mapview.onCreate(null);
                    mapview.onPause();
                    mapview.onDestroy();
                }catch (Exception ignored){
                    Log.d(TAG, "run: "+ ignored);
                }
            }
        }).start();

    }


    private void showTracks(List<Track> tracks){


        if (tracks != null && tracks.size() > 0) {

            mLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            TrackAdapter calendarAdapter = new TrackAdapter(context, tracks);
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



    private void setMapPosition(Track track){

        currentMapPosition = new LatLng(Double.valueOf(track.getLatitude()),
                                        Double.valueOf(track.getLongitude()));
        cameraLocation = CameraUpdateFactory.newLatLngZoom(currentMapPosition, 14f);
        mMap.addMarker(new MarkerOptions().position(currentMapPosition).title(track.getName()));
        mMap.animateCamera(cameraLocation,CAMERA_SPEED,null);

    }



    /**  ----CALLBACKS----*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    @Override
    public void onRequestCompleted(ArrayList<Track> tracks) {

        Log.d(TAG, "onRequestCompleted: " + tracks.size());
        this.tracks = tracks;
        showTracks(tracks);
        setMapPosition(tracks.get(0));
    }
}

package com.example.itscap.racetrack;

import android.app.ProgressDialog;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        TrackAsyncTask.IHttpRequest,
        ErrorDialog.IErrorCallback {

    public final String TAG = "debugTag";
    private static final String DELETE_DIALOG_TAG = "deleteDialogTag";
    public final int CAMERA_SPEED = 600;//ms

    private ArrayList<Track> tracks;
    private RecyclerView recyclerView;
    private PagerSnapHelper snapHelper;
    private TrackAsyncTask trackAsyncTask;
    private Context context;
    private ErrorDialog errorDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng currentMapPosition;
    private CameraUpdate cameraLocation;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        recyclerView = (RecyclerView) findViewById(R.id.calendarRecyclerView);

        context = getApplicationContext();
        tracks = new ArrayList<>();
        snapHelper = new PagerSnapHelper();

        getTracks();
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

    private void getTracks(){

        isLoading(true);
        trackAsyncTask = new TrackAsyncTask(this);
        trackAsyncTask.getTracks();
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

            onRequestFailed("No Data in list.");
        }
    }



    private void setMapPosition(Track track){

        currentMapPosition = new LatLng(Double.valueOf(track.getLatitude()),
                                        Double.valueOf(track.getLongitude()));
        cameraLocation = CameraUpdateFactory.newLatLngZoom(currentMapPosition, 14f);
        mMap.addMarker(new MarkerOptions().position(currentMapPosition).title(track.getName()));
        mMap.animateCamera(cameraLocation,CAMERA_SPEED,null);

    }

    private void isLoading(Boolean loading){

        if(loading) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("I'm starting the engine... ;)");
            progressDialog.setCancelable(false); //Disable dismiss by tapping outside of the dialog
            progressDialog.show();
        } else {

            progressDialog.dismiss();
        }
    }

    /**  ----CALLBACKS----*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    @Override
    public void onRequestSuccess(ArrayList<Track> tracks) {

        this.tracks = tracks;
        showTracks(tracks);
        setMapPosition(tracks.get(0));
        isLoading(false);
    }

    @Override
    public void onRequestFailed(String error) {
        Log.d(TAG, "onRequestFailed: " + error);
        errorDialog = ErrorDialog.getInstance(
                "Whops, something went wrong... :/",
                error + "\n\n" +
                "Do you want to retry?"
        );
        errorDialog.show(getSupportFragmentManager(),DELETE_DIALOG_TAG);
        isLoading(false);
    }

    @Override
    public void retry() {

        getTracks();
    }
}

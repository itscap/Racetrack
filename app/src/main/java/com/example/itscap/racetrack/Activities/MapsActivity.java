package com.example.itscap.racetrack.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.itscap.racetrack.Dialogs.ErrorDialog;
import com.example.itscap.racetrack.Helpers.SharedPreferencesHelper;
import com.example.itscap.racetrack.R;
import com.example.itscap.racetrack.Track;
import com.example.itscap.racetrack.TrackAdapter;
import com.example.itscap.racetrack.TrackAsyncTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        TrackAsyncTask.IHttpRequest,
        ErrorDialog.IErrorCallback {

    public final String TAG = "debugTag";
    private static final String DELETE_DIALOG_TAG = "deleteDialogTag";
    private static final int SETTINGS_ACTIVITY_RES = 1001;
    public final int CAMERA_SPEED = 600;//ms

    private ArrayList<Track> tracks;
    private RecyclerView recyclerView;
    private PagerSnapHelper snapHelper;
    private TrackAsyncTask trackAsyncTask;
    private Context context;
    private LatLng currentMapPosition;
    private CameraUpdate cameraLocation;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LinearLayout llCircle;
    private ImageView settingsButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private ErrorDialog errorDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        recyclerView = (RecyclerView) findViewById(R.id.calendarRecyclerView);
        llCircle = (LinearLayout) findViewById(R.id.llCircle);
        settingsButton = (ImageView) findViewById(R.id.imgSettings);

        context = getApplicationContext();
        tracks = new ArrayList<>();
        snapHelper = new PagerSnapHelper();

        showTutorial();
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

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateSettingsView();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_ACTIVITY_RES){
            setMapCustomStyle();
            resetSettingsView();
        }
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

    private void setMapCustomStyle(){

        int mapStyle = new SharedPreferencesHelper(this).getPrefInt(SharedPreferencesHelper.MAP_STYLE_PREF_KEY);
        if(mapStyle == -1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            try {

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, mapStyle));
                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }
        }
    }

    private void showTutorial() {

        if(new SharedPreferencesHelper(this).getPrefBool(SharedPreferencesHelper.TUTORIAL_PREF_KEY)) {
            Intent tutorialIntent = new Intent(this, TutorialActivity.class);
            startActivity(tutorialIntent);
        }
    }

    private void isLoading(Boolean loading){

        if(loading) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("I'm starting the engine...");
            progressDialog.setCancelable(false); //Disable dismiss by tapping outside of the dialog
            progressDialog.show();
        } else {

            progressDialog.dismiss();
        }
    }


    private void animateSettingsView(){

        settingsButton.setVisibility(View.INVISIBLE);
        //recyclerView.setVisibility(View.INVISIBLE);

        float scale = 40f;
        int duration = 1200;
        llCircle.animate()
                .scaleXBy(scale)
                .scaleYBy(scale)
                .alpha(1)
                .setDuration(duration)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        Intent settingsIntent = new Intent(context, SettingsActivity.class);
                        startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_RES);
                    }
                });
    }

    private void resetSettingsView(){

        float scale = 1f;
        int duration = 600;
        llCircle.animate()
                .scaleX(scale)
                .scaleY(scale)
                .alpha(1)
                .setDuration(duration)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        settingsButton.setVisibility(View.VISIBLE);
                        //recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }


    /**  ----CALLBACKS----*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapCustomStyle();
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

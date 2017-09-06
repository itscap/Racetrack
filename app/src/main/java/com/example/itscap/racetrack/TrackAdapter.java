package com.example.itscap.racetrack;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.RaceViewHolder> implements OnMapReadyCallback{

    public final String TAG = "debugTag";

    private Context context;
    private LinearLayoutManager llManager;
    private List<Track> tracks;
    private int currentPos;


    public TrackAdapter(Context context, LinearLayoutManager llManager, List<Track> tracks) {

        this.context = context;
        this.llManager = llManager;
        this.tracks = tracks;
        currentPos = 0;

    }


    public class RaceViewHolder extends RecyclerView.ViewHolder {

        private TextView circuit,country;
        private MapView trackMap;

        public RaceViewHolder(View view) {
            super(view);


            circuit = (TextView) view.findViewById(R.id.tvCircuitName);
            country = (TextView) view.findViewById(R.id.tvCountry);
            trackMap = (MapView)view.findViewById(R.id.mapView);
        }
    }

    @Override
    public RaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.circuit_col, parent, false);

        return new RaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RaceViewHolder holder, int position) {

        Track track = tracks.get(position);

        holder.circuit.setText(track.getCity());
        holder.country.setText(track.getCountry());


        holder.trackMap.onCreate(null);
        holder.trackMap.getMapAsync(this);
    }

    @Override
    public void onViewAttachedToWindow(RaceViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        currentPos = llManager.getPosition(holder.itemView);
        Log.d(TAG, "onViewAttachedToWindow: currentPos => " + currentPos);

    }

    @Override
    public int getItemCount() {

        return tracks.size();
    }

    private void setMapPosition(GoogleMap googleMap){

        Track track = tracks.get(currentPos);
        LatLng position = new LatLng(Double.valueOf(track.getLatitude()),
                                     Double.valueOf(track.getLongitude()));
        googleMap.clear();
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(position , 14.0f));
        googleMap.addMarker(new MarkerOptions().position(position).title(track.getName()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        setMapPosition(googleMap);
    }


}



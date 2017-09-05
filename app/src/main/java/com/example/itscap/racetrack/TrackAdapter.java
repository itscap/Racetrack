package com.example.itscap.racetrack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.RaceViewHolder> {

    public final String TAG = "debugTag";

    private Context context;
    private List<Track> tracks;


    public TrackAdapter(Context context, List<Track> tracks) {

        this.context = context;
        this.tracks = tracks;

    }


    public class RaceViewHolder extends RecyclerView.ViewHolder {

        private TextView circuit,country;

        public RaceViewHolder(View view) {
            super(view);

            circuit = (TextView) view.findViewById(R.id.tvCircuitName);
            country = (TextView) view.findViewById(R.id.tvCountry);
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
        //Race Inf
        holder.circuit.setText(track.getCity());
        holder.country.setText(track.getCountry());

    }

    @Override
    public int getItemCount() {

        return tracks.size();
    }


}



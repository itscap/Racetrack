package com.example.itscap.racetrack;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

/**
 * Created by itscap on 9/5/17.
 */

public class TrackAsyncTask  extends AsyncHttpClient {


    private final String TAG = "debugTag";
    private String BASE_URL;
    private Context context;
    private AsyncHttpClient client;
    private IHttpRequest mListner;


    public interface IHttpRequest{
        public void onRequestSuccess(ArrayList <Track> tracks);
        public void onRequestFailed(String error);
    }


    public TrackAsyncTask(Context context) {

        BASE_URL = "https://racetrack.herokuapp.com/circuits";
        this.context=context;
        client = new AsyncHttpClient();

        if (context instanceof IHttpRequest)
            mListner = (IHttpRequest) context;

    }


    public void getTracks() {

        client.get(BASE_URL,  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    parseJSON(response);
                } else {
                    mListner.onRequestFailed("Cannot retrieve data.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                mListner.onRequestFailed("Server request failded with status code " + statusCode);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                mListner.onRequestFailed("Server request failded with status code " + statusCode);
            }

        });

    }


    private void parseJSON(JSONArray response){

        ArrayList <Track> tracks = new ArrayList<>();
        try {

            for (int i=0; i<response.length();i++) {

                Track track = new Track();
                JSONObject jsonTrack = response.getJSONObject(i);
                JSONObject location = jsonTrack.getJSONObject("location");
                JSONObject length = jsonTrack.getJSONObject("circuitLength");

                track.setName(jsonTrack.getString("name"));
                track.setCountry(location.getString("country"));
                track.setCity(location.getString("city"));
                track.setLatitude(location.getString("latitude"));
                track.setLongitude(location.getString("longitude"));
                track.setKm(length.getString("km"));

                tracks.add(track);
            }

            mListner.onRequestSuccess(tracks);

        } catch (Exception e) {
            Log.d(TAG, "onSuccess: JSONException =>" + e);
            mListner.onRequestFailed("bad formatted json.");
        }
    }

}

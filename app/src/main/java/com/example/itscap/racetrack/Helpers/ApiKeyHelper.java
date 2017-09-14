package com.example.itscap.racetrack.Helpers;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;

/**
 * Created by itscap on 9/6/17.
 *
 * We actually don't need this but i just wanted to do some tests
 */

public class ApiKeyHelper {

    public final String TAG = "debugTag";
    public final String apiKeyAssert = "api_keys.json";
    private Context context;

    public ApiKeyHelper(Context context){

        this.context = context;
    }


    private String readAsset() {

        String assetContent = "";
        try {

            InputStream is = context.getAssets().open(apiKeyAssert);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            assetContent = new String(buffer, "UTF-8");

        } catch (Exception e) {

            e.printStackTrace();
            Log.d(TAG, "getApiKey: Error => " + e);
        }

        return assetContent;
    }

    public String getApiKeyValue(String jsonKey){

        String apiKey = "";

        try {
            JSONObject json = new JSONObject(readAsset());
            apiKey = json.getString(jsonKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return apiKey;
    }



}

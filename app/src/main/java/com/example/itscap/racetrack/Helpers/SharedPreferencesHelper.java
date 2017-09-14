package com.example.itscap.racetrack.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by itscap on 9/14/17.
 */

public class SharedPreferencesHelper {


    public static final String APP_SHARED_PREF_BUNDLE ="com.racetrack.sharedprefBundle";

    //Pref Keys
    public static final String TUTORIAL_PREF_KEY ="tutorial_pref_key";

    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {

        this.context=context;
        sharedpreferences = context.getSharedPreferences(APP_SHARED_PREF_BUNDLE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }


    public void savePrefStr(String key,String value){

        editor.putString(key, value);
        editor.commit();
    }

    public String getPrefStr(String key){

        return sharedpreferences.getString(key,"");
    }

    public void savePrefInt(String key,int value){

        editor.putInt(key, value);
        editor.commit();
    }

    public int getPrefInt(String key){

        //Int default value is 0 so app won't crash..
        return sharedpreferences.getInt(key,0);
    }

    public void savePrefBool(String key, boolean value){

        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getPrefBool(String key){

        return sharedpreferences.getBoolean(key,false);
    }

}

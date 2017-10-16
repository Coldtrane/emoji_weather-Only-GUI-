package com.example.gabe.emojiweather;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by minqianghu on 2017/10/12.
 */

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);

    }
    public String getCity(){

        return prefs.getString("city", "Auburn Hills, US" );
    }
    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}

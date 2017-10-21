package com.example.gabe.emojiweather;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class JSONWeatherParser {

    //need to accept json object here
    public String[] getWeather(String data){


        try{

            String[] output = new String[3];

            //Parsing for Weather description
            String[] parts = data.split("weather\":\"");
            parts = parts[1].split("\",");
            String weatherCond = parts[0];
            output[0] = weatherCond;

            //Parsing for Temperature data in F
            parts = data.split("temp_f\":");
            parts = parts[1].split(",");
            String weatherTemp = parts[0];
            output[1] = weatherTemp;

            //Temporary parsing for City Name
            parts = data.split("city\":\"");
            parts = parts[1].split("\",");
            String cityName = parts[0];
            output[2] = cityName;
            return output;

        }
        catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }
}

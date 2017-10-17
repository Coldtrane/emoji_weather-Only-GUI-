package com.example.gabe.emojiweather;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class JSONWeatherParser {

    //need to accept json object here
    public String[] getWeather(String data){


        try{


            //TODO change string split back to JSONArray
            /*
            JSONObject jsonObject = new JSONObject(data);
            //Sets weather type from second element of weather array
            JSONObject jObj= new JSONObject(data);
            JSONArray arrJson = jsonData.getJSONArray("numbers");
            if(myTemperature == null)
            {
                myTemperature = "empty";
            }
            if(myWeather == null)
            {
                myWeather = "empty";
            }
            valuesArray[0] = myTemperature;
            valuesArray[1] = myWeather;
            return valuesArray;
            //String[] parts = data.split(",");
            //String[] output = new String[2];
            //output[0] = parts[6];
            //return output;
            */

            //temporary parsing for Weather
            String[] parts = data.split(",\"description");
            parts = parts[1].split("\"");
            String weatherCond = parts[parts.length-1];
            String[] output = new String[2];

            output[0] = weatherCond;

            //temporary parsing for Temperature
            parts = data.split(",\"pressure");
            parts = parts[1].split("\"");
            String weatherTemp = parts[parts.length-1];
            weatherTemp = weatherTemp.substring(1);

            output[1] = weatherTemp;
            return output;

        }
        catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }
}

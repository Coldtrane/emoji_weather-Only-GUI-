package com.example.gabe.emojiweather;

import android.widget.TextView;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;


public class JSONTrafficParser {

    //need to accept json object here
    public String getTraffic(String data){

        try
        {
            String output = "";
            //Parsing for Weather description
            String[] parts = data.split("shortDesc\":\"");
            String[] reports = new String[parts.length];
            for (int i = 1; i < parts.length; i++)
            {
                String[] temp = parts[i].split("\",\"fullDesc\"");
                output =  output + temp[0] +"\n\n";
            }
            if(output == "")
            {
                return "No Traffic Incidents Found!";
            }
            return output;
        }
        catch (Exception e){
            e.printStackTrace();

            return "No Traffic Incidents Found.";
        }
    }
}
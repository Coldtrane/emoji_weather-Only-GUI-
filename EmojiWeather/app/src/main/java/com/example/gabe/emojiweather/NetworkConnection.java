package com.example.gabe.emojiweather;


import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkConnection {
    JSONObject networkConnection(String urlString){

        HttpURLConnection urlCon;
        StringBuilder result;
        URL urlObj;

        JSONObject jObj = null;

        try
        {
            urlObj = new URL(urlString);
            urlCon = (HttpURLConnection) urlObj.openConnection();
            urlCon.setDoOutput(false);
            urlCon.setRequestMethod("GET");
            urlCon.setConnectTimeout(15000);
            urlCon.connect();

            //Receive the response from the server
            InputStream in = new BufferedInputStream(urlCon.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String bufferString;
            while ((bufferString = buffer.readLine()) != null)
            {
                result.append(bufferString);
            }
            jObj = new JSONObject(result.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return jObj;
    }
}
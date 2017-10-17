package com.example.gabe.emojiweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView cityText;
    private TextView tempText;
    private TextView weatherText;
    String resultString = "";

    //Loaded on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        cityText = (TextView) findViewById(R.id.cityView);
        tempText = (TextView) findViewById(R.id.temperatureView);
        weatherText = (TextView) findViewById(R.id.weatherView);
        new NetworkConnect().execute();
    }


    class NetworkConnect extends AsyncTask<Void, Void, JSONObject> {
        String citycode = "4058740";
        String API_KEY = "5134d45a3a65322c8492a4028c7c9e3f";
        private String JSON_URL = ("http://api.openweathermap.org/data/2.5/forecast?id=" + citycode+ "&APPID=" + API_KEY);
        HttpURLConnection myTrial;
        StringBuilder result;
        URL urlObj;


        @Override
        protected JSONObject doInBackground(Void... args)
        {
            JSONObject jObj = null;

            try
            {
                urlObj = new URL(JSON_URL);
                myTrial = (HttpURLConnection) urlObj.openConnection();
                myTrial.setDoOutput(false);
                myTrial.setRequestMethod("GET");
                myTrial.setConnectTimeout(15000);
                myTrial.connect();

                //Receive the response from the server
                InputStream in = new BufferedInputStream(myTrial.getInputStream());
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

        @Override
        protected void onPostExecute(JSONObject json) {
            //Use JSON result to display in TextView
            if (json != null) {
                {
                    resultString = json.toString();
                    JSONWeatherParser parserWeather = new JSONWeatherParser();
                    String[] newStuff = parserWeather.getWeather(resultString);
                    weatherText.setText(newStuff[0]);
                    tempText.setText(newStuff[1]);

                }
            }
        }
    }
}
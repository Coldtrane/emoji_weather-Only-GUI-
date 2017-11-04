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
    private TextView trafficText;
    String resultString = "";
    String zipcode = "48302";

    //Loaded on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        cityText = (TextView) findViewById(R.id.cityView);
        tempText = (TextView) findViewById(R.id.temperatureView);
        weatherText = (TextView) findViewById(R.id.weatherView);
        trafficText = (TextView) findViewById(R.id.trafficView);

        new WeatherConnect().execute();
        new TrafficConnect().execute();
    }


    class WeatherConnect extends AsyncTask<Void, Void, JSONObject>
    {


        private String JSON_URL = ("https://api.wunderground.com/api/" + "48a6ddb9b2e805f6" + "/conditions/q/" + zipcode + ".json");



        @Override
        protected JSONObject doInBackground(Void... args)
        {
            NetworkConnection networkCon = new NetworkConnection();
            return networkCon.networkConnection(JSON_URL);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //Use JSON result to display in TextView
            if (json != null) {
                {
                    resultString = json.toString();
                    JSONWeatherParser parserWeather = new JSONWeatherParser();
                    String[] parsedData = parserWeather.getWeather(resultString);
                    double tempValue = Double.parseDouble(parsedData[1]);


                    if(parsedData[0].contains("Cloudy") || parsedData[0].contains("Overcast") || parsedData[0].contains("Fog") || parsedData[0].contains("Hazy"))
                    {
                        weatherText.setText(parsedData[0] + " ☁");
                    }
                    else if(parsedData[0].contains("Snow") || parsedData[0].contains("Sleet") || parsedData[0].contains("Flurries"))
                    {
                        weatherText.setText(parsedData[0] + " ❄");
                    }
                    else if(parsedData[0].contains("Rain") || parsedData[0].contains("Storms"))
                    {
                        weatherText.setText(parsedData[0] + " ☔");
                    }
                    else
                    {
                        weatherText.setText(parsedData[0] + " ☀");
                    }

                    if(tempValue >= 80)
                    {
                        tempText.setText(tempValue + " \uD83D\uDE21");
                    }
                    else if(tempValue >= 65 && tempValue< 80)
                    {
                        tempText.setText(tempValue + " \uD83D\uDE03");
                    }
                    else if(tempValue > 32 && tempValue < 65)
                    {
                        tempText.setText(tempValue + " \uD83D\uDE2C");
                    }
                    else
                    {
                        tempText.setText(tempValue + " \uD83D\uDE30");
                    }

                    cityText.setText(parsedData[2]);
                }
            }
        }
    }

    class TrafficConnect extends AsyncTask<Void, Void, JSONObject>
    {
        String API_KEY = "hH7c33NIQMCZEDqBA9DSDobxcnjprlu4";
        private String JSON_URL = ("https://www.mapquestapi.com/traffic/v2/incidents?&outFormat=json&boundingBox=42.740708808266845%2C-83.06625366210936%2C42.53689200787315%2C-83.51909637451172&filters=construction%2Cincidents&key=" + API_KEY);

        @Override
        protected JSONObject doInBackground(Void... args)
        {

            NetworkConnection networkCon = new NetworkConnection();
            return networkCon.networkConnection(JSON_URL);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //Use JSON result to display in TextView
            if (json != null) {
                {
                    resultString = json.toString();
                    JSONTrafficParser parserTraffic = new JSONTrafficParser();
                    String parsedData = parserTraffic.getTraffic(resultString);


                    trafficText.setText(parsedData);
                }
            }
        }
    }
}
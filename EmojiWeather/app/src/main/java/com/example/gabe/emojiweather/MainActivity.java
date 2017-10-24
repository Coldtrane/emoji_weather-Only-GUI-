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
        String zipcode = "48302";
        String API_KEY = "48a6ddb9b2e805f6";
        private String JSON_URL = ("https://api.wunderground.com/api/" + API_KEY + "/conditions/q/" + zipcode + ".json");
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
}
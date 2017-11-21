package com.example.gabe.emojiweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Window;
import android.widget.TextView;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private TextView cityText;
    private TextView tempText;
    private TextView weatherText;
    private TextView trafficText;
    String resultString = "";
    String zipcode = "";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Sets the view to use the activity main XML file
        setContentView(R.layout.activity_main);
        cityText = (TextView) findViewById(R.id.cityView);
        tempText = (TextView) findViewById(R.id.temperatureView);
        weatherText = (TextView) findViewById(R.id.weatherView);
        trafficText = (TextView) findViewById(R.id.trafficView);

        //Calls the zipcode popup
        enterZipPrompt();

    }

    /**
     *
     */
    protected void enterZipPrompt()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your zipcode");
        builder.setCancelable(false);

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        input.setHint("Enter a valid 5-digit zipcode");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length() == 5)
                {
                    zipcode = input.getText().toString();
                    new WeatherConnect().execute();
                    new TrafficConnect().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid Entry. Please try again.", Toast.LENGTH_SHORT).show();
                    enterZipPrompt();
                }

            }
        });
        builder.show();

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
                    else if(parsedData[0].contains("Error"))
                    {
                        weatherText.setText("❌" + parsedData[0] + " ❌");
                    }
                    else
                    {
                        weatherText.setText(parsedData[0] + " ☀");
                    }
                    if(tempValue >= 80 && tempValue < 200)
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
                    else if (tempValue == 666)
                    {
                        tempText.setText("☢" + tempValue + "☢");
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
        //old call
        //https://www.mapquestapi.com/traffic/v2/incidents?&outFormat=json&boundingBox=42.740708808266845%2C-83.06625366210936%2C42.53689200787315%2C-83.51909637451172&filters=construction%2Cincidents&key=" + API_KEY
        //detroit
        //https://www.mapquestapi.com/traffic/v2/incidents?&outFormat=json&boundingBox=42.43232607079181%2C-82.8324508666992%2C42.23334735634176%2C-83.26332092285156&filters=construction%2Cincidents&key=
        String API_KEY = "hH7c33NIQMCZEDqBA9DSDobxcnjprlu4";
        private String JSON_URL = ("https://www.mapquestapi.com/traffic/v2/incidents?&outFormat=json&boundingBox=42.43232607079181%2C-82.8324508666992%2C42.23334735634176%2C-83.26332092285156&filters=construction%2Cincidents&key=" + API_KEY);

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
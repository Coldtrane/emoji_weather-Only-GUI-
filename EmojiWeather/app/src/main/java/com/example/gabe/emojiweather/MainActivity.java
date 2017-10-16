package com.example.gabe.emojiweather;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.example.gabe.emojiweather.CityPreference;
import com.example.gabe.emojiweather.JSONWeatherParser;
import com.example.gabe.emojiweather.WeatherHttpClient;
import com.example.gabe.emojiweather.Weather;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import 	org.json.*;
import javax.net.ssl.HttpsURLConnection;

import java.lang.Object.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.*;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TextView;
import java.util.Scanner;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private TextView description;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);

        CityPreference cityPreference = new CityPreference(MainActivity.this);
        renderWeatherData(cityPreference.getCity());
    }

    public void renderWeatherData(String city ){
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[] {city +"&units=metric"});
    }
    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @Override
        protected Weather doInBackground(String... params) {
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data);
            Log.v("Data: ", weather.currentCondition.getDescription());
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity()+","+weather.place.getCountry());
            temp.setText(""+ tempFormat+"℃");
            description.setText("Condition: " + weather.currentCondition.getCondition() +
                    "("+ weather.currentCondition.getDescription() + ")");
        }
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Detroit, US");
        builder.setPositiveButton("Submit",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                CityPreference cityPreference = new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());

                String newCity = cityPreference.getCity();
                renderWeatherData(newCity);
            }

        });
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        /*if (id == R.id.change_cityId){
            showInputDialog();
        }*/
        return super.onOptionsItemSelected(item);
    }

}
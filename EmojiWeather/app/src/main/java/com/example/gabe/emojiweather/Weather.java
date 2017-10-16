package com.example.gabe.emojiweather;

/**
 * Created by minqianghu on 2017/10/11.
 */

public class Weather {
    public Place place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Clouds clouds = new Clouds();
    public Snow snow = new Snow();
}
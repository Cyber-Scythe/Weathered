package com.example.weathered.weathered;

import java.util.ArrayList;

public class StaticRvModel {

    private int image;
    private String dayOfWeek, dailyHi, dailyLow, dayNight, percentRain;


    StaticRvModel(int image, String dayOfWeek, String dailyHi, String dailyLow, String dayNight) {

        this.image = image;
        this.dayOfWeek = dayOfWeek;
        this.dailyHi = dailyHi;
        this.dailyLow = dailyLow;
        this.dayNight = dayNight;
    }

    public String getDayNight(){
        return dayNight;
    }

    public void setImage(int image){

        this.image = image;
    }

    public int getImage() {

        return image;
    }

    public String getPercentRain(){
        return percentRain;
    }

    public void setDayOfWeek(String dayOfWeek){

        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek() {

        return dayOfWeek;
    }


    public void setDailyHi(String dailyHi){

        this.dailyHi = dailyHi;
    }

    public String getDailyHi(){
        return dailyHi;
    }


    public void setDailyLow(String dailyLow){

        this.dailyLow = dailyLow;

    }
    public String getDailyLow(){

        return dailyLow;
    }

    int lastDailyId = 0;

    public ArrayList<StaticRvModel> createDailyList(int numLocations) {

        ArrayList<StaticRvModel> daily = new ArrayList<StaticRvModel>();

        for(int i = 0; i < 8; i++) {

            daily.add(new com.example.weathered.weathered.StaticRvModel(image, dayOfWeek, dailyHi, dailyLow, dayNight));

            lastDailyId++;
            System.out.println("lastLocationId: " + lastDailyId);
        }

        return daily;
    }
}

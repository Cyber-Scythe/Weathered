package com.example.weathered.weathered;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DynamicRvModel {

    String time, temp;
    String hourlyMain;
    LocalTime sunrise, sunset;
    LocalTime currTime;

    public DynamicRvModel(String time,
                          String temp,
                          String hourlyMain,
                          LocalTime currSunrise,
                          LocalTime currSunset,
                          LocalTime currTime) {

        this.time = time;
        this.temp = temp;
        this.hourlyMain = hourlyMain;
        this.sunrise = currSunrise;
        this.sunset = currSunset;
        this.currTime = currTime;
    }


    public void setTime(String time) {

        this.time = time;
    }

    public String getTime() {

        return time;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public LocalTime getCurrTime() {
        return currTime;
    }

    public void setTemp(String temp) {

        this.temp = temp;
    }

    public String getTemp() {

        return temp;
    }

    public void setHourlyMain(String hourlyMain) {

        this.hourlyMain = hourlyMain;
    }

    public String getHourlyMain() {

        return hourlyMain;
    }


    int lastLocationId = 0;


    public List<DynamicRvModel> createHourlyList(int numLocations) {

        List<DynamicRvModel> hourly = new ArrayList<DynamicRvModel>();

        for (int i = 0; i < 24; i++) {

            hourly.add(new com.example.weathered.weathered.DynamicRvModel(time, temp, hourlyMain, sunrise, sunset, currTime));

            lastLocationId++;
            System.out.println("lastLocationId: " + lastLocationId);
        }

        return hourly;
    }
}

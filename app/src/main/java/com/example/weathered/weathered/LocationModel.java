package com.example.weathered.weathered;

import java.util.ArrayList;

public class LocationModel {

    static String cityName;
    static String localTime;
    static String localTemp;
    static String dayOrNight;
    static String localWeather;
    public static double lat, lon;

    public static ArrayList<String> cityNameArray = new ArrayList<>();
    public static ArrayList<String> localTimeArray = new ArrayList<>();
    public static ArrayList<String> localTempArray = new ArrayList<>();
    public static ArrayList<String> localWeatherArray = new ArrayList<>();
    public static ArrayList<String> dayNightArray = new ArrayList<>();
    public static ArrayList<Double> latsArray = new ArrayList<>();
    public static ArrayList<Double> lonsArray = new ArrayList<>();


    public LocationModel(String time, String city, String temp, String dayNight, String weather, double latitude, double longitude){

        localTime = time;
        cityName = city;
        localTemp = temp;
        dayOrNight = dayNight;
        localWeather = weather;
        lat = latitude;
        lon = longitude;
    }


    public static void setCityName(String city){

        cityName = city;
        cityNameArray.add(cityName);
    }


    public static void setLocalTime(String time){

        localTime = time;
        localTimeArray.add(localTime);
    }


    public static void setLocalTemp(String temp){

        localTemp = temp;
        localTempArray.add(localTemp);
    }


    public static void setLocalWeather(String weather){

        localWeather = weather;
        localWeatherArray.add(localWeather);
    }


    public static void setDayOrNight(String dayNight){

        dayOrNight = dayNight;
        dayNightArray.add(dayOrNight);
    }


    public static void setLat(double latitude){

        lat = latitude;
        latsArray.add(latitude);
    }


    public static void setLon(double longitude){

        lon = longitude;
        lonsArray.add(longitude);
    }


    public static String getCityName(int position){
        return cityNameArray.get(position);
    }

    public static String getLocalTime(int position){
        return localTimeArray.get(position);
    }

    public static String getLocalTemp(int position){
        return localTempArray.get(position);
    }

    public static String getDayNight(int position){ return dayNightArray.get(position); }

    public static String getLocalWeather(int position){ return localWeatherArray.get(position); }

    public static Double getLat(int position){ return latsArray.get(position); }

    public static Double getLon(int position){ return lonsArray.get(position); }


    private static int lastLocationId = 0;


    public static ArrayList<LocationModel> createLocationsList(int numLocations) {

        ArrayList<LocationModel> locations = new ArrayList<LocationModel>();

        for(int i = 0; i < numLocations; i++) {

            locations.add(new LocationModel(localTimeArray.get(i), cityNameArray.get(i), localTempArray.get(i),
                    dayNightArray.get(i), localWeatherArray.get(i), latsArray.get(i), lonsArray.get(i)));

            lastLocationId++;
            System.out.println("lastLocationId: " + lastLocationId);
        }
        return locations;
    }
    // [ END: createLocationsList() ]

}
// [ END: LocationModel class]

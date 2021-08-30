package com.example.weathered.weathered;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WeatherModel extends ViewModel {

    // current (weather)
    MutableLiveData<String> temp_minLiveData;
    MutableLiveData<String> temp_maxLiveData;
    MutableLiveData<String> currFeelsLikeLiveData;
    MutableLiveData<String> currMainLiveData;
    MutableLiveData<String> currDescrLiveData;

    // sys
    MutableLiveData<String> sunriseLiveData, sunsetLiveData, countryLiveData;

    MutableLiveData<ArrayList<String>> weekdaysList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> dailyMinTempList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> dailyMaxTempList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> dailyMainList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> dailyDescrList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> cityNameList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> hourlyTimeList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> hourlyTempList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> hourlyMainList = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> dayNightList = new MutableLiveData<>();

    MutableLiveData<ArrayList<Integer>> dailyIconList = new MutableLiveData<>();

    MutableLiveData<ArrayList<LocalTime>> currTimeList = new MutableLiveData<>();
    MutableLiveData<ArrayList<LocalTime>> sunriseList = new MutableLiveData<>();
    MutableLiveData<ArrayList<LocalTime>> sunsetList = new MutableLiveData<>();

    MutableLiveData<List<StaticRvModel>> dailyItems = new MutableLiveData<>();
    MutableLiveData<List<DynamicRvModel>> hourlyItems = new MutableLiveData<>();

    MutableLiveData<String> currentCity, tempLiveData, dayNightLiveData, dailyMainLiveData;
    MutableLiveData<String> currHighLiveData, currLowLiveData;

    MutableLiveData<LocalTime> time_sunriseLiveData;
    MutableLiveData<LocalTime> time_sunsetLiveData;
    MutableLiveData<String> currTimeLiveData;

    FetchWeather fetch;

    // Create LiveData instance
    MutableLiveData<WeatherModel> liveWeather;


    // [ START: getWeather() ]
    public MutableLiveData<WeatherModel> getWeather() {

        if (liveWeather == null) {
            liveWeather = new MutableLiveData<>();
        }
        return liveWeather;
    }
    // [ END: getWeather() ]

    //---------------------------------------------------------------------------------------------
    // [ START: getCityNameList() ]
    // Method returns ArrayList of type String containing the names of cities.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrentCity(){
        if(currentCity == null){
            currentCity = new MutableLiveData<>();
        }
        return currentCity;
    }



    //---------------------------------------------------------------------------------------------
    // [ START: getCityNameList() ]
    // Method returns ArrayList of type String containing the names of cities.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getCityNameList(){

            if (cityNameList == null) {
                cityNameList = new MutableLiveData<>();
            }
        return cityNameList;

    }
    // [ END: getCityNameList() ]


    //---------------------------------------------------------------------------------------------
    // [ START: getCityName() ]
    // Method returns the name of a city at a give index
    //---------------------------------------------------------------------------------------------
    public String getCityName(int position) {

        return Objects.requireNonNull(cityNameList.getValue()).get(position);
    }

    //***************************** Current Weather Data ******************************************

    //---------------------------------------------------------------------------------------------
    // [ START: getCurrTemp() ]
    // Method returns LiveData for current temperature.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrTemp(){

        if(tempLiveData == null){

            tempLiveData = new MutableLiveData<>();
        }
        return tempLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrDescr() ]
    // Method returns LiveData for current weather description
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrDescr() {

        if (currDescrLiveData == null) {
            currDescrLiveData = new MutableLiveData<String>();
        }
        return currDescrLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrFeelsLike() ]
    // Method returns LiveData for what the temperature currently feels like
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrFeelsLike(){

        if(currFeelsLikeLiveData == null){

            currFeelsLikeLiveData = new MutableLiveData<String>();
        }
        return currFeelsLikeLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCountry() ]
    // Method returns LiveData for the current country
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCountry(){

        if(countryLiveData == null){

            countryLiveData = new MutableLiveData<String>();
        }
        return countryLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrMain() ]
    // Method returns LiveData for the current main weather description
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrMain() {

        if(currMainLiveData == null){

            currMainLiveData = new MutableLiveData<String>();
        }
        return currMainLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrHigh() ]
    // Method returns LiveData for current high temperature.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrHigh(){

        if(temp_maxLiveData == null){

            temp_maxLiveData = new MutableLiveData<>();
        }
        return temp_maxLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrLow() ]
    // Method returns LiveData for current low temperature.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getCurrLow() {

        if (temp_minLiveData == null) {

            temp_minLiveData = new MutableLiveData<>();
        }
        return temp_minLiveData;
    }

    //---------------------------------------------------------------------------------------------
    // [ START: getSunrise() ]
    // Method returns LiveData for time of sunrise at current location
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<LocalTime> getSunrise() {

        if(time_sunriseLiveData == null){

            time_sunriseLiveData = new MutableLiveData<>();
        }
        return time_sunriseLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getSunset() ]
    // Method returns LiveData for time of sunset at current location
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<LocalTime> getSunset() {

        if(time_sunsetLiveData == null) {

            time_sunsetLiveData = new MutableLiveData<>();
        }
        return time_sunsetLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDayNight() ]
    // Method returns LiveData telling us whether it is day or night
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getDayNight(){

        if(dayNightLiveData == null){

            dayNightLiveData = new MutableLiveData<>();
        }
        return dayNightLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getTimeSunrise() ]
    // Method returns LiveData of type String for time of sunrise
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getTimeSunrise(){
        if(sunriseLiveData == null){
            sunriseLiveData = new MutableLiveData<>();
        }
        return sunriseLiveData;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getTimeSunset() ]
    // Method returns LiveData of type String for time of sunset
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<String> getTimeSunset(){

        if(sunsetLiveData == null){

            sunsetLiveData = new MutableLiveData<>();
        }
        return sunsetLiveData;
    }


    // ****************************** DAILY WEATHER DATA ******************************************

    //---------------------------------------------------------------------------------------------
    // [ START: getWeekdayList() ]
    // Method returns ArrayList weekdays
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getWeekdayList(){

        if(weekdaysList == null){
            weekdaysList = new MutableLiveData<>();
        }
        return weekdaysList;
    }
    // [ END: getWeekdayList() ]


    //---------------------------------------------------------------------------------------------
    // [ START: getWeekday() ]
    // Method returns the weekday at a given index
    //---------------------------------------------------------------------------------------------
    public String getWeekday(int position){
        return Objects.requireNonNull(weekdaysList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMinTempList() ]
    // Method returns ArrayList of daily minimum temperatures
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getDailyMinTempList(){
        if(dailyMinTempList == null){
            dailyMinTempList = new MutableLiveData<>();
        }
        return dailyMinTempList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMin() ]
    // Method returns a String value for a daily minimum temperature
    //---------------------------------------------------------------------------------------------
    public String getDailyMin(int position){
        return Objects.requireNonNull(dailyMinTempList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMaxTempList() ]
    // Method returns ArrayList of daily maximum temperatures
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getDailyMaxTempList(){
        if(dailyMaxTempList == null){
            dailyMaxTempList = new MutableLiveData<>();
        }
        return dailyMaxTempList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMax() ]
    // Method returns the daily minimum temperature at a give index
    //---------------------------------------------------------------------------------------------
    public String getDailyMax(int position){
        return Objects.requireNonNull(dailyMaxTempList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMainList() ]
    // Method returns ArrayList of daily main weather descriptions
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getDailyMainList(){
        if(dailyMainList == null){
            dailyMainList = new MutableLiveData<>();
        }
        return dailyMainList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyMain() ]
    // Method returns the daily main weather description at a given index
    //---------------------------------------------------------------------------------------------
    public String getDailyMain(int position){
        return Objects.requireNonNull(dailyMainList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyDescrList() ]
    // Method returns an ArrayList of the daily weather descriptions
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getDailyDescrList(){
        if(dailyDescrList == null){
            dailyDescrList = new MutableLiveData<>();
        }
        return dailyDescrList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyDescr() ]
    // Method returns the daily weather description at a given index
    //---------------------------------------------------------------------------------------------
    public String getDailyDescr(int position){
        return Objects.requireNonNull(dailyDescrList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDayNightList() ]
    // Method returns ArrayList of type String containing values of day or night
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getDayNightList(){
        if(dayNightList == null){
            dayNightList = new MutableLiveData<>();
        }
        return dayNightList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyIconList() ]
    // Method returns an ArrayList of daily weather icons
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<Integer>> getDailyIconList(){
        if(dailyIconList == null){
            dailyIconList = new MutableLiveData<>();
        }
        return dailyIconList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getDailyIcon() ]
    // Method returns the daily weather icon at a given index
    //---------------------------------------------------------------------------------------------
    public int getDailyIcon(int imageId){
        return Objects.requireNonNull(dailyIconList.getValue()).get(imageId);
    }


    //***************************** HOURLY WEATHER DATA *******************************************


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrDailyTimeList() ]
    // Method returns an ArrayList of type String containing times of day
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<LocalTime>> getCurrDailyTimeList(){
        if(currTimeList == null){
            currTimeList = new MutableLiveData<>();
        }
        return currTimeList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrDailyTime() ]
    // Method returns an ArrayList of type String containing times of day
    //---------------------------------------------------------------------------------------------
    public LocalTime getCurrDailyTime(int position){
        return Objects.requireNonNull(currTimeList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrTimeList() ]
    // Method returns an ArrayList of type LocalTime containing times of day
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<LocalTime>> getCurrTimeList(){
        if(currTimeList == null){
            currTimeList = new MutableLiveData<>();
        }
        return currTimeList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getCurrTime() ]
    // Method returns value of type LocalTime at a given position
    //---------------------------------------------------------------------------------------------
    public LocalTime getCurrTime(int position){
        return Objects.requireNonNull(currTimeList.getValue()).get(position);
    }
    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyTimeList() ]
    // Method returns an ArrayList of type String containing hours of the day
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getHourlyTimeList(){
        if(hourlyTimeList == null){
            hourlyTimeList = new MutableLiveData<>();
        }
        return hourlyTimeList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyTime() ]
    // Method returns the hour of day at a given index
    //---------------------------------------------------------------------------------------------
    public String getHourlyTime(int position){
        return Objects.requireNonNull(hourlyTimeList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyTempList() ]
    // Method returns an ArrayList of type String containing temperatures at certain times of day.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getHourlyTempList(){
        if(hourlyTempList == null){
            hourlyTempList = new MutableLiveData<>();
        }
        return hourlyTempList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyTemp() ]
    // Method returns the hourly temp for a given time of day.
    //---------------------------------------------------------------------------------------------
    public String getHourlyTemp(int position){
        return Objects.requireNonNull(hourlyTempList.getValue()).get(position);
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyMainList() ]
    // Method returns an ArrayList of type String containing main descriptions for hourly weather.
    //---------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<String>> getHourlyMainList(){
        if(hourlyMainList == null){
            hourlyMainList = new MutableLiveData<>();
        }
        return hourlyMainList;
    }


    //---------------------------------------------------------------------------------------------
    // [ START: getHourlyMain() ]
    // Method returns the main weather description for a given hour
    //---------------------------------------------------------------------------------------------
    public String getHourlyMain(int position){
        return Objects.requireNonNull(hourlyMainList.getValue()).get(position);
    }

}
// [ END: WeatherModel class]

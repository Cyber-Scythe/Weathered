package com.example.weathered.weathered;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathered.R;
import com.example.weathered.weathered.Request.ApiRequest;
import com.example.weathered.weathered.Request.Language;
import com.example.weathered.weathered.Response.ApiResponse;
import com.example.weathered.weathered.Response.Current;
import com.example.weathered.weathered.Response.Daily;
import com.example.weathered.weathered.Response.Weather;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


public class NewLocation extends AppCompatActivity {

    double lat, lon;

    TextView cityTextView;
    TextView temperatureTextView;
    TextView descriptionTextView;
    TextView currHiLo, currHigh, currLow;

    ImageView weatherBackground;
    RelativeLayout relLayout;
    FrameLayout frameLayout;

    private RecyclerView dailyRv;
    private StaticRvAdapter staticRvAdapter;

    private RecyclerView hourlyRv;
    private DynamicRvAdapter dynamicRvAdapter;


    List<StaticRvModel> dailyItems = new ArrayList<>();
    List<DynamicRvModel> hourlyItems = new ArrayList<>();


    private static final String APP_ID = "";

    String currMain, currDescr, dayOfWeek, currTemp, dailyMin, dailyMax, dailyMain, dailyDescr;

    ArrayList<String> cityNameList = new ArrayList<>();
    ArrayList<String> weekdaysList = new ArrayList<>();
    ArrayList<String> dailyMinTempList = new ArrayList<>();
    ArrayList<String> dailyMaxTempList = new ArrayList<>();
    ArrayList<String> dailyMainList = new ArrayList<>();
    ArrayList<String> dailyDescrList = new ArrayList<>();
    ArrayList<Integer> dailyIconList = new ArrayList<>();

    ArrayList<String> hourlyTimeList = new ArrayList<>();
    ArrayList<String> hourlyTempList = new ArrayList<>();
    ArrayList<String> hourlyMainList = new ArrayList<>();
    ArrayList<LocalTime> currTimeList = new ArrayList<>();

    String city, currTime, dayNight, timeSunrise, timeSunset, time12Hr, high, low;

    LocalTime time_sunrise, time_sunset;

    int dailyLength, hourlyLength;

    WeatherModel wm = new WeatherModel();
    Conversion conversion = new Conversion();

    private WeatherModel newLocWeather;


    // [ START: onCreate() ]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_location);

        relLayout = findViewById(R.id.frag_contents);
        frameLayout = findViewById(R.id.container);
        cityTextView = findViewById(R.id.cityTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        currHigh = findViewById(R.id.currHigh);
        currLow = findViewById(R.id.currLow);

        dailyRv = findViewById(R.id.rv_daily);
        hourlyRv = findViewById(R.id.rv_hourly);


        // Set recycler view's layout manager
        dailyRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        hourlyRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dailyRv.setHasFixedSize(true);
        hourlyRv.setHasFixedSize(true);

        staticRvAdapter = new StaticRvAdapter(dailyItems, getApplicationContext());
        dailyRv.setAdapter(staticRvAdapter);

        dynamicRvAdapter = new DynamicRvAdapter(hourlyItems, getApplicationContext());
        hourlyRv.setAdapter(dynamicRvAdapter);


        Intent intent = getIntent();

        city = intent.getStringExtra("city");
        System.out.println("City in NewLocation class: " + city);

        cityNameList.add(city);

        wm.getCurrentCity().setValue(city);
        wm.getCityNameList().setValue(cityNameList);

        // Set TextView for city name
        cityTextView.setText(wm.getCurrentCity().getValue());

        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lng", 0);
        System.out.println("lat/lng: " + lat + " / " + lon);

        // Start new thread
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Current location Thread started running.");

                ApiClient apiClient = new ApiClient(APP_ID);

                ApiRequest apiRequest = new ApiRequest.Builder(lat, lon)
                        .lang(Language.en)
                        .build();

                apiClient.makeApiRequest(apiRequest, new OnWeatherResponseReceivedListener() {

                    // [ START: onSuccessfulResponse() ]
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccessfulResponse(ApiResponse response) {

                        long dt = response.getCurrent().getDt();
                        String timezone = response.getTimezone();


                        // GET CURRENT WEATHER DATA
                        Weather[] currWeather = response.getCurrent().getWeather();

                        // current main description
                        currMain = currWeather[0].getMain();
                        wm.getCurrMain().setValue(currMain);
                        System.out.println("currMain called with WM func: " + wm.getCurrMain().getValue());

                        // current description
                        currDescr = currWeather[0].getDescription();
                        wm.getCurrDescr().setValue(currDescr);

                        // current temperature
                        double temp = response.getCurrent().getTemp();
                        currTemp = conversion.convertTempToF(temp);
                        wm.getCurrTemp().setValue(currTemp);

                        // long value for sunrise and sunset
                        long sunriseLong = response.getCurrent().getSunrise();
                        long sunsetLong = response.getCurrent().getSunset();

                        // current locations local time
                        LocalTime time = Instant.ofEpochSecond(dt).atZone(ZoneId.of(timezone)).toLocalTime();

                        time_sunrise = Instant.ofEpochSecond(sunriseLong).
                                atZone(ZoneId.of(timezone)).toLocalTime().truncatedTo(ChronoUnit.MINUTES);

                        time_sunset = Instant.ofEpochSecond(sunsetLong).
                                atZone(ZoneId.of(timezone)).toLocalTime().truncatedTo(ChronoUnit.MINUTES);

                        timeSunrise = String.format(time_sunrise.toString(), DateTimeFormatter.ofPattern("h:m a"));
                        timeSunrise = timeSunrise.substring(1, 5) + " AM";
                        wm.getTimeSunrise().setValue(timeSunrise);

                        timeSunset = time_sunset.format(DateTimeFormatter.ofPattern("hh:mm a"));
                        timeSunset = timeSunset.substring(0, 5) + " PM";
                        wm.getTimeSunset().setValue(timeSunset);

                        // check if current time is before or after sunrise
                        if (time.isAfter(time_sunrise) || time.equals(time_sunrise)) {

                            dayNight = "day";

                        } else {

                            dayNight = "night";
                        }
                        wm.getDayNight().setValue(dayNight);

                        //setMainBackground(currMain, relativeLayout, dayNight);

                        // GET DAILY WEATHER DATA
                        Daily[] daily = response.getDaily();
                        dailyLength = daily.length;

                        for (int i = 0; i < dailyLength; i++) {

                            long dailyDt = daily[i].getDt();

                            // Convert UTC to day of the week
                            dayOfWeek = conversion.convertUtcToWeekday(dailyDt, timezone);
                            weekdaysList.add(dayOfWeek);
                            wm.getWeekdayList().setValue(weekdaysList);

                            //double dailyDayTemp = daily[i].getTemp().getDay();
                            double dailyMinTemp = daily[i].getTemp().getMin();
                            dailyMin = conversion.convertTempToF(dailyMinTemp);
                            dailyMinTempList.add(dailyMin);
                            wm.getDailyMinTempList().setValue(dailyMinTempList);
                            //System.out.println("dailyMin: " + dailyMin);

                            double dailyMaxTemp = daily[i].getTemp().getMax();
                            dailyMax = conversion.convertTempToF(dailyMaxTemp);
                            dailyMaxTempList.add(dailyMax);
                            wm.getDailyMaxTempList().setValue(dailyMaxTempList);


                            Weather[] dailyWeather = daily[i].getWeather();
                            long dailyId = dailyWeather[0].getID();
                            dailyMain = dailyWeather[0].getMain();
                            dailyMainList.add(dailyMain);
                            wm.getDailyMainList().setValue(dailyMainList);
                            //System.out.println("dailyMain: " + dailyMain);

                            int dailyIcon = getDailyIcon(dailyMain);
                            dailyIconList.add(dailyIcon);
                            wm.getDailyIconList().setValue(dailyIconList);

                            dailyDescr = dailyWeather[0].getDescription();
                            //System.out.println("dailyDescr: " + dailyDescr);

                            dailyMainList.add(dailyMain);
                            wm.getDailyMainList().setValue(dailyMainList);

                            dailyDescrList.add(dailyDescr);
                            wm.getDailyDescrList().setValue(dailyDescrList);
                        }

                        wm.getWeekdayList().setValue(weekdaysList);
                        wm.getDailyMinTempList().setValue(dailyMinTempList);


                        low = "L: " + dailyMinTempList.get(0);
                        wm.getCurrLow().setValue(low);

                        high = "H: " + dailyMaxTempList.get(0);
                        wm.getCurrHigh().setValue(high);


                        // GET HOURLY WEATHER DATA
                        Current[] hourlyWeather = response.getHourly();
                        hourlyLength = hourlyWeather.length;

                        for (int a = 0; a < hourlyLength; a++) {

                            long hourlyDt = hourlyWeather[a].getDt();

                            LocalTime currTime = Instant.ofEpochSecond(hourlyDt).
                                    atZone(ZoneId.of(timezone)).toLocalTime();

                            currTimeList.add(currTime);
                            wm.getCurrDailyTimeList().setValue(currTimeList);


                            if (currTime.truncatedTo(ChronoUnit.HOURS) == time_sunrise.truncatedTo(ChronoUnit.HOURS)) {
                                hourlyTimeList.add(a, String.format(currTime.toString(), DateTimeFormatter.ofPattern("hh:mm a")));
                                wm.getHourlyTimeList().setValue(hourlyTimeList);
                                //System.out.println("first if");

                            } else if (currTime.truncatedTo(ChronoUnit.HOURS) == time_sunset.truncatedTo(ChronoUnit.HOURS)) {
                                hourlyTimeList.add(a, String.format(currTime.toString(), DateTimeFormatter.ofPattern("hh:mm a")));
                                wm.getHourlyTimeList().setValue(hourlyTimeList);
                                //System.out.println("second if");

                            } else {

                                String hourlyTime = String.format(currTime.truncatedTo(ChronoUnit.HOURS).toString(), DateTimeFormatter.ofPattern(("h a")));
                                //System.out.println("hourlyTime: " + hourlyTime);

                                hourlyTimeList.add(hourlyTime);
                                wm.getHourlyTimeList().setValue(hourlyTimeList);

                                // System.out.println("else statement");
                            }

                            double hourlyTemp = hourlyWeather[a].getTemp();
                            String hourlyTempF = conversion.convertTempToF(hourlyTemp);
                            hourlyTempList.add(hourlyTempF);
                            wm.getHourlyTempList().setValue(hourlyTempList);
                            //System.out.println("hourlyTemp: " + hourlyTempF);

                            Weather[] hourly = hourlyWeather[0].getWeather();
                            String hourlyMain = hourly[0].getMain();
                            hourlyMainList.add(hourlyMain);
                            wm.getHourlyMainList().setValue(hourlyMainList);

                        } // end of for loop

                                    /*
                                    try {
                                        Thread.sleep(300000);
                                        System.out.println("Wait called on thread.");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    */

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        System.out.println("Inside runOnUiThread() ");
                                        setMainBackground(currMain, relLayout, dayNight);

                                        descriptionTextView.setText(wm.getCurrDescr().getValue());
                                        temperatureTextView.setText(wm.getCurrTemp().getValue());
                                        currHigh.setText(wm.getCurrHigh().getValue());
                                        currLow.setText(wm.getCurrLow().getValue());

                                        // get the current time for current location
                                        //currTime = LocalTime.now();

                                        // Add items to dailyItems RecyclerView
                                        for (int i = 0; i < dailyLength; i++) {

                                            dailyItems.add(new StaticRvModel(wm.getDailyIcon(i), wm.getWeekday(i),
                                                    wm.getDailyMax(i), wm.getDailyMin(i), wm.getDayNight().getValue()));

                                            staticRvAdapter.notifyItemInserted(i);
                                        }

                                        for (int a = 0; a < hourlyLength; a++) {

                                            hourlyItems.add(new DynamicRvModel(String.valueOf(wm.getHourlyTime(a)), wm.getHourlyTemp(a),
                                                    wm.getHourlyMain(a), time_sunrise, time_sunset, wm.getCurrTime(a)));

                                            dynamicRvAdapter.notifyItemInserted(a);
                                        }
                                        // End for-loop
                                    }
                                });
                            }
                        }).start();
                        // END new Thread Runnable
                    }
                    // [ END: onSuccessfulResponse() ]

                    // [ START: onFailedResponse() ]
                    @Override
                    public void onFailedResponse(Throwable t) {
                        Log.e("MainActivity", t.getMessage());
                    }
                    // [ END: onFailedResponse() ]
                });
            }
            // END run()
        }).start();
        // END currWeather thread



        // Get the ViewModel.
        newLocWeather = new ViewModelProvider(this).get(WeatherModel.class);

        //-----------------------------------------------------------------------------------------
        // Create the observer(s) which updates the UI.
        //-----------------------------------------------------------------------------------------

        // CurrMain observer
        final Observer<String> currMainObserver = new Observer<String>() {
            @Override
            public void onChanged(String currMain) {

                setMainBackground(currMain, relLayout, wm.getDayNight().getValue());
            }
        };

        // City name observer
        final Observer<String> cityObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String cityName) {
                 // Update the UI, in this case, a TextView.
                cityTextView.setText(cityName);
             }
        };

        // Temperature observer
        final Observer<String> tempObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String temp) {
                    // Update the UI, in this case, a TextView.
                    temperatureTextView.setText(temp);
            }
        };

        // Description observer
        final Observer<String> descrObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String currDescr) {
                // Update the UI, in this case, a TextView.
                descriptionTextView.setText(currDescr);
            }
        };

        // High temperature observer
        final Observer<String> highObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // Update the UI (TextView)
                currHigh.setText(high);
            }
        };

        // Low temperature observer
        final Observer<String> lowObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // Update the UI (TextView)
                currLow.setText(low);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        newLocWeather.getCurrMain().observe(this, currMainObserver);
        newLocWeather.getCurrentCity().observe(this, cityObserver);
        newLocWeather.getCurrTemp().observe(this, tempObserver);
        newLocWeather.getCurrDescr().observe(this, descrObserver);
        newLocWeather.getCurrHigh().observe(this, highObserver);
        newLocWeather.getCurrLow().observe(this, lowObserver);
    }
    // [ END: onCreate() ]



    // [ START: setMainBackground() ]
    public void setMainBackground(String currMain, RelativeLayout relLayout, String dayNight) {

        switch (currMain) {

            case "Clear":
                if (dayNight.contentEquals("day")) {

                    relLayout.setBackgroundResource(R.drawable.sunny_sky);
                } else {
                    relLayout.setBackgroundResource(R.drawable.night_sky_wil_stewart);
                }
                break;

            case "Clouds":
                if (dayNight.contentEquals("day")) {

                    relLayout.setBackgroundResource(R.drawable.cloudy);
                } else {
                    relLayout.setBackgroundResource(R.drawable.cloudy_night_ashwini_chaudhary);
                }
                break;

            case "Rain":
                if (dayNight.contentEquals("day")) {

                    relLayout.setBackgroundResource(R.drawable.rain);
                } else {
                    relLayout.setBackgroundResource(R.drawable.rain_night_yang_zerony);
                }
                break;

            case "Drizzle":
                if (dayNight.contentEquals("day")) {

                    relLayout.setBackgroundResource(R.drawable.drizzle);
                } else {
                    relLayout.setBackgroundResource(R.drawable.rain_night_yang_zerony);
                }
                break;

            case "Thunderstorm":
                if (dayNight.contentEquals("day")) {

                    relLayout.setBackgroundResource(R.drawable.lightning);
                } else {
                    relLayout.setBackgroundResource(R.drawable.thunderstorm_night_brandon_morgan);
                }
                break;

            case "Snow":
                relLayout.setBackgroundResource(R.drawable.snow);
                break;

            case "Mist":
                relLayout.setBackgroundResource(R.drawable.mist);
                break;

            case "Smoke":
                relLayout.setBackgroundResource(R.drawable.smoke);
                break;

            case "Haze":
                relLayout.setBackgroundResource(R.drawable.haze);
                break;

            case "Dust":
                relLayout.setBackgroundResource(R.drawable.dust);
                break;

            case "Fog":
                if (dayNight.contentEquals("day")) {
                    relLayout.setBackgroundResource(R.drawable.fog);
                } else {
                    relLayout.setBackgroundResource(R.drawable.foggy_night_harald_pliessnig);
                }
                break;

            case "Sand":
                relLayout.setBackgroundResource(R.drawable.sand);
                break;

            case "Ash":
                relLayout.setBackgroundResource(R.drawable.ash);
                break;

            case "Squall":
                relLayout.setBackgroundResource(R.drawable.squall);
                break;

            case "Tornado":
                relLayout.setBackgroundResource(R.drawable.tornado);
                break;

            default:
                System.out.println("No matching weather string");

        }
    }
    // [ END: setMainBackground() ]


    // [START: getDailyIcon() ]
    public int getDailyIcon(String dailyMain){

        int dailyIcon = 0;

        switch (dailyMain) {

            case "Clear":

                if (dayNight.contentEquals("day")) {
                    dailyIcon = R.drawable.ic_brightness;
                } else {
                    dailyIcon = R.drawable.ic_moon_clear;
                }
                break;

            case "Cloudy":

            case "Clouds":

                if (dayNight.contentEquals("day")) {
                    dailyIcon = R.drawable.ic_cloudy;
                } else {
                    dailyIcon = R.drawable.ic_cloudy_night;
                }
                break;

            case "Mist":

            case "Fog":

                dailyIcon = R.drawable.ic_foggy;
                break;

            case "drizzle":

            case "Rain":

                if (dayNight.contentEquals("day")) {
                    dailyIcon = R.drawable.ic_rainy;
                } else if (dayNight.contentEquals("night")) {
                    dailyIcon = R.drawable.ic_moon_rain;
                }
                break;

            case "Haze":
                dailyIcon = R.drawable.ic_haze;
                break;

            case "Thunderstorm":
                dailyIcon = R.drawable.ic_storm;
                break;

            case "Snow":
                dailyIcon = R.drawable.ic_snowing;
                break;

            case "Tornado":
                dailyIcon = R.drawable.ic_tornado;
                break;

            case "Squall":
                dailyIcon = R.drawable.ic_wind;
                break;

            default:
                dailyIcon = R.drawable.ic_tornado;
        }

        return dailyIcon;
    }
    // [ END: getDailyIcon() ]


    // [ START: onMenuClick() ]
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onMenuClick (MenuItem menuItem){

        System.out.println("menu Item clicked");

        currTime = LocalTime.now().toString();

        Intent intent = new Intent(getApplicationContext(), WeatherLocations.class);

        intent.putExtra("time", currTime);
        intent.putExtra("city", city);
        intent.putExtra("temp", currTemp);
        intent.putExtra("weather", currMain);
        intent.putExtra("dayNight", dayNight);

        startActivity(intent);
    }
    // [ END: onMenuClick() ]


    // [ START: onClickCancel() ]
    public void onClickCancel(View v){

        // return to LocationsWeather activity
        Intent cancelIntent = new Intent(getApplicationContext(), WeatherLocations.class);

        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(cancelIntent);

    }
    // [ END: onClickCancel() ]


    // [ START: onClickAdd() ]
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickAdd(View v) throws IOException {

        WeatherLocations wl = new WeatherLocations();

        System.out.println("locations array size beginning onClickAdd(): " +
                WeatherLocations.locations.size());

        // cityNameArray contains city
        if(wl.containsCity(city)) {

            int index = LocationModel.cityNameArray.indexOf(city);
            //WeatherLocationFragment.newInstance(lat, lon, city);

            wl.weatherFrags.get(index).onStart();
/*
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putDouble("lat", lat);
            bundle.putDouble("lon", lon);
            bundle.putString("city", city);

            transaction.replace(R.id.container, WeatherLocationFragment.class, null);
            transaction.addToBackStack(null);
            transaction.commit();
*/
            // cityNameArray does NOT contain city
        }else{

            wl.setCities(city);
            wl.setLats(lat);
            wl.setLons(lon);

            wl.weatherFrags.add(WeatherLocationFragment.newInstance(lat, lon, city));

            System.out.println("size of lats: " + wl.lats.size());

            LocationModel.setLocalTime(time12Hr);
            LocationModel.setCityName(city);
            LocationModel.setLocalTemp(currTemp);
            LocationModel.setDayOrNight(dayNight);
            LocationModel.setLocalWeather(currMain);
            LocationModel.setLat(lat);
            LocationModel.setLon(lon);


            WeatherLocationFragment fragment = WeatherLocationFragment.newInstance(lat, lon, city);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    fragment).commit();

            /*
            WeatherLocationFragment weatherFrag = new WeatherLocationFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putDouble("lat", lat);
            bundle.putDouble("lon", lon);
            bundle.putString("city", city);

            weatherFrag.setArguments(bundle);
            transaction.replace(R.id.container, weatherFrag, null);
            transaction.addToBackStack(null);
            transaction.commit();
            */
        }

    }
    // [ END: onClickAdd() ]

}
// [ END: NewLocation class]
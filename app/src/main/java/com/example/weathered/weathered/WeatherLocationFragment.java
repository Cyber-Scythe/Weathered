package com.example.weathered.weathered;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weathered.R;
import com.example.weathered.weathered.Request.ApiRequest;
import com.example.weathered.weathered.Request.Language;
import com.example.weathered.weathered.Response.ApiResponse;
import com.example.weathered.weathered.Response.Current;
import com.example.weathered.weathered.Response.Daily;
import com.example.weathered.weathered.Response.Weather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class WeatherLocationFragment extends Fragment {


    double lat, lon;

    TextView cityTextView;
    TextView temperatureTextView;
    TextView descriptionTextView;
    TextView currHigh, currLow;

    Button placesButton;

    RelativeLayout relLayout;
    FrameLayout frameLayout;

    RecyclerView dailyRv;
    StaticRvAdapter staticRvAdapter;

    RecyclerView hourlyRv;
    DynamicRvAdapter dynamicRvAdapter;


    List<StaticRvModel> dailyItems = new ArrayList<>();
    List<DynamicRvModel> hourlyItems = new ArrayList<>();


    private static final String APP_ID = "";

    String currMain, currDescr, dayOfWeek, currTemp, dailyMin, dailyMax, dailyMain, dailyDescr;

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

    ArrayList<WeatherLocationFragment> weatherFrags = new ArrayList<>();

    String city, dayNight, timeSunrise, timeSunset, time, high, low;

    int dailyLength, hourlyLength;

    LocalTime time_sunrise;
    LocalTime time_sunset;

    WeatherModel wm = new WeatherModel();
    Conversion conversion = new Conversion();

    WeatherModel weatherFrag;


    // [ START: onCreateView() ]
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_weather_location, container, false);

        relLayout = root.findViewById(R.id.frag_contents);
        frameLayout = root.findViewById(R.id.container);
        cityTextView = root.findViewById(R.id.cityTextView);
        descriptionTextView = root.findViewById(R.id.descriptionTextView);
        temperatureTextView = root.findViewById(R.id.temperatureTextView);
        currHigh = root.findViewById(R.id.currHigh);
        currLow = root.findViewById(R.id.currLow);
        placesButton = root.findViewById(R.id.placesButton);

        // [ START: OnClickListener() ]
        placesButton.setOnClickListener(new View.OnClickListener() {

            // [ START: onClick() ]
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){

                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                time = dateFormat.format(new Date());

                // If time starts with 0
                if(time.startsWith("0")) {
                    time = time.substring(1);
                }

                Intent intent = new Intent(getActivity(), WeatherLocations.class);
                startActivity(intent);
            }
            // [ END: onClick() ]
        });
        // [ END: OnClickListener() ]

        dailyRv = root.findViewById(R.id.rv_daily);
        hourlyRv = root.findViewById(R.id.rv_hourly);

        // Set recycler view's layout manager
        dailyRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        hourlyRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        dailyRv.setHasFixedSize(true);
        hourlyRv.setHasFixedSize(true);

        staticRvAdapter = new StaticRvAdapter(dailyItems, getContext());
        dailyRv.setAdapter(staticRvAdapter);

        dynamicRvAdapter = new DynamicRvAdapter(hourlyItems, getContext());
        hourlyRv.setAdapter(dynamicRvAdapter);

        // Assert Error: bundle arguments are probably null
        // assert getArguments() != null;

        lat = getArguments().getDouble("lat");
        lon = getArguments().getDouble("lon");
        city = getArguments().getString("city");

        System.out.print("City in WeatherLocationFragment: " + city);
        cityTextView.setText(city);

        System.out.println("    lat/lng: " + lat + " / " + lon);

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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        System.out.println("Inside runOnUiThread() ");

                                        // Update UI components
                                        setMainBackground(Objects.requireNonNull(wm.getCurrMain().getValue()),
                                                relLayout,
                                                wm.getDayNight().getValue());

                                        descriptionTextView.setText(wm.getCurrDescr().getValue());
                                        temperatureTextView.setText(wm.getCurrTemp().getValue());
                                        currHigh.setText(wm.getCurrHigh().getValue());
                                        currLow.setText(wm.getCurrLow().getValue());

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

/*
        System.out.println("Inside runOnUiThread() ");
        // Update UI components
        setMainBackground(Objects.requireNonNull(wm.getCurrMain().getValue()),
                          relLayout, wm.getDayNight().getValue());

        descriptionTextView.setText(wm.getCurrDescr().getValue());
        temperatureTextView.setText(wm.getCurrTemp().getValue());
        currHigh.setText(wm.getCurrHigh().getValue());
        currLow.setText(wm.getCurrLow().getValue());


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
*/

        // Get the ViewModel.
        weatherFrag = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(WeatherModel.class);

        //-----------------------------------------------------------------------------------------
        // Create the observer(s) which updates the UI.
        //-----------------------------------------------------------------------------------------

        // CurrMain observer
        final Observer<String> currMainObserver = new Observer<String>() {
            @Override
            public void onChanged(String currMain) {

                setMainBackground(Objects.requireNonNull(wm.getCurrMain().getValue()), relLayout, wm.getDayNight().getValue());
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
        weatherFrag.getCurrMain().observe(getViewLifecycleOwner(), currMainObserver);
        weatherFrag.getCurrentCity().observe(getViewLifecycleOwner(), cityObserver);
        weatherFrag.getCurrTemp().observe(getViewLifecycleOwner(), tempObserver);
        weatherFrag.getCurrDescr().observe(getViewLifecycleOwner(), descrObserver);
        weatherFrag.getCurrHigh().observe(getViewLifecycleOwner(), highObserver);
        weatherFrag.getCurrLow().observe(getViewLifecycleOwner(), lowObserver);

        return root;
    }
    // [ END: onCreateView() ]


    // [ START: newInstance() ]
    public static WeatherLocationFragment newInstance(double lat, double lon, String city) {

        WeatherLocationFragment f = new WeatherLocationFragment();

        Bundle b = new Bundle();
        b.putDouble("lat", lat);
        b.putDouble("lon", lon);
        b.putString("city", city);

        f.setArguments(b);

        return f;
    }
    // [ END: newInstance() ]


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

}
// [ END: WeatherLocationFragment ]
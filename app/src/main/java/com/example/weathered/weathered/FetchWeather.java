package com.example.weathered.weathered;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weathered.R;
import com.example.weathered.weathered.Request.ApiRequest;
import com.example.weathered.weathered.Request.Language;
import com.example.weathered.weathered.Response.ApiResponse;
import com.example.weathered.weathered.Response.Current;
import com.example.weathered.weathered.Response.Daily;
import com.example.weathered.weathered.Response.Weather;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;


// [ START: FetchWether class ]
@RequiresApi(api = Build.VERSION_CODES.N)
public class FetchWeather {

    Double lat, lon;
    String cityName;

    // current (weather)
    String temperature, temp_min, temp_max; // current temperature
    String currFeelsLike; // what current temp feels like

    // (current) weather
    static String currMain, currDescr, dayOfWeek, currTemp, dailyMin, dailyMax;

    // sys
    static String timeSunrise, timeSunset, country;

    static ArrayList<String> weekdaysList = new ArrayList<>();
    static ArrayList<String> dailyMinTempList = new ArrayList<>();
    static ArrayList<String> dailyMaxTempList = new ArrayList<>();
    static ArrayList<String> dailyMainList = new ArrayList<>();
    static ArrayList<String> dailyDescrList = new ArrayList<>();
    ArrayList<String> cityNameList = new ArrayList<>();

    static ArrayList<Integer> dailyIconList = new ArrayList<>();

    static ArrayList<String> hourlyTimeList = new ArrayList<>();
    static ArrayList<String> hourlyTempList = new ArrayList<>();
    static ArrayList<String> hourlyMainList = new ArrayList<>();
    static ArrayList<LocalTime> currTimeList = new ArrayList<>();
    ArrayList<String> dayNightList = new ArrayList<>();
    ArrayList<LocalTime> sunriseList = new ArrayList<>();
    ArrayList<LocalTime> sunsetList = new ArrayList<>();

    List<StaticRvModel> dailyItems = new ArrayList<>();
    List<DynamicRvModel> hourlyItems = new ArrayList<>();

    static String dayNight, dailyMain, dailyDescr;

    static LocalTime time_sunrise;
    static LocalTime time_sunset;
    static String currTime, low, high;

    Conversion conversion = new Conversion();
    WeatherModel wm = new WeatherModel();

    private static final String APP_ID = "79cda71f16ccc933c6b2b2577657ae66";

    static int dailyLength, hourlyLength;


    // [ START: FetchWeather() ]
    FetchWeather(Double lat, Double lon) {

        this.lat = lat;
        this.lon = lon;
    }
    // [ END: FetchWeather() ]


    // [ START: run() ]
    public void run() {

        System.out.println("FetchWeather Thread started running.");

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

                try {
                    Thread.sleep(600000);
                    System.out.println("Sleep called.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // [ END: onSuccessfulResponse() ]

            // [ START: onFailedResponse() ]
            @Override
            public void onFailedResponse (Throwable t){
                Log.e("MainActivity", t.getMessage());
            }
            // [ END: onFailedResponse() ]

        });
    }
    // [ END: run() ]


    // [ START: main() ]
    public void main(String args[]) {

    }
    // [ END: main()]


    // [START: getDailyIcon]
    public int getDailyIcon(String dailyMain) {

        int imageId;

        switch (dailyMain) {
            case "Clear":

                imageId = R.drawable.ic_brightness;
                break;

            case "Clouds":

            case "Cloudy":

                imageId = R.drawable.ic_cloudy;
                break;

            case "Mist":

            case "Fog":

                imageId = R.drawable.ic_foggy;
                break;

            case "drizzle":

            case "Rain":

                imageId = R.drawable.ic_rainy;
                break;

            case "Haze":
                imageId = R.drawable.ic_haze;
                break;

            case "Thunderstorm":
                imageId = R.drawable.ic_storm;
                break;

            case "Snow":
                imageId = R.drawable.ic_snowing;
                break;

            case "Tornado":
                imageId = R.drawable.ic_tornado;
                break;

            case "Squall":
                imageId = R.drawable.ic_wind;
                break;

            default:
                imageId = R.drawable.ic_brightness;
        }

        return imageId;
    }
    // [END: getDailyIcon() ]
}
// [ END: FetchWeather class ]
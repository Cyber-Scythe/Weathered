package com.example.weathered.weathered;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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



public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {


    private List<String> cities;
    private List<Double> lons;
    private List<Double> lats;

    private LayoutInflater mInflater;

    private ViewPager2 viewPager2;

    private int[] colorArray = {android.R.color.black,
            android.R.color.holo_blue_bright,
            android.R.color.holo_red_dark};

    String currMain, currDescr, dayOfWeek, currTemp, dailyMin, dailyMax, dailyMain, dailyDescr, dayNight;

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

    List<StaticRvModel> dailyItems = new ArrayList<>();
    List<DynamicRvModel> hourlyItems = new ArrayList<>();

    String timeSunrise, timeSunset, time;

    LocalTime time_sunrise, time_sunset, currTime;

    RecyclerView dailyRv, hourlyRv;

    RelativeLayout relativeLayout;

    Context context;


    ViewPagerAdapter(Context context, List<Double> lats, List<Double> lngs, List<String> cities, ViewPager2 viewPager2){

        this.mInflater = LayoutInflater.from(context);
        this.cities = cities;
        this.lats = lats;
        this.lons = lngs;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.activity_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {

        dailyRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        hourlyRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        dailyRv.setHasFixedSize(true);
        hourlyRv.setHasFixedSize(true);

        StaticRvAdapter staticRvAdapter = new StaticRvAdapter(dailyItems, context);
        dailyRv.setAdapter(staticRvAdapter);

        DynamicRvAdapter dynamicRvAdapter = new DynamicRvAdapter(hourlyItems, context);
        hourlyRv.setAdapter(dynamicRvAdapter);

        String city = cities.get(position);
        holder.cityTextView.setText(city);

        double lat = lats.get(position);
        double lon = lons.get(position);

        ApiClient apiClient = new ApiClient(String.valueOf(R.string.APP_ID));

        ApiRequest apiRequest = new ApiRequest.Builder(lat, lon)
                .lang(Language.en)
                .build();

        apiClient.makeApiRequest(apiRequest, new OnWeatherResponseReceivedListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccessfulResponse(ApiResponse response) {

                Conversion conversion = new Conversion();

                long dt = response.getCurrent().getDt();
                String timezone = response.getTimezone();

                // Get current weather data
                Weather[] currWeather = response.getCurrent().getWeather();

                currMain = currWeather[0].getMain();
                currDescr = currWeather[0].getDescription();
                double temp = response.getCurrent().getTemp();
                long sunriseLong = response.getCurrent().getSunrise();
                long sunsetLong = response.getCurrent().getSunset();
                LocalTime time = Instant.ofEpochSecond(dt).atZone(ZoneId.of(timezone)).toLocalTime();

                time_sunrise = Instant.ofEpochSecond(sunriseLong).
                        atZone(ZoneId.of(timezone)).toLocalTime().truncatedTo(ChronoUnit.MINUTES);

                time_sunset = Instant.ofEpochSecond(sunsetLong).
                        atZone(ZoneId.of(timezone)).toLocalTime().truncatedTo(ChronoUnit.MINUTES);

                timeSunrise = String.format(time_sunrise.toString(), DateTimeFormatter.ofPattern("h:m a"));
                timeSunrise = timeSunrise.substring(1, 5) + " AM";

                timeSunset = time_sunset.format(DateTimeFormatter.ofPattern("hh:mm a"));
                timeSunset = timeSunset.substring(0, 5) + " PM";

                // check if current time is before or after sunrise
                if (time.isAfter(time_sunrise) || time.equals(time_sunrise)) {

                    dayNight = "day";

                } else {

                    dayNight = "night";
                }

                setMainBackground(currMain, relativeLayout, dayNight);


                // Get daily weather data
                Daily[] daily = response.getDaily();

                for (int i = 0; i < daily.length; i++) {

                    long dailyDt = daily[i].getDt();

                    // Convert UTC to day of the week
                    dayOfWeek = conversion.convertUtcToWeekday(dailyDt, timezone);

                    weekdaysList.add(dayOfWeek);

                    double dailyMinTemp = daily[i].getTemp().getMin();
                    dailyMin = conversion.convertTempToF(dailyMinTemp);
                    dailyMinTempList.add(dailyMin);

                    double dailyMaxTemp = daily[i].getTemp().getMax();
                    dailyMax = conversion.convertTempToF(dailyMaxTemp);
                    dailyMaxTempList.add(dailyMax);

                    // Get daily weather data
                    Weather[] dailyWeather = daily[i].getWeather();

                    long dailyId = dailyWeather[0].getID();
                    dailyMain = dailyWeather[0].getMain();

                    int dailyIcon = getdailyIcon(dailyMain);
                    dailyIconList.add(dailyIcon);

                    dailyDescr = dailyWeather[0].getDescription();

                    dailyMainList.add(dailyMain);
                    dailyDescrList.add(dailyDescr);


                    dailyItems.add(new StaticRvModel(dailyIconList.get(i), weekdaysList.get(i), dailyMaxTempList.get(i), dailyMinTempList.get(i), dayNight));
                    staticRvAdapter.notifyItemInserted(i);
                }

                String low = "L: " + dailyMinTempList.get(0);
                String high = "H: " + dailyMaxTempList.get(0);

                holder.currLow.setText(low);
                holder.currHigh.setText(high);


                Current[] hourlyWeather = response.getHourly();

                for (int a = 0; a < hourlyWeather.length; a++) {

                    long hourlyDt = hourlyWeather[a].getDt();

                    LocalTime currTime = Instant.ofEpochSecond(hourlyDt).
                            atZone(ZoneId.of(timezone)).toLocalTime();

                    currTimeList.add(currTime);

                    if (currTime.truncatedTo(ChronoUnit.HOURS) == time_sunrise.truncatedTo(ChronoUnit.HOURS)) {
                        hourlyTimeList.add(a, String.format(currTime.toString(), DateTimeFormatter.ofPattern("hh:mm a")));

                    } else if (currTime.truncatedTo(ChronoUnit.HOURS) == time_sunset.truncatedTo(ChronoUnit.HOURS)) {
                        hourlyTimeList.add(a, String.format(currTime.toString(), DateTimeFormatter.ofPattern("hh:mm a")));

                    } else {

                        String hourlyTime = String.format(currTime.truncatedTo(ChronoUnit.HOURS).toString(), DateTimeFormatter.ofPattern(("h a")));

                        hourlyTimeList.add(hourlyTime);
                    }

                    double hourlyTemp = hourlyWeather[a].getTemp();
                    String hourlyTempF = conversion.convertTempToF(hourlyTemp);
                    hourlyTempList.add(hourlyTempF);

                    Weather[] hourly = hourlyWeather[0].getWeather();
                    String hourlyMain = hourly[0].getMain();
                    hourlyMainList.add(hourlyMain);

                    hourlyItems.add(new DynamicRvModel(hourlyTimeList.get(a),
                            hourlyTempList.get(a),
                            hourlyMainList.get(a),
                            time_sunrise,
                            time_sunset,
                            currTimeList.get(a)));

                    dynamicRvAdapter.notifyItemInserted(a);

                } // end of for loop

            }

            @Override
            public void onFailedResponse(Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        FrameLayout frameLayout;
        RelativeLayout relativeLayout;
        TextView cityTextView;
        TextView descriptionTextView;
        TextView temperatureTextView;
        TextView currHigh;
        TextView currLow;

        RecyclerView dailyRv;
        RecyclerView hourlyRv;

        Button placesButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = itemView.findViewById(R.id.container);
            relativeLayout = itemView.findViewById(R.id.frag_contents);

            cityTextView = itemView.findViewById(R.id.cityTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            currHigh = itemView.findViewById(R.id.currHigh);
            currLow = itemView.findViewById(R.id.currLow);

            dailyRv = itemView.findViewById(R.id.rv_daily);
            hourlyRv = itemView.findViewById(R.id.rv_hourly);

            placesButton = itemView.findViewById(R.id.placesButton);
        }
    }

    // [START: getDailyIcon]
    public int getdailyIcon(String dailyMain){

        int imageId;

        switch(dailyMain) {
            case "Clear":

                imageId =  R.drawable.ic_brightness;
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
                imageId =  R.drawable.ic_tornado;
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
}
// [ END: ViewPagerAdapter class ]


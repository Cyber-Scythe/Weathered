package com.example.weathered.weathered;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathered.R;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DynamicRvAdapter extends RecyclerView.Adapter<com.example.weathered.weathered.DynamicRvAdapter.ViewHolder> {

    Context context;
    List<com.example.weathered.weathered.DynamicRvModel> hourlyItems;

    String hrlyMain, dayNight;

    public DynamicRvAdapter(List<com.example.weathered.weathered.DynamicRvModel> hourlyItems, Context context){

        this.context = context;
        this.hourlyItems = hourlyItems;

    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public com.example.weathered.weathered.DynamicRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hourly, parent, false);
        return new ViewHolder(v);
    }


    // Involves populating data into the item through holder
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(com.example.weathered.weathered.DynamicRvAdapter.ViewHolder holder, int position) {

        com.example.weathered.weathered.DynamicRvModel hourly = hourlyItems.get(position);

        LocalTime sunrise = hourly.getSunrise();
        LocalTime sunset = hourly.getSunset();
        LocalTime currTime = hourly.getCurrTime();
        String hourlyTime = hourly.getTime();


        if(currTime.isAfter(sunrise) || currTime == sunrise){
            dayNight = "day";
        }else if(currTime.isBefore(sunrise) || currTime.isAfter(sunset) || currTime == sunset){
            dayNight ="night";
        }

        if(position == 0) {

            String now = "now";
            holder.timeTextView.setText(now);

            holder.tempTextView.setText(hourly.getTemp());

            int iconId = getHourlyIcon(hourly.getHourlyMain(), dayNight);
            holder.weatherIcon.setImageResource(iconId);

        }else{


            // Set item views based on your views and data model
            if(currTime.truncatedTo(ChronoUnit.HOURS) == sunrise.truncatedTo(ChronoUnit.HOURS)) {

                System.out.println("currTime = sunrise");
                // convert time from 24hr to 12hr
                String time_12hr = LocalTime.parse(sunrise.toString(), DateTimeFormatter.ofPattern("H:mm")).format(DateTimeFormatter.ofPattern("h:mm a"));

                holder.timeTextView.setText(time_12hr);
                holder.timeTextView.setTextSize(12);

                String rise = "sunrise";
                holder.tempTextView.setText(rise);
                holder.tempTextView.setTextSize(12);

                holder.weatherIcon.setImageResource(R.drawable.sunrise);

            }else if(currTime.truncatedTo(ChronoUnit.HOURS) == sunset.truncatedTo(ChronoUnit.HOURS)){

                System.out.println("currTime = sunset");
                String time_12hr = LocalTime.parse(sunset.toString(), DateTimeFormatter.ofPattern("H:mm")).format(DateTimeFormatter.ofPattern("h:mm a"));

                holder.timeTextView.setText(time_12hr);
                holder.timeTextView.setTextSize(12);

                String set = "sunset";
                holder.tempTextView.setText(set);
                holder.tempTextView.setTextSize(12);

                holder.weatherIcon.setImageResource(R.drawable.sunset);

            } else {

                String time_12hr = LocalTime.parse(currTime.toString(), DateTimeFormatter.ofPattern("H:mm")).format(DateTimeFormatter.ofPattern("h a"));
                holder.timeTextView.setText(time_12hr);
                holder.tempTextView.setText(hourly.getTemp());

                hrlyMain = hourly.getHourlyMain();


                if(currTime.isBefore(sunrise) || currTime.isAfter(sunset))  {

                    dayNight = "night";
                    holder.weatherIcon.setImageResource(getHourlyIcon(hrlyMain, dayNight));

                } else if(currTime.isAfter(sunrise) || currTime.isBefore(sunset)) {

                    dayNight = "day";
                    holder.weatherIcon.setImageResource(getHourlyIcon(hrlyMain, dayNight));
                }
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {

        return hourlyItems.size();
    }



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView timeTextView;
        public TextView tempTextView;
        public ImageView weatherIcon;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            tempTextView = (TextView) itemView.findViewById(R.id.tempTextView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.hourlyWeather);

        } // END ViewHolder
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getHourlyIcon(String hrlyMain, String dayNight){

        int iconId;

        switch (hrlyMain) {

            case "Clear":

                if (dayNight.contentEquals("day")) {
                    iconId = R.drawable.ic_brightness;
                } else {
                    iconId = R.drawable.ic_moon_clear;
                }
                break;

            case "Clouds":

            case "Cloudy":

                if (dayNight.contentEquals("day")) {
                    iconId = R.drawable.ic_cloudy;
                } else {
                    iconId = R.drawable.ic_cloudy_night;
                }
                break;

            case "Mist":

            case "Fog":

                iconId = R.drawable.ic_foggy;
                break;

            case "drizzle":

            case "Rain":

                if (dayNight.contentEquals("day")) {
                    iconId = R.drawable.ic_rainy;
                } else {
                    iconId = R.drawable.ic_moon_rain;
                }
                break;

            case "Haze":
                iconId = R.drawable.ic_haze;
                break;

            case "Thunderstorm":
                iconId = R.drawable.ic_storm;
                break;

            case "Snow":
                iconId = R.drawable.ic_snowing;
                break;

            case "Tornado":
                iconId = R.drawable.ic_tornado;
                break;

            case "Squall":
                iconId = R.drawable.ic_wind;
                break;

            default: iconId = R.drawable.ic_launcher_background;
        }

        return iconId;
    }
}
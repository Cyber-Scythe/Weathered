package com.example.weathered.weathered;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weathered.R;

import java.util.ArrayList;
import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {

    ArrayList<String> localTimes;
    ArrayList<String> cityNames;
    ArrayList<String> localTemps;
    ArrayList<String> dayNightArray;
    ArrayList<String> weatherArray;
    ArrayList<LocationModel> locations;


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        String city, currTemp, dayNight, weather;
        TextView localTime, currLocTemp, currCityName;
        ImageView currWeatherBackground;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            localTime = (TextView) itemView.findViewById(R.id.localTime);
            currLocTemp = (TextView) itemView.findViewById(R.id.currLocTemp);
            currCityName = (TextView) itemView.findViewById(R.id.currCityName);
            currWeatherBackground = (ImageView) itemView.findViewById(R.id.currWeatherBackground);
        }
    }

    // Store a member variable for the contacts
    private List<LocationModel> mLocations;

    // Pass in the contact array into the constructor
    public LocationsAdapter(List<LocationModel> locations) {
        mLocations = locations;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public LocationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View locationView = inflater.inflate(R.layout.rv_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(locationView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(LocationsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        LocationModel location = mLocations.get(position);

        // Set item views based on your views and data model
        TextView localTimeTextView = holder.localTime;
        String time = LocationModel.getLocalTime(position);
        localTimeTextView.setText(time);

        TextView cityTextView = holder.currCityName;
        cityTextView.setText(LocationModel.getCityName(position));

        TextView locTempTextView = holder.currLocTemp;
        locTempTextView.setText(LocationModel.getLocalTemp(position));

        String dayNight = LocationModel.getDayNight(position);

        ImageView weatherBackground = holder.currWeatherBackground;
        setWeatherBackground(dayNight, LocationModel.getLocalWeather(position), weatherBackground);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mLocations.size();
    }



    public void setWeatherBackground(String dayOrNight, String weather, ImageView weatherBackground){

        System.out.println("WEATHER: " + weather);

        switch (weather){

            case "Clear":
                if(dayOrNight.contentEquals("day")) {

                    weatherBackground.setBackgroundResource(R.drawable.sunny_sky);
                }else{
                    weatherBackground.setBackgroundResource(R.drawable.night_sky_wil_stewart);
                }
                break;

            case "Clouds":
                if(dayOrNight.contentEquals("day")){

                    weatherBackground.setBackgroundResource(R.drawable.cloudy);
                } else {
                    weatherBackground.setBackgroundResource(R.drawable.cloudy_night_ashwini_chaudhary);
                }
                break;

            case "Rain":
                if(dayOrNight.contentEquals("day")){

                    weatherBackground.setBackgroundResource(R.drawable.rain);
                } else {
                    weatherBackground.setBackgroundResource(R.drawable.rain_night_yang_zerony);
                }
                break;

            case "Drizzle":
                if(dayOrNight.contentEquals("day")){

                    weatherBackground.setBackgroundResource(R.drawable.drizzle);
                } else {
                    weatherBackground.setBackgroundResource(R.drawable.rain_night_yang_zerony);
                }
                break;

            case "Thunderstorm":
                if(dayOrNight.contentEquals("day")){

                    weatherBackground.setBackgroundResource(R.drawable.lightning);
                } else {
                    weatherBackground.setBackgroundResource(R.drawable.thunderstorm_night_brandon_morgan);
                }
                break;

            case "Snow":
                weatherBackground.setBackgroundResource(R.drawable.snow);
                break;

            case "Mist":
                weatherBackground.setBackgroundResource(R.drawable.mist);
                break;

            case "Smoke":
                weatherBackground.setBackgroundResource(R.drawable.smoke);
                break;

            case "Haze":
                weatherBackground.setBackgroundResource(R.drawable.haze);
                break;

            case "Dust":
                weatherBackground.setBackgroundResource(R.drawable.dust);
                break;

            case "Fog":
                if(dayOrNight.contentEquals("day")) {
                    weatherBackground.setBackgroundResource(R.drawable.fog);
                } else {
                    weatherBackground.setBackgroundResource(R.drawable.foggy_night_harald_pliessnig);
                }
                break;

            case "Sand":
                weatherBackground.setBackgroundResource(R.drawable.sand);
                break;

            case "Ash":
                weatherBackground.setBackgroundResource(R.drawable.ash);
                break;

            case "Squall":
                weatherBackground.setBackgroundResource(R.drawable.squall);
                break;

            case "Tornado":
                weatherBackground.setBackgroundResource(R.drawable.tornado);
                break;

            default:
                System.out.println("No matching weather string");

        } // END switch
    } // END setWeatherBackground

} // END LocationsAdapter

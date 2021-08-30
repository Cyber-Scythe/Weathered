package com.example.weathered.weathered;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weathered.R;

import org.jetbrains.annotations.NonNls;

import java.util.Objects;

public class MyAdapter extends FragmentStateAdapter {

    private static final int NUM_LOCATIONS = 6;

    // Required public constructor
    public MyAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position) {
            case 0:
                return CurrentLocationFragment.newInstance();

            case 1:
                return WeatherLocationFragment.newInstance(LocationModel.latsArray.get(1),
                        LocationModel.lonsArray.get(1),
                        LocationModel.cityNameArray.get(1));
            case 2:
                return WeatherLocationFragment.newInstance(LocationModel.latsArray.get(2),
                        LocationModel.lonsArray.get(2),
                        LocationModel.cityNameArray.get(2));
            case 3:
                return WeatherLocationFragment.newInstance(LocationModel.latsArray.get(3),
                        LocationModel.lonsArray.get(3),
                        LocationModel.cityNameArray.get(3));
            case 4:
                return WeatherLocationFragment.newInstance(LocationModel.latsArray.get(4),
                        LocationModel.lonsArray.get(4),
                        LocationModel.cityNameArray.get(4));
            case 5:
                return WeatherLocationFragment.newInstance(LocationModel.latsArray.get(5),
                        LocationModel.lonsArray.get(5),
                        LocationModel.cityNameArray.get(5));

            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
    }


    @Override
    public int getItemCount() {

        return LocationModel.cityNameArray.size();
    }
}
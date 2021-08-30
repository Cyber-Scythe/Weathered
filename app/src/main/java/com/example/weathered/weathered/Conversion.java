package com.example.weathered.weathered;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Conversion {

    // [ START: convertTempToF(double temp) ]
    // Method converts temperature from Kelvins
    // to Fahrenheit and returns temperature as
    // a string with the degree symbol appended
    // to the end
    public String convertTempToF(double temp) {

        // convert Kelvins to F
        double tempInF = (((temp * 9) / 5) - 459.67);

        // convert temperature to an int so that
        // only two digits are displayed
        int tempF = (int) tempInF;

        return tempF + "°";
    } // [ END: convertTempToF() ]



    // Date and Time Conversion method. Converts
    // timestamp to date and time of request

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDayOfWeek(String date){

        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/mm/dd")).
                getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String convertUtcToWeekday(long dt, String timezone) {

        return Instant.ofEpochSecond(dt).
                atZone(ZoneId.of(timezone)).
                getDayOfWeek().
                getDisplayName(TextStyle.FULL, Locale.US);
    }
    // [ END: convertUtcToWeekday() ]
}

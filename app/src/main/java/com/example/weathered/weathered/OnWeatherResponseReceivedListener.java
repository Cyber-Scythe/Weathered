package com.example.weathered.weathered;

import com.example.weathered.weathered.Response.ApiResponse;

import java.text.ParseException;

public interface OnWeatherResponseReceivedListener {

    void onSuccessfulResponse(ApiResponse response) throws ParseException;

    void onFailedResponse(Throwable t);
}

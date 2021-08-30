package com.example.weathered.weathered.Utilities;

import com.example.weathered.weathered.Response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {
    @GET("data/2.5/onecall")
    Call<ApiResponse> getWeatherForLocation(@Query("lat") double lat,
                                            @Query("lon") double lon,
                                            @Query("lang") String lang,
                                            @Query("appid") String APP_ID);
}

package com.example.weathered.weathered;

import com.example.weathered.weathered.Request.ApiRequest;
import com.example.weathered.weathered.Response.ApiResponse;
import com.example.weathered.weathered.Utilities.ApiService;
import com.example.weathered.weathered.Utilities.ApiServiceBuilder;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApiClient {


    private final String APIKEY;

    public ApiClient(String APP_ID) {
        this.APIKEY = APP_ID;
    }

    public void makeApiRequest(ApiRequest request, OnWeatherResponseReceivedListener callback) {

        ApiService apiService = ApiServiceBuilder.getApiService();

        apiService.getWeatherForLocation(request.getLatitude(), request.getLongitude(),
                request.getLanguage().name(), APIKEY).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {

                if(response.isSuccessful()){
                    try {
                        callback.onSuccessfulResponse(response.body());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                callback.onFailedResponse(new Throwable(response.message()));
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onFailedResponse(t);
            }
        });    }

}

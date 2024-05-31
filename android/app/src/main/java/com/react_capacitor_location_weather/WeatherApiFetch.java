package com.react_capacitor_location_weather;

import com.react_capacitor_location_weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiFetch {
    @GET("v1/forecast")
    Call<WeatherResponse> getWeather(@Query("latitude") double latitude,
                                     @Query("longitude") double longitude,
                                     @Query("current") String current);
}

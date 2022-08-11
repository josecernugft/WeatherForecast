package com.josecernu.weatherforecast.network

import com.josecernu.weatherforecast.model.Weather
import com.josecernu.weatherforecast.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherAPI {
    @GET(value = "data/2.5/forecast")
    suspend fun getWeather(
        @Query("lat") lat: String = "40.4165",
        @Query("lon") lon: String = "-3.70256",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = Constants.API_KEY
    ): Weather
}
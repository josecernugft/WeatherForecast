package com.josecernu.weatherforecast.repository

import android.util.Log
import com.josecernu.weatherforecast.data.DataOrException
import com.josecernu.weatherforecast.model.Weather
import com.josecernu.weatherforecast.network.WeatherAPI
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getWeather(lat: String, lon: String):DataOrException<Weather, Boolean, Exception>  {
        val response = try {
            api.getWeather(lat = lat, lon = lon)
        }catch (e: Exception) {
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $response")
        return DataOrException(data = response)
    }
}
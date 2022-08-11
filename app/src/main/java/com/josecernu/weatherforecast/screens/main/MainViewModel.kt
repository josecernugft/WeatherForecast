package com.josecernu.weatherforecast.screens.main

import androidx.lifecycle.ViewModel
import com.josecernu.weatherforecast.data.DataOrException
import com.josecernu.weatherforecast.model.Weather
import com.josecernu.weatherforecast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    suspend fun getWeatherData(lat: String, lon: String): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(lat, lon)
    }
}
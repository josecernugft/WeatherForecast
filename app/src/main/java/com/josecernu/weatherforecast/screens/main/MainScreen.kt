package com.josecernu.weatherforecast.screens.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.josecernu.weatherforecast.data.DataOrException
import com.josecernu.weatherforecast.model.Weather
import com.josecernu.weatherforecast.model.WeatherItem
import com.josecernu.weatherforecast.navigation.WeatherScreens
import com.josecernu.weatherforecast.screens.settings.SettingsViewModel
import com.josecernu.weatherforecast.utils.formatDate
import com.josecernu.weatherforecast.utils.formatDecimals
import com.josecernu.weatherforecast.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    lat: String,
    lon: String,
) {
    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    var unit by remember {
        mutableStateOf("imperial")
    }
    var isImperial by remember {
        mutableStateOf(false)
    }

    if (!unitFromDb.isNullOrEmpty()) {
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
    }
        isImperial = unit == "imperial"

        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)) {
            value = mainViewModel.getWeatherData(lat, lon, units = unit)
        }.value

        if (weatherData.loading == true) {
            CircularProgressIndicator()
        } else if (weatherData.data != null) {
            MainScaffold(weather = weatherData.data, navController, isImperial = isImperial)
        }


}

@Composable
fun MainScaffold(weather: Weather, navController: NavController, isImperial: Boolean) {
    Scaffold(topBar = {
        WeatherAppBar(title = weather.city.name + ", ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
            elevation = 5.dp,
        lat = weather.city.coord.lat.toString(),
        lon = weather.city.coord.lon.toString()) {
            Log.d("MainScreen", "MainScaffold: ButtonClicked")
        }
    }) {
        MainContent(data = weather, isImperial = isImperial)
    
    }
}

@Composable
fun MainContent(data: Weather, isImperial: Boolean) {
    val weatherItem = data.list[0]
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = formatDate(weatherItem.dt), // Wed Nov 30
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSecondary,
        fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp))
        Surface(modifier = Modifier
            .padding(4.dp)
            .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)) {

            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(text = formatDecimals(weatherItem.main.temp) + "º",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold)
                Text(text = weatherItem.weather[0].main, fontStyle = FontStyle.Italic)

            }
        }
        HumidityWindPressureRow(weather = data.list[0], isImperial = isImperial)
        Divider()
        SunsetSunRiseRow(weather = data)
        Text("Next days",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        Surface(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color(0xFFEEF1EF),
        shape = RoundedCornerShape(size = 14.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)) {
                itemsIndexed(data.list) {
                    index, item: WeatherItem ->
                    //To get only one item per day
                    if (index % 8 == 0) {
                        WeatherDetailRow(weather = item)
                    }
                }
            }
        }
    }
}


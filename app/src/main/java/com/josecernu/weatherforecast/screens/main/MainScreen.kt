package com.josecernu.weatherforecast.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.josecernu.weatherforecast.R
import com.josecernu.weatherforecast.data.DataOrException
import com.josecernu.weatherforecast.model.Weather
import com.josecernu.weatherforecast.model.WeatherItem
import com.josecernu.weatherforecast.utils.formatDate
import com.josecernu.weatherforecast.utils.formatDateTime
import com.josecernu.weatherforecast.utils.formatDecimals
import com.josecernu.weatherforecast.widgets.*

@Composable
fun MainScreen(navController: NavController,
               mainViewModel: MainViewModel = hiltViewModel()) {

    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)) {
        value = mainViewModel.getWeatherData("55.7522", "37.6156")
    }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(weather = weatherData.data, navController)
    }
}

@Composable
fun MainScaffold(weather: Weather, navController: NavController) {
    Scaffold(topBar = {
        WeatherAppBar(title = weather.city.name + ", ${weather.city.country}",
            navController = navController,
            elevation = 5.dp) {
            Log.d("MainScreen", "MainScaffold: ButtonClicked")
        }
    }) {
        MainContent(data = weather)
    
    }
}

@Composable
fun MainContent(data: Weather) {
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
        HumidityWindPressureRow(weather = data.list[0])
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
                items(items = data.list) {
                    item: WeatherItem ->
                    WeatherDetailRow(weather = item)
                }
            }

        }
    }
}


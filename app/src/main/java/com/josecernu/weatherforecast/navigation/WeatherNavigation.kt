package com.josecernu.weatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.josecernu.weatherforecast.screens.about.AboutScreen
import com.josecernu.weatherforecast.screens.favorites.FavoritesScreen
import com.josecernu.weatherforecast.screens.main.MainScreen
import com.josecernu.weatherforecast.screens.main.MainViewModel
import com.josecernu.weatherforecast.screens.search.SearchScreen
import com.josecernu.weatherforecast.screens.settings.SettingsScreen
import com.josecernu.weatherforecast.screens.splash.WeatherSplashScreen

@ExperimentalComposeUiApi
@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = WeatherScreens.SplashScreen.name ) {
        composable(WeatherScreens.SplashScreen.name){
            WeatherSplashScreen(navController = navController)
        }
        //www.google.com/cityname="Seattle"
        val route = WeatherScreens.MainScreen.name
        composable("$route/{lat}/{lon}",
        arguments = listOf(
            navArgument(name = "lat"){
                type = NavType.StringType
            },
            navArgument(name = "lon"){
                type = NavType.StringType
            })){
            navBack ->
                val lat = navBack.arguments?.getString("lat")
                val lon = navBack.arguments?.getString("lon")
                val mainViewModel = hiltViewModel<MainViewModel>()
                MainScreen(navController = navController, mainViewModel, lat = lat.toString(), lon = lon.toString())
        }
        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }

        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController = navController)
        }

        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController = navController)
        }

        composable(WeatherScreens.FavoritesScreen.name){
            FavoritesScreen(navController = navController)
        }

    }
}
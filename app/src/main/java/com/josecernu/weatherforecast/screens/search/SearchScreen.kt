package com.josecernu.weatherforecast.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.josecernu.weatherforecast.navigation.WeatherScreens
import com.josecernu.weatherforecast.widgets.WeatherAppBar

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(topBar = {
        WeatherAppBar(
            title = "Search",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            isMainScreen = false,
        ){
            navController.popBackStack()
        }
    }) {
        Surface() {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                SearchBar(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)) {
                    lat, lon -> navController.navigate(WeatherScreens.MainScreen.name + "/$lat/$lon")
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String, String) -> Unit) {
    val searchQueryLatState = rememberSaveable { mutableStateOf("") }
    val searchQueryLonState = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val validLat = remember(searchQueryLatState.value){
        searchQueryLatState.value.trim().isNotEmpty()
    }
    val validLon = remember(searchQueryLonState.value){
        searchQueryLonState.value.trim().isNotEmpty()
    }
    val focusManager = LocalFocusManager.current
    Column {
        CommonTextField(
            valueState = searchQueryLatState,
            placeholder = "Lat",
            onAction = KeyboardActions (
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ) {
                //onSearch(searchQueryState.value.trim())
                //searchQueryState.value = ""
                //keyboardController?.hide()
            })
        CommonTextField(
            valueState = searchQueryLonState,
            placeholder = "Lon",
            onAction = KeyboardActions {
                if ((!validLon) || (!validLat)) return@KeyboardActions
                onSearch(searchQueryLatState.value.trim(), searchQueryLonState.value.trim())
                searchQueryLatState.value = ""
                searchQueryLonState.value = ""
                keyboardController?.hide()
            })
    }

}

@Composable
fun CommonTextField(valueState: MutableState<String>,
                    placeholder: String,
                    keyboardType: KeyboardType = KeyboardType.Text,
                    imeAction: ImeAction = ImeAction.Next,
                    onAction: KeyboardActions = KeyboardActions.Default) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it},
        label = { Text(text = placeholder)},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Black),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp))


}

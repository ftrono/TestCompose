package com.ftrono.testCompose.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.ftrono.testCompose.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(loggedInState: Boolean) {
    val configuration = LocalConfiguration.current
    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
    if (isLandscape) {
        HomeHorizontal(loggedInState)
    } else {
        HomeVertical(loggedInState)
    }
}


@Composable
fun HomeVertical(loggedInState: Boolean) {
    //WRAPPER:
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.windowBackground)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpotifyLoginStatus(loggedInState)
        Image(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            painter = painterResource(id = R.drawable.djames),
            contentDescription = "DJames logo"
        )

    }
}


@Composable
fun SpotifyLoginStatus(loggedInState: Boolean) {
    //SPOTIFY LOGIN STATUS:
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Spotify logo:
        Image(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            painter = painterResource(id = R.drawable.logo_spotify),
            contentDescription = "Spotify logo",
            colorFilter = if (!loggedInState) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            } else {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
            }
        )
        //Logged in text:
        Text(
            text = if (loggedInState) "LOGGED IN" else "NOT LOGGED IN",
            fontSize = 12.sp,
            color = colorResource(id = R.color.light_grey),
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(start = 12.dp)
        )
    }
}


@Composable
fun HomeHorizontal(loggedIn: Boolean) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.windowBackground))) {
        androidx.compose.material3.Text(
            text = "Home Screen",
            fontSize = 24.sp,
            color = colorResource(id = R.color.light_grey),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}


//PREVIEWS:
@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun HomePreview() {
    HomeScreen(loggedInState = true)
}


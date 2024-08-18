package com.ftrono.testCompose.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ftrono.testCompose.R


@Composable
fun SettingsScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        SettingsHorizontal()
    } else {
        SettingsVertical()
    }
}


@Preview
@Composable
fun SettingsVertical() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.windowBackground))) {
        Text(
            text = "Settings Screen",
            fontSize = 24.sp,
            color = colorResource(id = R.color.light_grey),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}


@Preview(heightDp = 360, widthDp = 800)
@Composable
fun SettingsHorizontal() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.windowBackground))) {
        Text(
            text = "Settings Screen",
            fontSize = 24.sp,
            color = colorResource(id = R.color.light_grey),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}

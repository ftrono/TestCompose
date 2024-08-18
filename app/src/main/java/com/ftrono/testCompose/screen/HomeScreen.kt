package com.ftrono.testCompose.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import com.ftrono.testCompose.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
    if (isLandscape) {
        HomeHorizontal()
    } else {
        HomeVertical()
    }
}


@Preview
@Composable
fun HomeVertical() {
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


@Preview(heightDp = 360, widthDp = 800)
@Composable
fun HomeHorizontal() {
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

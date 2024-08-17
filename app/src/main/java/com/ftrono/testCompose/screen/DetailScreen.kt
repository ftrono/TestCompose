package com.ftrono.testCompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ftrono.testCompose.R

@Preview(showSystemUi = true)
@Composable
fun DetailScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.windowBackground))) {
        Text(
            text = "Detail Screen",
            fontSize = 24.sp,
            color = colorResource(id = R.color.light_grey),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}

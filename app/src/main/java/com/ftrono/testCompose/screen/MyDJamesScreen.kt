package com.ftrono.testCompose.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftrono.testCompose.R
import com.ftrono.testCompose.ui.theme.MyDJamesItem


@Composable
fun MyDJamesScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
    MyDJamesMain()
}


@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun MyDJamesMain() {

    val vocCards = listOf("artists", "playlists", "contacts", "places")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.windowBackground))
    ) {
        //SCREEN HEADER:
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(colorResource(id = R.color.windowBackground)),
            contentAlignment = Alignment.CenterStart
        ) {
            //Header:
            Text(
                text = "🎧  My DJames",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.light_grey),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = 14.dp,
                        bottom = 10.dp
                    )
                    .wrapContentWidth()
            )
        }
        //CONTENT:
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            //VOCABULARY:
            //Headers:
            Text(
                text = "Vocabulary",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.light_grey),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = 14.dp
                    )
                    .wrapContentWidth()
            )
            Text(
                text = "Help DJames understand you:",
                fontSize = 14.sp,
                color = colorResource(id = R.color.mid_grey),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        bottom = 8.dp
                    )
                    .wrapContentWidth()
            )
            //CARDS:
            DJamesCardsRow(items = listOf(
                MyDJamesItem.Artists,
                MyDJamesItem.Playlists
            ))
            DJamesCardsRow(items = listOf(
                MyDJamesItem.Contacts,
                MyDJamesItem.Places
            ))

            //FOR YOU:
            //Header:
            Text(
                text = "For you",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.light_grey),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = 30.dp,
                        bottom = 8.dp
                    )
                    .wrapContentWidth()
            )
            //Cards:
            DJamesCardsRow(items = listOf(
                MyDJamesItem.Parking,
                MyDJamesItem.News
            ))
        }
    }
}


@Composable
fun DJamesCardsRow(items: List<MyDJamesItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        for (item in items) {
            //ARTISTS:
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(12.dp)
                    .weight(0.5f)
                    .height(120.dp)
                    .shadow(
                        elevation = 2.dp,
                        spotColor = colorResource(id = R.color.colorAccentLight),
                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.dark_grey_background)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    //DESCRIPTION:
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(30.dp),
                            painter = painterResource(id = item.icon),
                            contentDescription = "",
                            tint = colorResource(id = R.color.colorAccentLight)
                        )
                        Text(
                            text = item.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.light_grey),
                            modifier = Modifier
                                .wrapContentWidth()
                        )
                        Text(
                            text = "0 items",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = colorResource(id = R.color.mid_grey),
                            modifier = Modifier
                                .wrapContentWidth()
                        )
                    }
                }
            }
        }
    }
}

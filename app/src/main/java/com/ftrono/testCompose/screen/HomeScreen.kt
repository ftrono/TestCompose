package com.ftrono.testCompose.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.ftrono.testCompose.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import com.ftrono.testCompose.application.overlayActive
import com.ftrono.testCompose.application.spotifyLoggedIn
import com.ftrono.testCompose.application.volumeUpEnabled


private var overlayActiveLiveData = MutableLiveData<Boolean>(overlayActive)
private var volumeUpEnabledLiveData = MutableLiveData<Boolean>(volumeUpEnabled)

@Composable
fun HomeScreen(loggedInState: Boolean) {
    val configuration = LocalConfiguration.current
    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }

    val overlayActiveState by overlayActiveLiveData.observeAsState()
    val volumeUpEnabledState by overlayActiveLiveData.observeAsState()

    //WRAPPER:
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.windowBackground)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpotifyLoginStatus(loggedInState)
        if (isLandscape) {
            //DISPLAY HORIZONTALLY:
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BaloonHome(true, overlayActiveState!!, volumeUpEnabledState!!)
                BaloonArrowHome(true)
                LogoHome()
            }
        } else {
            //DISPLAY VERTICALLY:
            BaloonHome(false, overlayActiveState!!, volumeUpEnabledState!!)
            BaloonArrowHome(false)
            LogoHome()
        }
        StartButton(overlayActiveState!!)
    }
}


@Composable
fun LogoHome() {
    Image(
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .zIndex(1f),
        painter = painterResource(id = R.drawable.djames),
        contentDescription = "DJames logo"
    )
}


@Composable
fun BaloonHome(isLandscape: Boolean, overlayActive: Boolean, volumeUpEnabled: Boolean) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.dark_grey))
            .zIndex(1f)
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Descr main:
            Text(
                text = if (overlayActive) {
                    "Hi, Sir!\nHow can I help you? 🚗"
                } else {
                    "Hi, Sir!\nTap on START to begin! 🚗"
                },
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp,
                fontWeight = if (overlayActive) FontWeight.Bold else FontWeight.Normal,
                color = if (overlayActive) colorResource(id = R.color.colorHeader) else colorResource(id = R.color.light_grey),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .wrapContentWidth()
                    .wrapContentHeight()
            )

            //Descr use:
            Text(
                text = if (overlayActive && volumeUpEnabled) {
                    "Use the FLOATING button, the VOLUME UP button,\nor a remote SHUTTER button to\nrecord a voice request!"
                } else if (overlayActive) {
                    "Use the FLOATING button to\nrecord a voice request!"
                } else {
                    "You can ask me many things:\ngo have a look to the Guide!"
                },
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 14.sp,
                color = if (overlayActive) colorResource(id = R.color.light_grey) else colorResource(id = R.color.mid_grey),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
    }
}


@Composable
fun BaloonArrowHome(isLandscape: Boolean) {
    Box(
        modifier = Modifier
            .width(45.dp)
            .height(45.dp)
            .zIndex(0f)
            .offset(
                y = if (isLandscape) 0.dp else -(30.dp),
                x = if (isLandscape) -(30.dp) else 0.dp
            )
            .rotate(45f)
            .background(colorResource(id = R.color.dark_grey))
    )
}


@Composable
fun StartButton(overlayActiveState: Boolean) {
    val mContext = LocalContext.current
    Button(
        modifier = Modifier
            .padding(top = 20.dp)
            .width(150.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (overlayActiveState) {
                colorResource(id = R.color.colorStop)
            } else {
                colorResource(id = R.color.colorAccent)
            } ,
            contentColor = colorResource(id = R.color.light_grey)
        ),
        content = {
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                color = colorResource(id = R.color.light_grey),
                fontWeight = FontWeight.Bold,
                text = if (overlayActiveState) "S T O P" else "S T A R T"
            )
        },
        onClick = {
            overlayActive = !overlayActive
            overlayActiveLiveData.postValue(overlayActive)
            if (overlayActive) {
                //TODO
                Toast.makeText(mContext, "Overlay active!", Toast.LENGTH_SHORT).show()
            } else {
                //TODO
                Toast.makeText(mContext, "Overlay stopped!", Toast.LENGTH_SHORT).show()
            }
        }
    )
}


@Composable
fun SpotifyLoginStatus(loggedInState: Boolean) {
    //SPOTIFY LOGIN STATUS:
    Row (
        modifier = Modifier
            .padding(bottom = 20.dp)
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
                .padding(start = 12.dp)
                .wrapContentWidth()
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


package com.ftrono.testCompose.application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ftrono.testCompose.R
import com.ftrono.testCompose.ui.theme.ClockTheme
import com.ftrono.testCompose.ui.theme.black
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FakeLockScreen: ComponentActivity() {

    private val TAG: String = FakeLockScreen::class.java.getSimpleName()

    //Parameters:
    private var now: LocalDateTime? = null
    private val dateFormat = DateTimeFormatter.ofPattern("E, dd MMM")
    private val hourFormat = DateTimeFormatter.ofPattern("HH")
    private val minsFormat = DateTimeFormatter.ofPattern("mm")
    private var clockSeparator: String = "\n"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            //For safe padding:
            statusBarStyle = SystemBarStyle.auto(black.toArgb(), black.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(black.toArgb(), black.toArgb())
        )
        setContent {
            ClockTheme {
                //Background:
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = black
                ) {
                    ClockScreen()
                }
            }
        }

        //Start personal Receiver:
        val actFilter = IntentFilter()
        actFilter.addAction(ACTION_TIME_TICK)

        //register all the broadcast dynamically in onCreate() so they get activated when app is open and remain in background:
        registerReceiver(clockActReceiver, actFilter, RECEIVER_EXPORTED)
        Log.d(TAG, "ClockActReceiver started.")

        //Start clock:
        updateDateClock()
    }


    @Preview
    @Preview(heightDp = 360, widthDp = 800)
    @Composable
    fun ClockScreen() {
        val configuration = LocalConfiguration.current
        val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
        //val separator = if (isLandscape) ":" else "\n"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .background(colorResource(id = R.color.black)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //DATE:
            Text(
                modifier = Modifier
                    .padding(bottom = if (!isLandscape) 20.dp else 10.dp)
                    .fillMaxWidth(),
                text = "Mon 1 Jan",
                color = colorResource(id = R.color.faded_grey),
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                //CLOSE BUTTON (HORIZONTAL):
                if (isLandscape) {
                    CloseButton(true)
                }
                //MAXI CLOCK:
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(0f),
                    text = if (!isLandscape) "00\n00" else "00:00",
                    color = colorResource(id = R.color.faded_grey),
                    textAlign = TextAlign.Center,
                    fontSize = if (!isLandscape) 150.sp else 140.sp,
                    lineHeight = 150.sp
                )
            }

            //PLAYER INFO:
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(
                        top = if (!isLandscape) 20.dp else 10.dp,
                        bottom = if (!isLandscape) 40.dp else 0.dp
                    )
                    .wrapContentSize(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors (
                    containerColor = colorResource(id = R.color.transparent_dark_grey)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            top=10.dp,
                            bottom=10.dp,
                            start=24.dp,
                            end=24.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //ARTWORK ICON:
                    Image(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .zIndex(1f),
                        painter = painterResource(id = R.drawable.artwork_clock),
                        contentDescription = "Album art"
                    )
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        //SONG NAME:
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .wrapContentWidth(),
                            text = "Song Name",
                            color = colorResource(id = R.color.mid_grey),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic
                        )
                        //ARTIST NAME:
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .wrapContentWidth(),
                            text = "Artist Name",
                            color = colorResource(id = R.color.mid_grey),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        //CONTEXT NAME:
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .offset(y=-(2.dp))
                                .wrapContentWidth(),
                            text = "Context Name",
                            color = colorResource(id = R.color.mid_grey),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
            //CLOSE BUTTON (VERTICAL):
            if (!isLandscape) {
                CloseButton(false)
            }
        }

    }


    @Composable
    fun CloseButton(isLandscape: Boolean) {
        OutlinedButton(
            modifier= Modifier
                .padding(start = if (isLandscape) 20.dp else 0.dp)
                .size(50.dp)
                .zIndex(1f),  //avoid the oval shape
            shape = CircleShape,
            border= BorderStroke(2.dp, colorResource(id = R.color.faded_grey)),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.faded_grey)),
            onClick = {
                //Start Main:
                val intent1 = Intent(this, MainActivity::class.java)
                startActivity(intent1)
                finish()
            }
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "content description")
        }
    }


    override fun onDestroy() {
        //unregister receivers:
        unregisterReceiver(clockActReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
        //Start Main:
        val intent1 = Intent(this, MainActivity::class.java)
        startActivity(intent1)
    }

    fun updateDateClock() {
        now = LocalDateTime.now()
//        dateView!!.text = now!!.format(dateFormat)
//        clockView!!.text = now!!.format(hourFormat) + clockSeparator + now!!.format(minsFormat)
    }


    //PERSONAL RECEIVER:
    var clockActReceiver = object: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            //Update clock (every minute):
            if (intent!!.action == ACTION_TIME_TICK) {
                updateDateClock()
            }

        }
    }

}
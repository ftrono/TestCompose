package com.ftrono.testCompose.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.health.connect.datatypes.units.Length
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.ftrono.testCompose.R
import com.ftrono.testCompose.application.ClockActivity
import com.ftrono.testCompose.application.midThreshold
import com.ftrono.testCompose.application.overlayActive
import com.ftrono.testCompose.application.playThreshold
import com.ftrono.testCompose.ui.theme.NavigationItem
import com.ftrono.testCompose.utilities.Utilities
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale


//Status:
private var historySize = MutableLiveData<Int>(0)

//TODO: need wrapper for preview (use raw JSON) vs utils for real use!

@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun HistoryScreen() {
//    val configuration = LocalConfiguration.current
//    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
    val mContext = LocalContext.current
    var history_logs by remember {
        mutableStateOf(getItems(mContext))
    }

    val historySizeState by historySize.observeAsState()
    historySize.postValue(history_logs.size)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.windowBackground))
    ) {
        //HEADER:
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(colorResource(id = R.color.windowBackground)),
            contentAlignment = Alignment.CenterEnd
        ) {
            //TEXT HEADERS:
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "🕑  History",
                    fontSize = 24.sp,
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
                    text = "$historySizeState requests (last 30 days)",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = colorResource(id = R.color.mid_grey),
                    modifier = Modifier
                        .padding(
                            start = 53.dp,
                            bottom = 20.dp
                        )
                        .wrapContentWidth()
                )
            }
            //OPTIONS BUTTONS:
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                //SEND BUTTON:
                IconButton(
                    onClick = {
                        history_logs = getItems(mContext)
                        Toast.makeText(mContext, "History updated!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = colorResource(id = R.color.colorAccentLight)
                    )
                }
                //DELETE BUTTON:
                IconButton(
                    modifier = Modifier
                        .padding(end=12.dp),
                    onClick = {
                    /*TODO*/
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete All",
                        tint = colorResource(id = R.color.colorAccentLight)
                    )
                }
            }
        }
        //HISTORY LIST:
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(
                history_logs
            ) { index, item ->
                //HISTORY CARD:
                HistoryCard(item = item.asJsonObject)
            }
        }

    }

}

@Composable
fun HistoryCard(item: JsonObject) {
    //INFO:
    val mContext = LocalContext.current
    val itemInfo = getItemInfo(item, mContext)
    val filename = itemInfo.get("filename").asString
    val textIntro = itemInfo.get("textIntro").asString
    val textMain = itemInfo.get("textMain").asString
    val textExtra = itemInfo.get("textExtra").asString

    //CARD:
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 2.dp,
                spotColor = colorResource(id = R.color.black),
                shape = RoundedCornerShape(10.dp)
            ),
        //border = BorderStroke(1.dp, colorResource(id = R.color.dark_grey)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors (
            containerColor = colorResource(id = R.color.windowBackground)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ){
            //INTRO & DATETIME:
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = colorResource(id = R.color.mid_grey),
                fontSize = 12.sp,
                text = textIntro
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                //SEND BUTTON:
                IconButton(
                    modifier = Modifier
                        .padding(end=6.dp)
                        .size(30.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share log",
                        tint = colorResource(id = R.color.mid_grey)
                    )
                }
                //DELETE BUTTON:
                IconButton(
                    modifier = Modifier
                        .padding(end=12.dp)
                        .size(30.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete log",
                        tint = colorResource(id = R.color.mid_grey)
                    )
                }
            }
        }
        //REQUEST TEXT:
        Text(
            modifier = Modifier
                .padding(start = 12.dp, bottom = 8.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            color = colorResource(id = R.color.light_grey),
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            text = textMain
        )
        //EXTRA INFO:
        Text(
            modifier = Modifier
                .padding(start = 12.dp, bottom = 8.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            color = colorResource(id = R.color.colorAccentLight),
            fontSize = 12.sp,
            lineHeight = 14.sp,
            text = textExtra
        )
    }



}


//GET ITEM INFO:
fun getItemInfo(item: JsonObject, context: Context): JsonObject {
    var itemInfo = JsonObject()
    val trimLength = 40

    //Datetime:
    val datetime = item.get("datetime").asString

    //Intent name:
    val intentName = if (item.has("intent_name")) {
        item.get("intent_name").asString
    } else {
        "Unknown"
    }

    //Intent icon:
    val intentIcon = if (intentName == "CallRequest") {
        "📞"
    } else if (intentName == "MessageRequest") {
        "💬"
    } else if (intentName.contains("Play")) {
        "🎧"
    } else {
        "❔"
    }

    //Album type:
    var albumType = try {
        item.get("spotify_play").asJsonObject.get("album_type").asString.replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        "Album"
    }

    //Context error:
    val context_error = try {
        item.get("context_error").asBoolean
    } catch (e: Exception) {
        false
    }

    //Request scoring:
    var itemScore = ""
    try {
        itemScore = if (item.has("voc_score")) {
            if (item.get("voc_score").asInt > midThreshold) {
                    "🟢"
                } else {
                    "🟡"
                }
            } else {
                if (!context_error && item.get("best_score").asInt >= playThreshold) {
                    "🟢"
                } else {
                    "🟡"
                }
        }
    } catch (e: Exception) {
        Log.d("HistoryScreen", "No score info in log item: $datetime")
    }

    //Build info:
    val queryText = item.get("nlp").asJsonObject.get("query_text").asString
    val textIntro = "$intentIcon  $itemScore  $datetime  •  $intentName"
    val textMain = if (intentName.contains("Play") && !queryText.contains("play ")) {
        "play: $queryText"
    } else {
        queryText
    }

    //Extra text:
    var textExtra = ""

    if (intentName.contains("Call") || intentName.contains("Message")) {
        //Calls & Messages:
        var contacted = item.get("contact_extractor").asJsonObject.get("contact_confirmed").asString.replaceFirstChar { it.uppercase() }
        textExtra = "Contact:  $contacted"

    } else if (intentName.contains("Play")) {
        //Play requests:
        var playType = try {
                item.get("spotify_play").asJsonObject.get("play_type").asString
            } catch (e: Exception) {
                ""
            }

        var utils = Utilities(context)
        if (playType == "playlist") {
            //Playlist / artist / collection:
            var matchName = item.get("spotify_play").asJsonObject.get("context_name").asString.split(" ").map { it.lowercase().capitalize(
                Locale.getDefault()) }.joinToString(" ")
            textExtra = "Playlist:  $matchName"


        } else if (playType == "album") {
            //Album:
            var matchName = utils.trimString(item.get("spotify_play").asJsonObject.get("match_name").asString, trimLength)
            var artistName = utils.trimString(item.get("spotify_play").asJsonObject.get("artist_name").asString, trimLength)
            if (albumType != "Album") {
                textExtra = "Album:  $matchName  ($albumType)\nArtist:  $artistName"
            } else {
                textExtra = "Album:  $matchName\nArtist:  $artistName"
            }

        } else {
            //Track:
            var matchName = utils.trimString(item.get("spotify_play").asJsonObject.get("match_name").asString, trimLength)
            var artistName = utils.trimString(item.get("spotify_play").asJsonObject.get("artist_name").asString, trimLength)

            //Context:
            var play_externally = try {
                    item.get("play_externally").asBoolean
                } catch (e: Exception) {
                    false
                }
            var contextType = item.get("spotify_play").asJsonObject.get("context_type").asString.replaceFirstChar { it.uppercase() }
            var contextName = ""
            if (contextType == "Playlist" && !context_error && !play_externally) {
                //Use Playlist:
                contextName = item.get("spotify_play").asJsonObject.get("context_name").asString
            } else {
                //Default to Album type:
                contextType = albumType
                contextName = item.get("spotify_play").asJsonObject.get("album_name").asString
            }
            contextName = utils.trimString(contextName.split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) })
            var contextFull = "$contextName  ($contextType)"
            if (play_externally) {
                contextFull = "$contextFull [EXT]"
            }
            textExtra = "Track:  $matchName\nArtist:  $artistName\nContext:  $contextFull"
        }

    } else {
        textExtra = "(No info)"
    }


    //Store to JSON:
    itemInfo.addProperty("filename", "$datetime.json")
    itemInfo.addProperty("textIntro", textIntro)
    itemInfo.addProperty("textMain", textMain)
    itemInfo.addProperty("textExtra", textExtra)

    return itemInfo
}


//TODO: TEMP:
fun getItems(context: Context): List<JsonElement> {
    val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.history_logs)))
    val logItems = JsonParser.parseReader(reader).asJsonArray.toList()
    return logItems
}

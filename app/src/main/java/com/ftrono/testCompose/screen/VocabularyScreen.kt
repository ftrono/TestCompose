package com.ftrono.testCompose.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ftrono.testCompose.R
import com.ftrono.testCompose.application.midThreshold
import com.ftrono.testCompose.application.playThreshold
import com.ftrono.testCompose.ui.navigateTo
import com.ftrono.testCompose.ui.theme.MyDJamesItem
import com.ftrono.testCompose.ui.theme.NavigationItem
import com.ftrono.testCompose.utilities.Utilities
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale


//Status:
private var vocabulary = MutableLiveData<JsonObject>(JsonObject())

//TODO: need wrapper for preview (use raw JSON) vs utils for real use!


@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun VocabularyScreenPreview() {
    val navController = rememberNavController()
    VocabularyScreen(navController, "artists", MyDJamesItem.Artists)
}

@Composable
fun VocabularyScreen(navController: NavController, filter: String, myDJamesItem: MyDJamesItem) {
    val mContext = LocalContext.current
    val vocabularyState by vocabulary.observeAsState()
    vocabulary.postValue(getVocItems(mContext, filter))

    val deleteAllOn = remember { mutableStateOf(false) }
    if (deleteAllOn.value) {
        DialogDeleteVocabulary(mContext, deleteAllOn, filter)
    }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //BACK:
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .offset(x = (6.dp))
                            .size(32.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(id = R.color.colorAccentLight)
                    )
                }
                //TEXT HEADERS:
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (filter == "artists") {
                            "Your hard-to-spell artists"
                        } else if (filter == "playlists") {
                            "Your favourite playlists"
                        } else if (filter == "contacts") {
                            "Your favourite contacts"
                        } else {
                            ""
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.light_grey),
                        modifier = Modifier
                            .padding(
                                start = 6.dp,
                                top = 14.dp
                            )
                            .wrapContentWidth()
                    )
                    Text(
                        text = "${vocabularyState!!.size()} items",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = colorResource(id = R.color.colorAccentLight),
                        modifier = Modifier
                            .padding(
                                start = 6.dp,
                                bottom = 20.dp
                            )
                            .wrapContentWidth()
                    )
                }
            }
            //OPTIONS BUTTONS:
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                //REFRESH BUTTON:
                IconButton(
                    onClick = {
                        vocabulary.postValue(getVocItems(mContext, filter))
                        Toast.makeText(mContext, "Vocabulary updated!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .offset(x = (6.dp))
                            .size(32.dp),
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
                        deleteAllOn.value = true
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
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
                vocabularyState!!.keySet().toList()
            ) { index, item ->
                //HISTORY CARD:
                VocabularyCard(key = item, filter = filter, myDJamesItem = myDJamesItem)
            }
        }

    }

}

@Composable
fun VocabularyCard(key: String, filter: String, myDJamesItem: MyDJamesItem) {
    //INFO:
    val mContext = LocalContext.current

    val editVocOn = remember { mutableStateOf(false) }
    if (editVocOn.value) {
        DialogEditVocabulary(mContext, editVocOn, filter, key)
    }

    val deleteVocOn = remember { mutableStateOf(false) }
    if (deleteVocOn.value) {
        DialogDeleteVocabulary(mContext, deleteVocOn, filter, key)
    }

    //CARD:
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        //border = BorderStroke(1.dp, colorResource(id = R.color.dark_grey)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors (
            containerColor = colorResource(id = R.color.windowBackground)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ){
            //VOC TEXT:
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .size(35.dp),
                    painter = painterResource(id = myDJamesItem.listIcon),
                    contentDescription = "Vocabulary"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 8.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    color = colorResource(id = R.color.light_grey),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    text = key
                )
                }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                //EDIT BUTTON:
                IconButton(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(30.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Share log",
                        tint = colorResource(id = R.color.mid_grey)
                    )
                }
                //DELETE BUTTON:
                IconButton(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(30.dp),
                    onClick = {
                        deleteVocOn.value = true
                    }) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete log",
                        tint = colorResource(id = R.color.mid_grey)
                    )
                }
            }
        }
    }
}


@Composable
fun DialogDeleteVocabulary(mContext: Context, dialogOnState: MutableState<Boolean>, filter: String, key: String = "") {
    //DELETE DIALOG:
    if (dialogOnState.value) {
        AlertDialog(
            onDismissRequest = {
                //cancelable -> true:
                dialogOnState.value = false
            },
            containerColor = colorResource(id = R.color.dark_grey),
            title = {
                Text(
                    text = if (key != "") "Delete item" else "Delete $filter vocabulary",
                    color = colorResource(id = R.color.light_grey)
                ) },
            text = {
                Text(
                    text = if (key != "") {
                        "Do you want to delete this ${filter.slice(0..<(filter.length-1))}?\n\n$key"
                    } else {
                        "Do you want to delete all $filter in vocabulary?"
                    },
                    color = colorResource(id = R.color.mid_grey)
                ) },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable { dialogOnState.value = false },
                    text = "No",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.colorAccentLight)
                )
            },
            confirmButton = {
                Text(
                    modifier = Modifier
                        .clickable {
                            dialogOnState.value = false
                            var toastText = ""
                            if (key != "") {
                                /*TODO*/
                                toastText = "Item deleted!"
                            } else {
                                /*TODO*/
                                toastText = "${filter.replaceFirstChar { it.uppercase() }} vocabulary deleted!"
                            }
                            getVocItems(mContext, filter) //Refresh list
                            Toast.makeText(mContext, toastText, Toast.LENGTH_LONG).show()
                        },
                    text = "Yes",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.colorAccentLight)
                )
            }
        )
    }
}


@Composable
fun DialogEditVocabulary(mContext: Context, dialogOnState: MutableState<Boolean>, filter: String, key: String = "") {
    val item = vocabulary.value!!.get(key).asJsonObject
    //EDIT DIALOG:
}


//GET ITEM INFO:
fun getVocItemInfo(item: JsonObject, context: Context): JsonObject {
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
        Log.d("VocabularyScreen", "No score info in log item: $datetime")
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
fun getVocItems(context: Context, filter: String): JsonObject {
    val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.vocabulary_sample)))
    val vocItems = JsonParser.parseReader(reader).asJsonObject.get(filter).asJsonObject
    return vocItems
}

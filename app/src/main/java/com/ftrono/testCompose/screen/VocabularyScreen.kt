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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ftrono.testCompose.R
import com.ftrono.testCompose.ui.theme.MyDJamesItem
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader


//Status:
private var vocabulary = MutableLiveData<JsonObject>(JsonObject())

//TODO: need wrapper for preview (use raw JSON) vs utils for real use!


@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun VocabularyScreenPreview() {
    val navController = rememberNavController()
    VocabularyScreen(navController, "contacts", MyDJamesItem.Playlists, editPreview = false)
}

@Composable
fun VocabularyScreen(navController: NavController, filter: String, myDJamesItem: MyDJamesItem, editPreview: Boolean = false) {
    val mContext = LocalContext.current
    val vocabularyState by vocabulary.observeAsState()
    vocabulary.postValue(getVocItems(mContext, filter))

    //ALL:
    val deleteAllOn = rememberSaveable { mutableStateOf(false) }
    if (deleteAllOn.value) {
        DialogDeleteVocabulary(mContext, deleteAllOn, filter)
    }

    val editVocOn = rememberSaveable { mutableStateOf(editPreview) }
    if (editVocOn.value || editPreview) {
        DialogEditVocabulary(mContext, editVocOn, filter, key="")
    }

    //List states:
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = colorResource(id = R.color.colorAccent),
                icon = {
                    Icon(
                        Icons.Default.Add,
                        "Add vocabulary item",
                        tint = colorResource(id = R.color.light_grey)
                    ) },
                text = {
                    Text(
                        text = "Add",
                        color = colorResource(id = R.color.light_grey),
                        fontSize = 16.sp
                    ) },
                expanded = listState.isScrollingUp(),
                onClick = {
                    editVocOn.value = true
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.windowBackground))
            ) {
                //HEADER:
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(colorResource(id = R.color.windowBackground)),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    //BG_IMAGE:
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0f),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(myDJamesItem.background),
                        contentDescription = "DJames logo"
                    )
                    //HEADER CONTENT:
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                //GRADIENT:
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to colorResource(id = R.color.transparent_full),
                                        0.3f to colorResource(id = R.color.transparent_full),
                                        1f to colorResource(id = R.color.windowBackground)
                                    )
                                )
                            ),
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
                    ) {
                        //REFRESH BUTTON:
                        IconButton(
                            onClick = {
                                vocabulary.postValue(getVocItems(mContext, filter))
                                Toast.makeText(mContext, "Vocabulary updated!", Toast.LENGTH_SHORT)
                                    .show()
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
                                .padding(end = 12.dp),
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    var vocItems = vocabularyState!!.keySet().toList()
                    itemsIndexed(
                        vocItems
                    ) { index, item ->
                        //HISTORY CARD:
                        VocabularyCard(item, filter, myDJamesItem)
                        if (index == vocItems.lastIndex) Spacer(modifier = Modifier.padding(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VocabularyCard(key: String, filter: String, myDJamesItem: MyDJamesItem) {
    val mContext = LocalContext.current

    val editVocOn = rememberSaveable { mutableStateOf(false) }
    if (editVocOn.value) {
        DialogEditVocabulary(mContext, editVocOn, filter, key)
    }

    val deleteVocOn = rememberSaveable { mutableStateOf(false) }
    if (deleteVocOn.value) {
        DialogDeleteVocabulary(mContext, deleteVocOn, filter, key)
    }
    //CARD:
    Card(
        onClick = {
            editVocOn.value = true
        },
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
                    onClick = {
                        editVocOn.value = true
                    }) {
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
                //cancelable -> true
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
                        .clickable {
                            dialogOnState.value = false
                        },
                    text = "No",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.colorAccentLight)
                )
            },
            confirmButton = {
                Text(
                    modifier = Modifier
                        .clickable {
                            var toastText = ""
                            if (key != "") {
                                /*TODO*/
                                toastText = "Item deleted!"
                            } else {
                                /*TODO*/
                                toastText = "${filter.replaceFirstChar { it.uppercase() }} vocabulary deleted!"
                            }
                            dialogOnState.value = false
                            Toast.makeText(mContext, toastText, Toast.LENGTH_LONG).show()
                            getVocItems(mContext, filter) //Refresh list
                        },
                    text = "Yes",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.colorAccentLight)
                )
            }
        )
    }
}


// Returns whether the lazy list is currently scrolling up
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}



//TODO: TEMP:
fun getVocItems(context: Context, filter: String): JsonObject {
    val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.vocabulary_sample)))
    val vocItems = JsonParser.parseReader(reader).asJsonObject.get(filter).asJsonObject
    return vocItems
}

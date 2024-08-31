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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    VocabularyScreen(navController, "contacts", MyDJamesItem.Playlists)
}

@Composable
fun VocabularyScreen(navController: NavController, filter: String, myDJamesItem: MyDJamesItem) {
    val mContext = LocalContext.current
    val vocabularyState by vocabulary.observeAsState()
    vocabulary.postValue(getVocItems(mContext, filter))

    //ALL:
    val deleteAllOn = rememberSaveable { mutableStateOf(false) }
    if (deleteAllOn.value) {
        DialogDeleteVocabulary(mContext, deleteAllOn, filter)
    }

    //TODO: FOR PREVIEW ONLY!
    val editVocOn = rememberSaveable { mutableStateOf(true) }
    if (editVocOn.value) {
        DialogEditVocabulary(mContext, editVocOn, filter, key="")
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
                VocabularyCard(item, filter, myDJamesItem)
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


@Composable
fun DialogEditVocabulary(mContext: Context, dialogOnState: MutableState<Boolean>, filter: String, key: String) {
    //TODO:
    //val item = vocabulary.value!!.get(key).asJsonObject
    var textName by rememberSaveable { mutableStateOf(key) }
    var textPlayURL by rememberSaveable { mutableStateOf("") }
    var textPrefix by rememberSaveable { mutableStateOf("+39") }
    var textPhone by rememberSaveable { mutableStateOf("") }

    //TextField colors:
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colorResource(id = R.color.colorAccentLight),
        unfocusedBorderColor = colorResource(id = R.color.mid_grey),
        focusedTextColor = colorResource(id = R.color.light_grey),
        unfocusedTextColor = colorResource(id = R.color.light_grey),
        focusedPlaceholderColor = colorResource(id = R.color.mid_grey),
        unfocusedPlaceholderColor = colorResource(id = R.color.mid_grey),
        cursorColor = colorResource(id = R.color.colorAccentLight),
        selectionColors = TextSelectionColors(
            handleColor = colorResource(id = R.color.colorAccent),
            backgroundColor = colorResource(id = R.color.transparent_green)
        )
    )

    //EDIT DIALOG:
    Dialog(
        onDismissRequest = {
            //cancelable -> false
        }
    ) {
        //CONTAINER:
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors (
                containerColor = colorResource(id = R.color.dark_grey)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                //TITLE:
                Text(
                    text = "✏️  ${filter.slice(0..<(filter.length-1)).replaceFirstChar { it.uppercase() }}",
                    modifier = Modifier.padding(8.dp),
                    color = colorResource(id = R.color.light_grey),
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp
                )

                //COMMON: TEXT FIELD 1:
                Text(
                    text = "Name",
                    modifier = Modifier.padding(top=12.dp),
                    color = colorResource(id = R.color.colorAccentLight),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 20.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    colors = textFieldColors,
                    value = textName,
                    onValueChange = { newText ->
                        textName = newText.trimStart { it == '0' }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Unspecified,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = "Write name here...",
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic
                        )
                    },
                )

                if (filter == "playlists") {
                    //PLAYLIST: TEXT FIELD 2:
                    Text(
                        text = "Playlist URL",
                        color = colorResource(id = R.color.colorAccentLight),
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = textFieldColors,
                        value = textPlayURL,
                        onValueChange = { newText ->
                            textPlayURL = newText.trimStart { it == '0' }
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Next
                        ),
                        placeholder = {
                            Text(
                                text = "Paste here the Spotify link...",
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            )
                        },
                    )

                } else if (filter == "contacts") {
                    //CONTACTS: TEXT FIELD 2:
                    Text(
                        text = "Preferred messaging language",
                        color = colorResource(id = R.color.colorAccentLight),
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    VocLanguagesSpinner()

                    //CONTACTS: TEXT FIELD 3:
                    Text(
                        text = "Main phone",
                        color = colorResource(id = R.color.colorAccentLight),
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //PREFIX:
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 20.dp)
                                .width(60.dp)
                                .wrapContentHeight(),
                            colors = textFieldColors,
                            value = textPrefix,
                            onValueChange = { newText ->
                                textPrefix = newText.trimStart { it == '0' }
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            placeholder = {
                                Text(
                                    text = "+39",
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            },
                        )
                        //SUFFIX:
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 20.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            colors = textFieldColors,
                            value = textPhone,
                            onValueChange = { newText ->
                                textPhone = newText.trimStart { it == '0' }
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            placeholder = {
                                Text(
                                    text = "Phone number...",
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            },
                        )
                    }
                }
                //BUTTONS ROW:
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    //CANCEL:
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 20.dp)
                            .clickable {
                                dialogOnState.value = false
                            },
                        color = colorResource(id = R.color.colorAccentLight),
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    //SAVE:
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .clickable {
                                /* TODO */
                                dialogOnState.value = false
                            },
                        color = colorResource(id = R.color.colorAccentLight),
                        text = "Save",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocLanguagesSpinner() {
    //TODO: Spinner components:
    val parentOptions = listOf("English", "Italian", "French", "German", "Spanish")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(parentOptions[0]) }

    //Full spinner:
    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 20.dp)
            .fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
        }
    ) {
        //Visualized textbox:
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = colorResource(id = R.color.dark_grey_background),
                unfocusedContainerColor = colorResource(id = R.color.transparent_full),
                focusedTextColor = colorResource(id = R.color.light_grey),
                unfocusedTextColor = colorResource(id = R.color.light_grey),
                focusedIndicatorColor = colorResource(id = R.color.colorAccentLight),
                unfocusedIndicatorColor = colorResource(id = R.color.mid_grey),
            )
        )
        //Dropdown:
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            scrollState = rememberScrollState(),
            containerColor = colorResource(id = R.color.dark_grey_background)
        ) {
            parentOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        isExpanded = false
                    },
                    text = {
                        Text(
                            text = selectionOption,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.light_grey)
                        )
                    }
                )
            }
        }
    }
}


//TODO: TEMP:
fun getVocItems(context: Context, filter: String): JsonObject {
    val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.vocabulary_sample)))
    val vocItems = JsonParser.parseReader(reader).asJsonObject.get(filter).asJsonObject
    return vocItems
}

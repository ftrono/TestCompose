package com.ftrono.testCompose.screen

import android.content.Context
import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
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
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import com.ftrono.testCompose.R
import com.ftrono.testCompose.application.messLanguages
import com.ftrono.testCompose.ui.theme.MyDJamesItem


@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DialogEditPreview() {
    val navController = rememberNavController()
    VocabularyScreen(navController, "contacts", MyDJamesItem.Playlists, editPreview = true)
}


@Composable
fun DialogEditVocabulary(mContext: Context, dialogOnState: MutableState<Boolean>, filter: String, key: String) {
    //TODO:
    //val item = vocabulary.value!!.get(key).asJsonObject
    var textName by rememberSaveable { mutableStateOf(key) }
    var textPlayURL by rememberSaveable { mutableStateOf("") }
    var textPrefix by rememberSaveable { mutableStateOf("+39") }
    var textPhone by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

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
            //cancelable -> true
            dialogOnState.value = false
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        //CONTAINER:
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    focusManager.clearFocus()
                },
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
                        .wrapContentHeight()
                        .focusRequester(focusRequester),
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
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
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
                            .wrapContentHeight()
                            .focusRequester(focusRequester),
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
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
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
                    VocLanguagesSpinner(messLanguages)

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
                                .wrapContentHeight()
                                .focusRequester(focusRequester),
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
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
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
fun VocLanguagesSpinner(parentOptions: List<String>) {
    //TODO: Spinner components:
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
                        //TODO
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

package com.ftrono.testCompose.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftrono.testCompose.R

@Preview
@Preview(heightDp = 360, widthDp = 800)
@Composable
fun SettingsScreen() {
//    val configuration = LocalConfiguration.current
//    val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.windowBackground))) {
        //TODO:
        //val item = vocabulary.value!!.get(key).asJsonObject
        var textName by rememberSaveable { mutableStateOf("") }
        var textPlayURL by rememberSaveable { mutableStateOf("") }
        var textPrefix by rememberSaveable { mutableStateOf("+39") }
        var textPhone by rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

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

        Column(
            modifier = Modifier
                .padding(20.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            //TITLE:
            Text(
                text = "Settings",
                modifier = Modifier.padding(8.dp),
                color = colorResource(id = R.color.light_grey),
                textAlign = TextAlign.Start,
                fontSize = 22.sp
            )

            //COMMON: TEXT FIELD 1:
            Text(
                text = "Name",
                modifier = Modifier.padding(top = 12.dp),
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
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    onGo = {
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
        }
    }
}



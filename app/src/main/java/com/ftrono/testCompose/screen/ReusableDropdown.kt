package com.ftrono.testCompose.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftrono.testCompose.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSpinner(parentOptions: List<String>, use: String, width: Int = 0) {
    //TODO: PUT "USE" ARG TO SELECT PREF TO OVERWRITE!!!
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(parentOptions[0]) }

    //Full spinner:
    ExposedDropdownMenuBox(
        modifier = if (width > 0) {
            Modifier
                .padding(top = 8.dp, bottom = 20.dp, start=36.dp)
                .width(width.dp)
        } else {
            Modifier
                .padding(top = 8.dp, bottom = 20.dp, start=36.dp)
                .fillMaxWidth()
        },
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

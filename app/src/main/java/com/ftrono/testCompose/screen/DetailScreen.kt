package com.ftrono.testCompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.colorResource
import com.ftrono.testCompose.R


@Composable
fun DetailScreen(onItemClick: () -> Unit) {
    DetailItemList(onItemClick = onItemClick)
}

@Composable
fun DetailItemList(onItemClick: () -> Unit) {
    val list = mutableListOf<CardItem>()

    for (i in 0..10) {
        list.add(CardItem(id = i))
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        items(list) { item ->
            DetailItem(item = item, onItemClick = onItemClick)
        }
    }
}

@Composable
fun DetailItem(item: CardItem, onItemClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.colorAccent))
            .clickable { onItemClick() }
    ) {
        Text(
            text = "Item ${item.id}",
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(16.dp)
        )
    }
}

data class CardItem(val id: Int)


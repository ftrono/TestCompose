package com.ftrono.testCompose.ui.theme

import com.ftrono.testCompose.R

sealed class MyDJamesItem(
    var route: String,
    var icon: Int,
    var title: String) {

    //MY DJAMES:
    object Artists : MyDJamesItem("artists", R.drawable.png_note, "My Artists")
    object Playlists : MyDJamesItem("playlists", R.drawable.png_headphones, "My Playlists")
    object Contacts : MyDJamesItem("contacts", R.drawable.png_contact, "My Contacts")
    object Places : MyDJamesItem("places", R.drawable.png_pin, "My Places")
    object Parking : MyDJamesItem("parking", R.drawable.png_car, "Last Parking")
    object News : MyDJamesItem("news", R.drawable.png_news, "My News")
}

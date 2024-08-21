package com.ftrono.testCompose.ui.theme

import com.ftrono.testCompose.R

sealed class MyDJamesItem(
    var route: String,
    var icon: Int,
    var listIcon: Int,
    var title: String) {

    //MY DJAMES:
    object Artists : MyDJamesItem("artists", R.drawable.png_note, R.drawable.icon_note, "My Artists")
    object Playlists : MyDJamesItem("playlists", R.drawable.png_headphones, R.drawable.icon_headphones, "My Playlists")
    object Contacts : MyDJamesItem("contacts", R.drawable.png_contact, R.drawable.icon_contact, "My Contacts")
    object Places : MyDJamesItem("places", R.drawable.png_pin, R.drawable.icon_pin, "My Places")
    object Parking : MyDJamesItem("parking", R.drawable.png_car, R.drawable.icon_pin, "Last Parking")
    object News : MyDJamesItem("news", R.drawable.png_news, R.drawable.icon_note, "My News")
}

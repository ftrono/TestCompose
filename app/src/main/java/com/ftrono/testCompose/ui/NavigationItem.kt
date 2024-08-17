package com.ftrono.testCompose.ui.theme

import com.ftrono.testCompose.R

sealed class NavigationItem(
    var route: String,
    var icon: Int,
    var title: String) {

    object Home : NavigationItem("home", R.drawable.nav_home, "Home")
    object Music : NavigationItem("guide", R.drawable.nav_help, "Guide")
    object Movies : NavigationItem("myDjames", R.drawable.nav_vocabulary, "My Djames")
    object Profile : NavigationItem("history", R.drawable.nav_history, "History")
}

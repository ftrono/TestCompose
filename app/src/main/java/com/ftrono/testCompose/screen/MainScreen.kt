package com.ftrono.testCompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.ftrono.testCompose.BottomNavigationItems
import com.ftrono.testCompose.SetupNavigationHost
import com.ftrono.testCompose.TopBarNavigation
import com.ftrono.testCompose.R


@Composable
fun MainScreen(
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    Scaffold(
        scaffoldState = scaffoldState,
        //TOP BAR:
        topBar = {
            TopBarNavigation(
                modifier = Modifier
                    .background(colorResource(id = R.color.colorAccent)),
                navController = navController
            ) },
        //BOTTOM BAR:
        bottomBar = {
            BottomNavigationItems(
                navController = navController
            ) }
    ) { padding ->
        //LIST BACKGROUND:
        SetupNavigationHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colorResource(id = R.color.windowBackground))
        )
    }
}

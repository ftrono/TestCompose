package com.ftrono.testCompose

import android.hardware.camera2.params.ColorSpaceTransform
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ftrono.testCompose.ui.HomeScreen
import com.ftrono.testCompose.ui.MoviesScreen
import com.ftrono.testCompose.ui.MusicScreen
import com.ftrono.testCompose.ui.ProfileScreen
import com.ftrono.testCompose.ui.theme.DJamesTheme
import com.ftrono.testCompose.ui.theme.NavigationItem
import com.ftrono.testCompose.ui.theme.black
import com.ftrono.testCompose.ui.theme.colorPrimary
import com.ftrono.testCompose.ui.theme.light_grey

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            //For safe padding:
            statusBarStyle = SystemBarStyle.auto(colorPrimary.toArgb(), colorPrimary.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(black.toArgb(), black.toArgb())
        )
        setContent {
            DJamesTheme {
                //Background:
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = black
                ) {
                    MainScreen()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        CenterAlignedTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            ),
            title = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        modifier = Modifier.offset(y = (2.dp)),
                        text = "🎧 DJames",
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.light_grey),
                        //fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.offset(y = -(2.dp)),
                        text = "for francesco_trono",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.mid_grey)
                        //fontWeight = FontWeight.Bold
                    )
                }
            },
//            navigationIcon = {
//                IconButton(onClick = { /* doSomething() */ }) {
//                    Icon(
//                        imageVector = Icons.Filled.Menu,
//                        contentDescription = "Localized description"
//                    )
//                }
//            },
            colors = TopAppBarColors(
                containerColor = colorResource(id = R.color.colorPrimary),
                scrolledContainerColor = colorResource(id = R.color.colorPrimary),
                navigationIconContentColor = colorResource(id = R.color.mid_grey),
                titleContentColor = colorResource(id = R.color.light_grey),
                actionIconContentColor = colorResource(id = R.color.mid_grey)
            ),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            //actions =
        )
    }


    @Composable
    fun BottomNavigationBar(
        items: List<NavigationItem>,
        navController: NavController
    ) {
        //NAV BAR:
        NavigationBar(
            containerColor = colorResource(id = R.color.black),
            contentColor = colorResource(id = R.color.mid_grey)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            //RECYCLER LIST:
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title
                        )
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = colorResource(id = R.color.colorAccent),
                        selectedTextColor = colorResource(id = R.color.colorAccent),
                        unselectedIconColor = colorResource(id = R.color.mid_grey),
                        unselectedTextColor = colorResource(id = R.color.mid_grey),
                        selectedIndicatorColor = colorResource(id = R.color.transparent_green),
                        disabledIconColor = colorResource(id = R.color.mid_grey),
                        disabledTextColor = colorResource(id = R.color.mid_grey)
                    ),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as users select items:
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when reselecting the same item:
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item:
                            restoreState = true
                        }
                    }
                )
            }
        }
    }


    @Preview
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Music,
            NavigationItem.Movies,
            NavigationItem.Profile
        )

        //MAIN SCREEN (SCAFFOLD):
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding(),
            topBar = { TopBar() },
            bottomBar = { BottomNavigationBar(items, navController) },
            // Set background color to avoid the white flashing when you switch between screens:
            backgroundColor = colorResource(id = R.color.windowBackground)
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                //SET CURRENT SCREEN FROM NAVIGATION HOST:
                Navigation(navController = navController)
            }
        }
    }


    //NAV HOST:
    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                HomeScreen()
            }
            composable(NavigationItem.Music.route) {
                MusicScreen()
            }
            composable(NavigationItem.Movies.route) {
                MoviesScreen()
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
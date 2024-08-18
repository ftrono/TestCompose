package com.ftrono.testCompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ftrono.testCompose.screen.GuideScreen
import com.ftrono.testCompose.screen.HistoryScreen
import com.ftrono.testCompose.screen.HomeScreen
import com.ftrono.testCompose.screen.MyDJamesScreen
import com.ftrono.testCompose.screen.SettingsScreen
import com.ftrono.testCompose.ui.theme.DJamesTheme
import com.ftrono.testCompose.ui.theme.NavigationItem
import com.ftrono.testCompose.ui.theme.black
import com.ftrono.testCompose.ui.theme.colorPrimary


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


    @Preview
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Guide,
            NavigationItem.MyDJames,
            NavigationItem.History
        )

        //MAIN SCREEN (SCAFFOLD):
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding(),
            topBar = { TopBar(navController) },
            bottomBar = { BottomNavigationBar(items, navController) },
            // Set background color to avoid the white flashing when you switch between screens:
            containerColor = colorResource(id = R.color.windowBackground)
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


    //TOP APP BAR:
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController) {
        val mContext = LocalContext.current

        // STATES:
        var mDisplayMenu by remember {
            mutableStateOf(false)
        }

        var menuLoggedIn by remember {
            mutableStateOf(false)
        }

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
            colors = TopAppBarColors(
                containerColor = colorResource(id = R.color.colorPrimary),
                scrolledContainerColor = colorResource(id = R.color.colorPrimary),
                navigationIconContentColor = colorResource(id = R.color.mid_grey),
                titleContentColor = colorResource(id = R.color.light_grey),
                actionIconContentColor = colorResource(id = R.color.mid_grey)
            ),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = {
                //SETTINGS BUTTON:
                IconButton(
                    onClick = {
                        navigateTo(navController, NavigationItem.Settings)
                    }) {
                    Icon(
                        painterResource(id = R.drawable.item_settings),
                        "",
                        tint = colorResource(id = R.color.light_grey)
                    )
                }

                //"MORE OPTIONS" BUTTON:
                IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                    Icon(
                        Icons.Default.MoreVert,
                        "",
                        tint = colorResource(id = R.color.light_grey)
                    )
                }

                //DROPDOWN MENU:
                DropdownMenu(
                    modifier = Modifier
                        .background(colorResource(id = R.color.dark_grey)),
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false }
                ) {

                    //1) Item: LOGIN/LOGOUT
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (!menuLoggedIn) "Login to Spotify" else "Logout from Spotify",
                                color = colorResource(id = R.color.light_grey),
                                fontSize = 16.sp
                            )},
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.item_user),
                                "",
                                tint = colorResource(id = R.color.mid_grey)
                            )
                        },
                        onClick = {
                            menuLoggedIn = !menuLoggedIn
                            if (menuLoggedIn) {
                                Toast.makeText(mContext, "Logged in to Spotify!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(mContext, "Logged out of Spotify", Toast.LENGTH_SHORT).show()
                            }
                            mDisplayMenu = false
                        })

                    //2) Item: VOICE SETTINGS
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Voice settings",
                                color = colorResource(id = R.color.light_grey),
                                fontSize = 16.sp
                            )},
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.speak_icon_gray),
                                "",
                                tint = colorResource(id = R.color.mid_grey)
                            )
                        },
                        onClick = {
                            Toast.makeText(mContext, "Voice settings", Toast.LENGTH_SHORT).show()
                            mDisplayMenu = false
                        })

                    //3) Item: PERMISSIONS
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Permissions",
                                color = colorResource(id = R.color.light_grey),
                                fontSize = 16.sp
                            )},
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.item_permissions),
                                "",
                                tint = colorResource(id = R.color.mid_grey)
                            )
                        },
                        onClick = {
                            Toast.makeText(mContext, "Permissions", Toast.LENGTH_SHORT).show()
                            mDisplayMenu = false
                        })

                }
            }
        )
    }


    //BOTTOM NAVBAR:
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
                        navigateTo(navController, item)
                    }
                )
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
            composable(NavigationItem.Guide.route) {
                GuideScreen()
            }
            composable(NavigationItem.MyDJames.route) {
                MyDJamesScreen()
            }
            composable(NavigationItem.History.route) {
                HistoryScreen()
            }
            composable(NavigationItem.Settings.route) {
                SettingsScreen()
            }
        }
    }


    //Helper: navigate to route:
    fun navigateTo(navController: NavController, item: NavigationItem) {
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

}
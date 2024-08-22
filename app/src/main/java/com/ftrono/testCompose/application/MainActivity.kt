package com.ftrono.testCompose.application

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
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
import com.ftrono.testCompose.R
import com.ftrono.testCompose.screen.GuideScreen
import com.ftrono.testCompose.screen.HistoryScreen
import com.ftrono.testCompose.screen.HomeScreen
import com.ftrono.testCompose.screen.MyDJamesScreen
import com.ftrono.testCompose.screen.SettingsScreen
import com.ftrono.testCompose.screen.VocabularyScreen
import com.ftrono.testCompose.ui.Navigation
import com.ftrono.testCompose.ui.navigateTo
import com.ftrono.testCompose.ui.theme.DJamesTheme
import com.ftrono.testCompose.ui.theme.MyDJamesItem
import com.ftrono.testCompose.ui.theme.NavigationItem
import com.ftrono.testCompose.ui.theme.windowBackground


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            //For safe padding:
            statusBarStyle = SystemBarStyle.auto(windowBackground.toArgb(), windowBackground.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(windowBackground.toArgb(), windowBackground.toArgb())
        )
        setContent {
            DJamesTheme {
                //Background:
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = windowBackground
                ) {
                    MainScreen()
                }
            }
        }
    }


    @Preview
    @Preview(heightDp = 360, widthDp = 800)
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val navItems = listOf(
            NavigationItem.Home,
            NavigationItem.Guide,
            NavigationItem.MyDJames,
            NavigationItem.History
        )

        val spotifyLoggedInState by spotifyLoggedIn.observeAsState()
        val settingsOpenState by settingsOpen.observeAsState()
        val innerNavOpenState by innerNavOpen.observeAsState()
        val configuration = LocalConfiguration.current
        val isLandscape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }
        val customNavSuiteType = if (isLandscape) NavigationSuiteType.NavigationRail else NavigationSuiteType.NavigationBar

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        //var selectedItem by rememberSaveable { mutableIntStateOf(0) }

        val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
            navigationBarItemColors = NavigationBarItemDefaults.colors(
                indicatorColor = colorResource(id = R.color.transparent_green),
                selectedIconColor = colorResource(id = R.color.colorAccentLight),
                selectedTextColor = colorResource(id = R.color.colorAccentLight),
                unselectedIconColor = colorResource(id = R.color.mid_grey),
                unselectedTextColor = colorResource(id = R.color.mid_grey)
            ),
            navigationRailItemColors = NavigationRailItemDefaults.colors(
                indicatorColor = colorResource(id = R.color.transparent_green),
                selectedIconColor = colorResource(id = R.color.colorAccentLight),
                selectedTextColor = colorResource(id = R.color.colorAccentLight),
                unselectedIconColor = colorResource(id = R.color.mid_grey),
                unselectedTextColor = colorResource(id = R.color.mid_grey)
            )
        )

        //MAIN SCREEN: NAVIGATION:
        NavigationSuiteScaffold(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .shadow(
                    elevation = 4.dp,
                    spotColor = colorResource(id = R.color.mid_grey)
                ),
            layoutType = customNavSuiteType,
            navigationSuiteItems = {
                navItems.forEach { navItem ->
                    item(
                        modifier = if (isLandscape) {
                            Modifier
                                .offset(y = 12.dp)
                                .padding(4.dp)
                        } else {
                            Modifier
                        },
                        icon = {
                            Icon(
                                painterResource(id = navItem.icon),
                                contentDescription = navItem.title
                            )
                        },
                        label = {
                            Text(
                                text = navItem.title
                            )
                        },
                        colors = myNavigationSuiteItemColors,
                        alwaysShowLabel = true,
                        selected = currentRoute == navItem.route,
                        onClick = {
                            //Navigate:
                            val curNavRoute = navItem.route
                            if (curNavRoute == lastNavRoute && (settingsOpenState!! || innerNavOpenState!!)) {
                                navController.popBackStack()
                            } else {
                                navigateTo(navController, curNavRoute)
                            }
                            lastNavRoute = curNavRoute
                        }
                    )
                }
            },
            containerColor = colorResource(id = R.color.black),
            contentColor = colorResource(id = R.color.mid_grey),
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = colorResource(id = R.color.windowBackground),
                navigationBarContentColor = colorResource(id = R.color.mid_grey),
                navigationRailContainerColor = colorResource(id = R.color.windowBackground),
                navigationRailContentColor = colorResource(id = R.color.mid_grey),
            )
        ) {
            //MAIN SCREEN: SCAFFOLD:
            Scaffold(
                topBar = { TopBar(navController, spotifyLoggedInState!!, settingsOpenState!!) },
                // Set background color to avoid the white flashing when you switch between screens:
                containerColor = colorResource(id = R.color.windowBackground)
            ) {
                Box(
                    modifier = Modifier
                        .padding(it)
                ) {
                    //SET CURRENT SCREEN FROM NAVIGATION HOST:
                    Navigation(navController)
                }
            }
        }
    }


    //TOP APP BAR:
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController, spotifyLoggedInState: Boolean, settingsOpenState: Boolean) {
        val mContext = LocalContext.current

        // STATES:
        var mDisplayMenu by remember {
            mutableStateOf(false)
        }

        CenterAlignedTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    spotColor = colorResource(id = R.color.mid_grey)
                ),
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
                containerColor = colorResource(id = R.color.windowBackground),
                scrolledContainerColor = colorResource(id = R.color.windowBackground),
                navigationIconContentColor = colorResource(id = R.color.mid_grey),
                titleContentColor = colorResource(id = R.color.light_grey),
                actionIconContentColor = colorResource(id = R.color.mid_grey)
            ),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = {
                //SETTINGS BUTTON:
                IconButton(
                    onClick = {
                        //Navigate:
                        val curNavRoute = NavigationItem.Settings.route
                        if (curNavRoute == lastNavRoute && (settingsOpenState)) {
                            navController.popBackStack()
                        } else {
                            navigateTo(navController, curNavRoute)
                        }
                        lastNavRoute = curNavRoute
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.item_settings),
                        contentDescription = "",
                        tint = if (settingsOpenState) {
                            colorResource(id = R.color.colorAccentLight)
                        } else {
                            colorResource(id = R.color.light_grey)
                        }
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
                                text = if (!spotifyLoggedInState) "Login to Spotify" else "Logout from Spotify",
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
                            if (spotifyLoggedIn.value == false) {
                                spotifyLoggedIn.postValue(true)
                                Toast.makeText(mContext, "Logged in to Spotify!", Toast.LENGTH_SHORT).show()
                            } else {
                                spotifyLoggedIn.postValue(false)
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

}
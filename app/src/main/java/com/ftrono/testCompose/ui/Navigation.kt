package com.ftrono.testCompose.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ftrono.testCompose.application.curNavId
import com.ftrono.testCompose.application.filter
import com.ftrono.testCompose.application.settingsOpen
import com.ftrono.testCompose.screen.GuideScreen
import com.ftrono.testCompose.screen.HistoryScreen
import com.ftrono.testCompose.screen.HomeScreen
import com.ftrono.testCompose.screen.MyDJamesScreen
import com.ftrono.testCompose.screen.SettingsScreen
import com.ftrono.testCompose.screen.VocabularyScreen
import com.ftrono.testCompose.ui.theme.MyDJamesItem
import com.ftrono.testCompose.ui.theme.NavigationItem


//NAV HOST:
@Composable
fun Navigation(navController: NavHostController) {
    val filterState by filter.observeAsState()
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        //MAIN:
        //0 -> HOME:
        composable(
            NavigationItem.Home.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = if (curNavId > 0) {
                        AnimatedContentTransitionScope.SlideDirection.End
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    }
                )
            }) {
            HomeScreen()
            curNavId = 0
            settingsOpen.postValue(false)
        }

        //1 -> GUIDE:
        composable(
            NavigationItem.Guide.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = if (curNavId > 1) {
                        AnimatedContentTransitionScope.SlideDirection.End
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    }
                )
            }) {
            GuideScreen()
            curNavId = 1
            settingsOpen.postValue(false)
        }

        //2 -> MY DJAMES:
        composable(
            NavigationItem.MyDJames.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = if (curNavId >= 2) {
                        AnimatedContentTransitionScope.SlideDirection.End
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    }
                )
            }) {
            MyDJamesScreen(navController)
            curNavId = 2
            settingsOpen.postValue(false)
        }

        //3 -> HISTORY:
        composable(
            NavigationItem.History.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = if (curNavId > 3) {
                        AnimatedContentTransitionScope.SlideDirection.End
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    }
                )
            }) {
            HistoryScreen()
            curNavId = 3
            settingsOpen.postValue(false)
        }

        //EXTRA:
        //0 -> SETTINGS:
        composable(
            NavigationItem.Settings.route,
            enterTransition = {
                scaleIn() + expandVertically(expandFrom = Alignment.Bottom)
            },
            exitTransition = {
                scaleOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
            }
        ) {
            SettingsScreen()
            curNavId = 0
            settingsOpen.postValue(true)
        }

        //MYDJAMES VOC:
        //1 -> ARTISTS:
        composable(
            MyDJamesItem.Artists.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }) {
            curNavId = 2
            filter.postValue("artists")
            VocabularyScreen(
                navController = navController,
                filter = filterState!!,
                myDJamesItem = MyDJamesItem.Artists
            )
        }

        //2 -> PLAYLISTS:
        composable(
            MyDJamesItem.Playlists.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }) {
            curNavId = 2
            filter.postValue("playlists")
            VocabularyScreen(
                navController = navController,
                filter = filterState!!,
                myDJamesItem = MyDJamesItem.Playlists
            )
        }

        //3 -> CONTACTS:
        composable(
            MyDJamesItem.Contacts.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }) {
            curNavId = 2
            filter.postValue("contacts")
            VocabularyScreen(
                navController = navController,
                filter = filterState!!,
                myDJamesItem = MyDJamesItem.Contacts
            )
        }
    }
}


//Helper: navigate to route:
fun navigateTo(navController: NavController, route: String, inner: Boolean = false) {
    navController.navigate(route) {
        // Pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as users select items:
        navController.graph.startDestinationRoute?.let { route ->
            if (inner) {
                popUpTo(navController.currentBackStackEntry!!.id) {
                    saveState = true
                }
            } else {
                popUpTo(route) {
                    saveState = true
                }
            }
        }

        // Avoid multiple copies of the same destination when reselecting the same item:
        launchSingleTop = true
        // Restore state when reselecting a previously selected item:
        restoreState = true
    }
}

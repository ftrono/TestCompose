package com.ftrono.testCompose.application

import android.app.Application
import androidx.lifecycle.MutableLiveData


//GLOBALS:
val playThreshold = 80
val midThreshold = 55

//STATUS VARS:
var spotifyLoggedIn = MutableLiveData<Boolean>(false)
var overlayActive = MutableLiveData<Boolean>(false)
var volumeUpEnabled = MutableLiveData<Boolean>(true)
var settingsOpen = MutableLiveData<Boolean>(false)
var innerNavOpen = MutableLiveData<Boolean>(false)
var filter = MutableLiveData<String>("artists")
var curNavId = 0
var lastNavRoute = "home"


//Clock Act receiver:
const val ACTION_TIME_TICK = "android.intent.action.TIME_TICK"


class App: Application()
{
    companion object {
//        var prefs: Prefs? = null
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        //prefs = Prefs(applicationContext)

    }
}
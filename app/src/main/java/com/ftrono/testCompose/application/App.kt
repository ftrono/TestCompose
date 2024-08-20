package com.ftrono.testCompose.application


import android.app.Application

//GLOBALS:

//STATUS VARS:
var spotifyLoggedIn: Boolean = false
var overlayActive: Boolean = false
var volumeUpEnabled: Boolean = true

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
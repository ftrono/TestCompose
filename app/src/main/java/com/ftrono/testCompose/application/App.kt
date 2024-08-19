package com.ftrono.testCompose.application


import android.app.Application

//GLOBALS:
var spotifyLoggedIn: Boolean = false
var overlayActive: Boolean = false
var volumeUpEnabled: Boolean = true


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
package com.ftrono.testCompose.application


import android.app.Application
import androidx.lifecycle.MutableLiveData

//GLOBALS:

//STATUS VARS:
var spotifyLoggedIn = MutableLiveData<Boolean>(false)
var overlayActive = MutableLiveData<Boolean>(false)
var volumeUpEnabled = MutableLiveData<Boolean>(true)


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
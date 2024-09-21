package com.ftrono.testCompose.utilities

import android.content.Context
import android.util.Log
import com.ftrono.testCompose.R
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader


class Utilities () {
    private val TAG = Utilities::class.java.simpleName

    //Trim strings:
    fun trimString(textOrig: String, maxLength: Int = 30): String {
        var textTrimmed = textOrig
        if (textOrig.length > maxLength) {
            textTrimmed = textOrig.slice(0..maxLength) + "..."
        }
        return textTrimmed
    }

    //GUIDE:
    //Read Guide:
    fun getGuideArray(context: Context): JsonArray {
        var guideArray = JsonArray()
        var reader: BufferedReader? = null
        try {
            //Get default query language:
//            if (prefs.queryLanguage.toInt() == 1) {
//                reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.guide_ita)))   //"ita"
//            } else {
//                reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.guide_eng)))   //"eng"
//            }
            reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.guide_eng)))   //"eng"   //TODO: TEMP!
            //Load:
            guideArray = JsonParser.parseReader(reader).asJsonArray
        } catch (e: Exception) {
            Log.w(TAG, "GUIDE PARSING ERROR: ", e)
        }
        //Log.d(TAG, guideArray.toString())
        return guideArray
    }
}
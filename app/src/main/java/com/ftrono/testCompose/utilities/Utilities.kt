package com.ftrono.testCompose.utilities

import android.content.Context


class Utilities (private val context: Context) {

    //Trim strings:
    fun trimString(textOrig: String, maxLength: Int = 30): String {
        var textTrimmed = textOrig
        if (textOrig.length > maxLength) {
            textTrimmed = textOrig.slice(0..maxLength) + "..."
        }
        return textTrimmed
    }

}
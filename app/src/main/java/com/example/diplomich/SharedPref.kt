package com.example.diplomich

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context:Context){
var mySharedPref:SharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE)

    public fun setNightModeState(state:Boolean){
        val editor:SharedPreferences.Editor =  mySharedPref.edit()
        editor.putBoolean("NightMode",state)
        editor.apply()
    }

    fun loadNightModeState(): Boolean {
        return mySharedPref.getBoolean("NightMode", false)
    }

}
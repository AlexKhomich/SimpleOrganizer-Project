package com.cdg.alex.simpleorganizer.service

import android.content.Context
import android.content.SharedPreferences
import java.util.*


abstract class JsonParser {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var alarmMap: Map<String, *>
    lateinit var list: ArrayList<String>

    fun getAllAlarms (context: Context): List<String> {
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        alarmMap = sharedPreferences.all
        list = ArrayList()
        for ((key, value) in alarmMap) {
            val alarmValue = value.toString()
            list.add(alarmValue)
        }

        return list
    }

    fun getSharedPrefs (context: Context): Map<String, *> {
      sharedPreferences= context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
       alarmMap = sharedPreferences.all
        return alarmMap
    }

    fun getNumberOfAlarms (context: Context): Int {
        val counter: Int = getAllAlarms(context).count()
        return counter
    }

    abstract fun getTime (context: Context): String?
    abstract fun getRingtone (context: Context): String?
    abstract fun getPeriod (context: Context): String?
    abstract fun getSwitchState (context: Context): Boolean
    abstract fun mondayState (context: Context): Boolean
    abstract fun tuesdayState (context: Context): Boolean
    abstract fun wednesdayState (context: Context): Boolean
    abstract fun thursdayState (context: Context): Boolean
    abstract fun fridayState (context: Context): Boolean
    abstract fun saturdayState (context: Context): Boolean
    abstract fun sundayState (context: Context): Boolean
    abstract fun getAlarmId (context: Context): String?
    abstract fun getSoundPath (context: Context): String?
    abstract fun checkPeriod (context: Context): Boolean

}
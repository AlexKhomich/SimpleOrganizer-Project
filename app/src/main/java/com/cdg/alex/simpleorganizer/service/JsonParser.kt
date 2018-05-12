package com.cdg.alex.simpleorganizer.service

import android.content.Context
import java.util.*


abstract class JsonParser {

    companion object {

        private lateinit var alarmMap: Map<String, *>

        fun getAllAlarms(context: Context): List<String> {
            val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            alarmMap = sharedPreferences.all
            val list: ArrayList<String> = ArrayList()
            for ((key, value) in alarmMap) {
                val alarmValue = value.toString()
                list.add(alarmValue)
            }
            return list
        }

        fun getSharedPrefs(context: Context): Map<String, *> {
           val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            alarmMap = sharedPreferences.all
            return alarmMap
        }

        fun getNumberOfAlarms(context: Context): Int  = getAllAlarms(context).size
    }


    abstract fun getTime(context: Context): String?
    abstract fun getRingtone(context: Context): String?
    abstract fun getPeriod(context: Context): String?
    abstract fun getSwitchState(context: Context): Boolean
    abstract fun mondayState(context: Context): Boolean
    abstract fun tuesdayState(context: Context): Boolean
    abstract fun wednesdayState(context: Context): Boolean
    abstract fun thursdayState(context: Context): Boolean
    abstract fun fridayState(context: Context): Boolean
    abstract fun saturdayState(context: Context): Boolean
    abstract fun sundayState(context: Context): Boolean
    abstract fun getAlarmId(context: Context): String?
    abstract fun getSoundPath(context: Context): String?
    abstract fun checkPeriod(context: Context): Boolean

}
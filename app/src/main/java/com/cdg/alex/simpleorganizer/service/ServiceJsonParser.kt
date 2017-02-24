package com.cdg.alex.simpleorganizer.service

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class ServiceJsonParser: JsonParser() {
    //    здесь будут парситься строки, полученные из shared prefs и записываться в ServiceSettingsHolder
    private lateinit var result: String
    private var indexOfAlarm: Int = 0

    fun setIndexOfAlarm(index: Int) {
        indexOfAlarm = index
    }

    override fun getTime(context: Context): String? {
        result = getAllAlarms(context)[indexOfAlarm]
        var time: String = ""
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { time = it.getString("timeTextView") }
        return time
    }

    override fun getRingtone(context: Context): String? {
        result = getAllAlarms(context)[indexOfAlarm]
        var ringtone: String = ""
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { ringtone = it.getString("setRingtoneView") }
        return ringtone
    }

    override fun getPeriod(context: Context): String? {
        result = getAllAlarms(context)[indexOfAlarm]
        var period: String = ""
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { period = it.getString("setPeriodView") }
        return period
    }

    override fun getSwitchState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var switchState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { switchState = it.getBoolean("onOfSwitch") }
        return switchState
    }

    override fun mondayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var mondayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { mondayState = it.getBoolean("monday") }
        return mondayState
    }

    override fun tuesdayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var tuesdayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { tuesdayState = it.getBoolean("tuesday") }
        return tuesdayState
    }

    override fun wednesdayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var wednesdayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { wednesdayState = it.getBoolean("wednesday") }
        return wednesdayState
    }

    override fun thursdayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var thursdayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { thursdayState = it.getBoolean("thursday") }
        return thursdayState
    }

    override fun fridayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var fridayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { fridayState = it.getBoolean("friday") }
        return fridayState
    }

    override fun saturdayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var saturdayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { saturdayState = it.getBoolean("saturday") }
        return saturdayState
    }

    override fun sundayState(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var sundayState: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { sundayState = it.getBoolean("sunday") }
        return sundayState
    }

    override fun getAlarmId(context: Context): String? {
        result = getAllAlarms(context)[indexOfAlarm]
        var id: String = ""
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { id = it.getString("id") }
        return id
    }

    override fun getSoundPath(context: Context): String? {
        result = getAllAlarms(context)[indexOfAlarm]
        var soundPath: String = ""
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { soundPath = it.getString("soundPath") }
        return soundPath
    }

    override fun checkPeriod(context: Context): Boolean {
        result = getAllAlarms(context)[indexOfAlarm]
        var checkPeriod: Boolean = false
        val jsonObject: JSONObject = JSONObject(result)
        val jsonArray: JSONArray = jsonObject.getJSONArray("settings")
        (0..jsonArray.length() - 1)
                .map { jsonArray.getJSONObject(it) }
                .forEach { checkPeriod = it.getBoolean("checkPeriod") }
        return checkPeriod
    }

}
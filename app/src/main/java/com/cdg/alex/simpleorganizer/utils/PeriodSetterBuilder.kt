package com.cdg.alex.simpleorganizer.utils

import android.content.Context
import android.content.SharedPreferences
import com.cdg.alex.simpleorganizer.service.JsonParser
import com.cdg.alex.simpleorganizer.settings_builder.JsonSettingsStringBuilder


//здесь нужно организовать перестановку дней периода в конце недели и запись в shared preferences
class PeriodSetterBuilder: SettingsToHolder {

    private fun readSavedAlarms(context: Context, numberOfAlarm: Int): ArrayList<Boolean> {
        val dayList = ArrayList<Boolean>()
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isMonday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isTuesday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isWednesday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isThursday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isFriday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isSaturday)
        dayList.add(readFromSettingsAndSaveToHolder(context)[numberOfAlarm].isSunday)
        return dayList
    }

    fun buildNewWeek(context: Context) {

        val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor :SharedPreferences.Editor = sharedPrefs.edit()

        for (i in 0..JsonParser.getNumberOfAlarms(context) - 1) {
            val dayList: ArrayList<Boolean> = ArrayList()
            val periodSetter = PeriodSetter(readSavedAlarms(context, i), readFromSettingsAndSaveToHolder(context)[i].period!!, context)
            dayList.addAll(periodSetter.buildNewPeriodWeek(i))
            val jsonSettingsStringBuilder: JsonSettingsStringBuilder = JsonSettingsStringBuilder.Builder()
                    .setTime(readFromSettingsAndSaveToHolder(context)[i].time.toString())
                    .setPeriod(readFromSettingsAndSaveToHolder(context)[i].period.toString())
                    .setRingtone(readFromSettingsAndSaveToHolder(context)[i].ringtone.toString())
                    .setOnOfSwitch(readFromSettingsAndSaveToHolder(context)[i].isOnOrOf)
                    .setMonday(dayList[0])
                    .setTuesday(dayList[1])
                    .setWednesday(dayList[2])
                    .setThursday(dayList[3])
                    .setFriday(dayList[4])
                    .setSaturday(dayList[5])
                    .setSunday(dayList[6])
                    .setCheckPeriod(readFromSettingsAndSaveToHolder(context)[i].isPeriodChecked)
                    .setAlarmId(readFromSettingsAndSaveToHolder(context)[i].id.toString())
                    .setSoundPath(readFromSettingsAndSaveToHolder(context)[i].soundPath.toString()).build()
            editor.putString(readFromSettingsAndSaveToHolder(context)[i].id.toString(), jsonSettingsStringBuilder.toString())
        }

        editor.apply()

    }


}
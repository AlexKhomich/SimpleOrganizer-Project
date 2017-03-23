package com.cdg.alex.simpleorganizer.utils

import android.content.Context
import com.cdg.alex.simpleorganizer.service.JsonParser
import com.cdg.alex.simpleorganizer.service.ServiceJsonParser
import com.cdg.alex.simpleorganizer.service.ServiceSettingsHolder


interface SettingsToHolder {

     fun readFromSettingsAndSaveToHolder(context: Context): ArrayList<ServiceSettingsHolder> {
        val settingsList = ArrayList<ServiceSettingsHolder>()
        val sjp = ServiceJsonParser()

        for (i in 0..JsonParser.getNumberOfAlarms(context) - 1) {
            sjp.setIndexOfAlarm(i)
            val serviceSettingsHolder = ServiceSettingsHolder(sjp.getTime(context), sjp.getSwitchState(context), sjp.mondayState(context), sjp.tuesdayState(context), sjp.wednesdayState(context),
                    sjp.thursdayState(context), sjp.fridayState(context), sjp.saturdayState(context), sjp.sundayState(context), sjp.checkPeriod(context), sjp.getRingtone(context), sjp.getPeriod(context),
                    sjp.getAlarmId(context), sjp.getSoundPath(context))

            settingsList.add(serviceSettingsHolder)
        }

        return settingsList
    }
}
package com.cdg.alex.simpleorganizer.service

import android.app.IntentService
import android.content.Intent
import com.cdg.alex.simpleorganizer.utils.AlarmTime
import com.cdg.alex.simpleorganizer.utils.NextAlarmHolder
import java.util.*

class AlarmService : IntentService("AlarmService") {

    private lateinit var serviceSettingsHolder: ServiceSettingsHolder
    private val sjp = ServiceJsonParser()
    private val settingsList = ArrayList<ServiceSettingsHolder>()

    override fun onHandleIntent(intent: Intent?) {


    }

//    вычитывает будильники из хранилища и сохраняет их в holder
    private fun readFromSettingsAndSaveToHolder(): ArrayList<ServiceSettingsHolder> {

        for (i in 0..JsonParser.getNumberOfAlarms(this) - 1) {
            sjp.setIndexOfAlarm(i)
            serviceSettingsHolder = ServiceSettingsHolder(sjp.getTime(this), sjp.getSwitchState(this), sjp.mondayState(this), sjp.tuesdayState(this), sjp.wednesdayState(this),
                    sjp.thursdayState(this), sjp.fridayState(this), sjp.saturdayState(this), sjp.sundayState(this), sjp.checkPeriod(this), sjp.getRingtone(this), sjp.getPeriod(this),
                    sjp.getAlarmId(this), sjp.getSoundPath(this))

            settingsList.add(serviceSettingsHolder)
        }

        return settingsList
    }

    private fun getOnOfState(index: Int): Boolean {
        val holderTemp = settingsList[index]
        return holderTemp.isOnOrOf
    }

    private fun getTime(index: Int): AlarmTime {
        val holderTemp = settingsList[index]
        val time: String? = holderTemp.time
        val argSplit: List<String>? = time?.split(":")
        val hour: Int? = argSplit?.get(0)?.toInt()
        val minute: Int? = argSplit?.get(2)?.toInt()
        return AlarmTime(hour, minute)
    }

    private fun getDaysList(index: Int): ArrayList<Boolean> {
        val holderTemp = settingsList[index]
        val daysList = ArrayList<Boolean>()
        daysList.add(holderTemp.isMonday)
        daysList.add(holderTemp.isTuesday)
        daysList.add(holderTemp.isWednesday)
        daysList.add(holderTemp.isThursday)
        daysList.add(holderTemp.isFriday)
        daysList.add(holderTemp.isSaturday)
        daysList.add(holderTemp.isSunday)
        return daysList
    }


    fun computeNextAlarm(): NextAlarmHolder {

        return NextAlarmHolder(0, getTime(0))

    }


}

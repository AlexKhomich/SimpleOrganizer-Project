package com.cdg.alex.simpleorganizer.service

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.cdg.alex.simpleorganizer.receiver.AlarmReceiver
import com.cdg.alex.simpleorganizer.utils.AlarmTime
import com.cdg.alex.simpleorganizer.utils.NextAlarmHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmService : IntentService("AlarmService") {

    override fun onHandleIntent(p0: Intent?) {
        setNextAlarmAndRunIt(this)
    }


    private lateinit var serviceSettingsHolder: ServiceSettingsHolder
    private val sjp = ServiceJsonParser()
    private val settingsList = ArrayList<ServiceSettingsHolder>()
    private val MONDAY: String = "Mon"
    private val TUESDAY: String = "Tue"
    private val WEDNESDAY: String = "Wed"
    private val THURSDAY: String = "Thu"
    private val FRIDAY: String = "Fri"
    private val SATURDAY: String = "Sat"
    private val SUNDAY: String = "Sun"

//    вычитывает будильники из хранилища и сохраняет их в holder
    private fun readFromSettingsAndSaveToHolder(context: Context): ArrayList<ServiceSettingsHolder> {

        for (i in 0..JsonParser.getNumberOfAlarms(context) - 1) {
            sjp.setIndexOfAlarm(i)
            serviceSettingsHolder = ServiceSettingsHolder(sjp.getTime(context), sjp.getSwitchState(context), sjp.mondayState(context), sjp.tuesdayState(context), sjp.wednesdayState(context),
                    sjp.thursdayState(context), sjp.fridayState(context), sjp.saturdayState(context), sjp.sundayState(context), sjp.checkPeriod(context), sjp.getRingtone(context), sjp.getPeriod(context),
                    sjp.getAlarmId(context), sjp.getSoundPath(context))

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
        val minute: Int? = argSplit?.get(1)?.toInt()
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


    fun computeNextAlarm(context: Context): NextAlarmHolder { //all works good in this function

       var nextAlarm: NextAlarmHolder = NextAlarmHolder(1, AlarmTime(8, 0))

        val alarmHolderList = ArrayList<NextAlarmHolder>()

        for (i in 0..readFromSettingsAndSaveToHolder(context).size - 1) {
            if (getOnOfState(i)) {
                val curHour = getCurrentHour(getCurrentDate())
                val curMinute = getCurrentMinute(getCurrentDate())
                val curDay = getCurrentDayOfWeek(getCurrentDate())
                val alarmTime: AlarmTime = getTime(i)
                val alarmDays: ArrayList<Boolean>  = getDaysList(i)

                for (k in 0..alarmDays.size - 1) {
                    if (alarmDays[k] && k == curDay) {
                        if (curHour == alarmTime.hours) {
                            if (curMinute < alarmTime.minutes!!) {
                                alarmHolderList.add(NextAlarmHolder(k, AlarmTime(alarmTime.hours, alarmTime.minutes)))
                                break
                            }
                        } else if (curHour < alarmTime.hours!!) {
                            alarmHolderList.add(NextAlarmHolder(k, AlarmTime(alarmTime.hours, alarmTime.minutes)))
                            break
                        }
                    } else {
                        if (alarmDays[k] && k > curDay) {
                            alarmHolderList.add(NextAlarmHolder(k, AlarmTime(alarmTime.hours, alarmTime.minutes)))
                            break
                        }
                    }
                }
            }
        }

        val iterator = alarmHolderList.listIterator()
        for (i in 0..alarmHolderList.size - 1) {
            if (alarmHolderList.size > 2) { // if elements are more than 2 in list
                iterator.next()
                if (iterator.hasNext()) { // here place has the mistake with increment
                    val day = alarmHolderList[i].dayOfWeek
                    var hour = alarmHolderList[i].time.hours
                    var minute = alarmHolderList[i].time.minutes
                    val nextDay = alarmHolderList[i + 1].dayOfWeek
                    var nextHours = alarmHolderList[i + 1].time.hours
                    var nextMinutes = alarmHolderList[i + 1].time.minutes

                    if (day == nextDay) {
                        hour = alarmHolderList[i].time.hours
                        nextHours = alarmHolderList[i + 1].time.hours
                        if (hour == nextHours) {
                            minute = alarmHolderList[i].time.minutes
                            nextMinutes = alarmHolderList[i + 1].time.minutes
                            if (minute!! < nextMinutes!!) {
                                val alarmTime = AlarmTime(hour, minute)
                                nextAlarm = NextAlarmHolder(day, alarmTime)
                                alarmHolderList.remove(alarmHolderList[i + 1])
                            } else if (nextMinutes < minute) {
                                val alarmTime = AlarmTime(nextHours, nextMinutes)
                                nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                                alarmHolderList.remove(alarmHolderList[i])
                            }
                        } else if (hour!! < nextHours!!) {
                            val alarmTime = AlarmTime(hour, minute)
                            nextAlarm = NextAlarmHolder(day, alarmTime)
                            alarmHolderList.remove(alarmHolderList[i + 1])
                        } else if (hour > nextHours) {
                            val alarmTime = AlarmTime(nextHours, nextMinutes)
                            nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                            alarmHolderList.remove(alarmHolderList[i])
                        }
                    } else if (day < nextDay) {
                        val alarmTime = AlarmTime(hour, minute)
                        nextAlarm = NextAlarmHolder(day, alarmTime)
                        alarmHolderList.remove(alarmHolderList[i + 1])
                    } else {
                        val alarmTime = AlarmTime(nextHours, nextMinutes)
                        nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                        alarmHolderList.remove(alarmHolderList[i])
                    }
                } else {
                    val alarmTime = AlarmTime(alarmHolderList[i].time.hours, alarmHolderList[i].time.minutes)
                    nextAlarm = NextAlarmHolder(alarmHolderList[i].dayOfWeek, alarmTime)
                }
            } else if (alarmHolderList.size <= 2) { // if elements are less or equals than 2 in list
                iterator.next()
                if (iterator.hasNext()) { // if the list has more then one element
                    val day = alarmHolderList[i].dayOfWeek
                    var hour = alarmHolderList[i].time.hours
                    var minute = alarmHolderList[i].time.minutes
                    val nextDay = alarmHolderList[i + 1].dayOfWeek
                    var nextHours = alarmHolderList[i + 1].time.hours
                    var nextMinutes = alarmHolderList[i + 1].time.minutes

                    if (day == nextDay) {
                        hour = alarmHolderList[i].time.hours
                        nextHours = alarmHolderList[i + 1].time.hours
                        if (hour == nextHours) {
                            minute = alarmHolderList[i].time.minutes
                            nextMinutes = alarmHolderList[i + 1].time.minutes
                            if (minute!! < nextMinutes!!) {
                                val alarmTime = AlarmTime(hour, minute)
                                nextAlarm = NextAlarmHolder(day, alarmTime)
                                alarmHolderList.remove(alarmHolderList[i + 1])
                            } else if (nextMinutes < minute) {
                                val alarmTime = AlarmTime(nextHours, nextMinutes)
                                nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                                alarmHolderList.remove(alarmHolderList[i])
                            }
                        } else if (hour!! < nextHours!!) {
                            val alarmTime = AlarmTime(hour, minute)
                            nextAlarm = NextAlarmHolder(day, alarmTime)
                            alarmHolderList.remove(alarmHolderList[i + 1])
                        } else if (hour > nextHours) {
                            val alarmTime = AlarmTime(nextHours, nextMinutes)
                            nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                            alarmHolderList.remove(alarmHolderList[i])
                        }
                    } else if (day < nextDay) {
                        val alarmTime = AlarmTime(hour, minute)
                        nextAlarm = NextAlarmHolder(day, alarmTime)
                        alarmHolderList.remove(alarmHolderList[i + 1])
                    } else {
                        val alarmTime = AlarmTime(nextHours, nextMinutes)
                        nextAlarm = NextAlarmHolder(nextDay, alarmTime)
                        alarmHolderList.remove(alarmHolderList[i])
                    }
                    break
                } else { // if the list has only one element
                    val alarmTime = AlarmTime(alarmHolderList[i].time.hours, alarmHolderList[i].time.minutes)
                    nextAlarm = NextAlarmHolder(alarmHolderList[i].dayOfWeek, alarmTime)
                }
            }
        }
            return nextAlarm
    }

    fun setNextAlarmAndRunIt(context: Context) {
        val calendar = Calendar.getInstance()
        val intent = Intent(context, AlarmReceiver::class.java)
        val savedAlarm: NextAlarmHolder = computeNextAlarm(context)
        calendar.set(Calendar.DAY_OF_WEEK, savedAlarm.dayOfWeek + 2)
        calendar.set(Calendar.HOUR_OF_DAY, savedAlarm.time.hours!!)
        calendar.set(Calendar.MINUTE, savedAlarm.time.minutes!!)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    fun getCurrentDate(): String {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("EEE:HH:mm", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun getCurrentDayOfWeek(regEx: String): Int {
        var temp: Int = 0
        val argSplit: List<String> = regEx.split(":")
        val dayOfWeek: String = argSplit[0]
        when (dayOfWeek) {
            MONDAY -> temp = 0
            TUESDAY -> temp = 1
            WEDNESDAY -> temp = 2
            THURSDAY -> temp = 3
            FRIDAY -> temp = 4
            SATURDAY -> temp = 5
            SUNDAY -> temp = 6
        }
        return  temp
    }

    fun getCurrentHour(regEx: String): Int {
        val argSplit: List<String> = regEx.split(":")
        val currentHour: Int = argSplit[1].toInt()
        return currentHour
    }

    fun getCurrentMinute(regEx: String): Int {
        val argSplit: List<String> = regEx.split(":")
        val currentMinute: Int = argSplit[2].toInt()
        return currentMinute
    }

}

package com.cdg.alex.simpleorganizer.service

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.cdg.alex.simpleorganizer.receiver.AlarmReceiver
import com.cdg.alex.simpleorganizer.receiver.StartAlarmServiceReceiver
import com.cdg.alex.simpleorganizer.utils.AlarmTime
import com.cdg.alex.simpleorganizer.utils.NextAlarmHolder
import com.cdg.alex.simpleorganizer.utils.SettingsToHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmService : IntentService("AlarmService"), SettingsToHolder {

    override fun onHandleIntent(p0: Intent?) {
        setNextAlarmAndRunIt(this)
    }

    private val MONDAY: String = "Mon"
    private val TUESDAY: String = "Tue"
    private val WEDNESDAY: String = "Wed"
    private val THURSDAY: String = "Thu"
    private val FRIDAY: String = "Fri"
    private val SATURDAY: String = "Sat"
    private val SUNDAY: String = "Sun"

    private fun getOnOfState(index: Int): Boolean {
        val holderTemp = readFromSettingsAndSaveToHolder(this)[index]
        return holderTemp.isOnOrOf
    }

    private fun getTime(index: Int): AlarmTime {
        val holderTemp = readFromSettingsAndSaveToHolder(this)[index]
        val time: String? = holderTemp.time
        val argSplit: List<String>? = time?.split(":")
        val hour: Int? = argSplit?.get(0)?.toInt()
        val minute: Int? = argSplit?.get(1)?.toInt()
        return AlarmTime(hour, minute)
    }

    private fun getDaysList(index: Int): ArrayList<Boolean> {
        val holderTemp = readFromSettingsAndSaveToHolder(this)[index]
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
       var nextAlarm: NextAlarmHolder
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

        if (alarmHolderList.isNotEmpty()) {
            nextAlarm = alarmHolderList[0]
            if (alarmHolderList.size > 1) {
                for (i in 1..alarmHolderList.size - 1) {
                    if (nextAlarm.dayOfWeek == alarmHolderList[i].dayOfWeek) {
                        if (nextAlarm.time.hours == alarmHolderList[i].time.hours) {
                            if (nextAlarm.time.minutes!! > alarmHolderList[i].time.minutes!!){
                                nextAlarm = alarmHolderList[i]
                            }
                        } else if (nextAlarm.time.hours!! > alarmHolderList[i].time.hours!!) {
                            nextAlarm = alarmHolderList[i]
                        }
                    } else if (nextAlarm.dayOfWeek > alarmHolderList[i].dayOfWeek) {
                        nextAlarm = alarmHolderList[i]
                    }
                }
            }
        } else return NextAlarmHolder(-1, AlarmTime(24, 60))

        return nextAlarm
    }

    fun setNextAlarmAndRunIt(context: Context) {
        val calendar = Calendar.getInstance()
        val savedAlarm: NextAlarmHolder = computeNextAlarm(context)

        if (savedAlarm.dayOfWeek == -1 && savedAlarm.time.hours == 24 && savedAlarm.time.minutes == 60) {
            calendar.add(Calendar.DAY_OF_YEAR, getShiftDayOfMonth(getCurrentDayOfWeek(getCurrentDate())))
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 10)
            val intent = Intent(context, StartAlarmServiceReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            Log.i("TAG", "Alarm has been installing for build new week")
        } else {
            if ((savedAlarm.dayOfWeek + 2) > 7 && getCurrentDayOfWeek(getCurrentDate()) != 6) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, savedAlarm.time.hours!!)
                calendar.set(Calendar.MINUTE, savedAlarm.time.minutes!!)
                calendar.set(Calendar.SECOND, 0)
            } else if ((savedAlarm.dayOfWeek + 2) > 7 && getCurrentDayOfWeek(getCurrentDate()) == 6) {
                calendar.set(Calendar.HOUR_OF_DAY, savedAlarm.time.hours!!)
                calendar.set(Calendar.MINUTE, savedAlarm.time.minutes!!)
                calendar.set(Calendar.SECOND, 0)
            } else {
                calendar.set(Calendar.DAY_OF_WEEK, savedAlarm.dayOfWeek + 2)
                calendar.set(Calendar.HOUR_OF_DAY, savedAlarm.time.hours!!)
                calendar.set(Calendar.MINUTE, savedAlarm.time.minutes!!)
                calendar.set(Calendar.SECOND, 0)
            }

            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
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

    fun getShiftDayOfMonth(currDay: Int): Int {
        var temp: Int = 0
        when (currDay) {
            0 -> temp = 7
            1 -> temp = 6
            2 -> temp = 5
            3 -> temp = 4
            4 -> temp = 3
            5 -> temp = 2
            6 -> temp = 1
        }
        return temp
    }

}

package com.cdg.alex.simpleorganizer.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cdg.alex.simpleorganizer.R
import com.cdg.alex.simpleorganizer.receiver.AlarmReceiver
import com.cdg.alex.simpleorganizer.service.AlarmService
import com.cdg.alex.simpleorganizer.service.MediaPlayerService

class AlarmNotificationActivity : AppCompatActivity() {

    private var br: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notification)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.intent.action.STOP")
        br = object : BroadcastReceiver() {
            @SuppressLint("ObsoleteSdkInt")
            override fun onReceive(context: Context?, intent: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity()
                    } else {
                        finish()
                    }
                }
            }
        }
        this.registerReceiver(br, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(br)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun onStopAlarmRing() {
        val intent = Intent(this, MediaPlayerService::class.java)
        this.stopService(intent)
        //запуск нового будильника
        val alarmServiceIntent = Intent(this, AlarmService::class.java)
        this.startService(alarmServiceIntent)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            } else {
                finish()
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun onClickSnooze() {
        //should create function for set snooze time
        val shPrefSettings: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val timeOfSnnoze: Long = shPrefSettings.getString("time_of_snooze", "5").toLong()
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeOfSnnoze * 60_000, pendingIntent)
        val mediaServiceIntent = Intent(this, MediaPlayerService::class.java)
        this.stopService(mediaServiceIntent)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            } else {
                finish()
            }
        }
    }
}

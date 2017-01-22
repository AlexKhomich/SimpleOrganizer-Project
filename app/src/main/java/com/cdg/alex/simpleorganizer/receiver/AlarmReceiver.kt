package com.cdg.alex.simpleorganizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cdg.alex.simpleorganizer.AlarmNotificationActivity
import com.cdg.alex.simpleorganizer.service.MediaPlayerService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i("TAG", "ALARM!!!")

        val mediaServiceIntent = Intent(context, MediaPlayerService::class.java)

        val alarmNotificationIntent = Intent(context, AlarmNotificationActivity::class.java)

        context?.startService(mediaServiceIntent)
        context?.startActivity(alarmNotificationIntent)


    }
}
package com.cdg.alex.simpleorganizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cdg.alex.simpleorganizer.service.AlarmService


class StartAlarmServiceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmServiceStarter = Intent(context, AlarmService::class.java)
        context?.startService(alarmServiceStarter)
    }
}
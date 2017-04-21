package com.cdg.alex.simpleorganizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cdg.alex.simpleorganizer.service.AlarmService
import com.cdg.alex.simpleorganizer.service.PeriodBuilderService


class StartAlarmServiceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmServiceStarter = Intent(context, PeriodBuilderService::class.java)
        context?.startService(alarmServiceStarter)
    }
}
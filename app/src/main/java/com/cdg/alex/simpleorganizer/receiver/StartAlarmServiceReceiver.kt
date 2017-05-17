package com.cdg.alex.simpleorganizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cdg.alex.simpleorganizer.service.PeriodBuilderService


class StartAlarmServiceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TAG", "Period builder receiver")
        val alarmServiceStarter = Intent(context, PeriodBuilderService::class.java)
        context?.startService(alarmServiceStarter)
    }
}
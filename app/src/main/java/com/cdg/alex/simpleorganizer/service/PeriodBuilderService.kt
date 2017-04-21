package com.cdg.alex.simpleorganizer.service

import android.app.IntentService
import android.content.Intent
import com.cdg.alex.simpleorganizer.utils.PeriodSetterBuilder


class PeriodBuilderService : IntentService("PeriodBuilderService") {

    override fun onHandleIntent(intent: Intent?) {
        val periodSetterBuilder = PeriodSetterBuilder()
        periodSetterBuilder.buildNewWeek(this)
        //start service for set new alarm in new week
        val alarmServiceIntent: Intent = Intent(this, AlarmService::class.java)
        this.startService(alarmServiceIntent)

    }
}



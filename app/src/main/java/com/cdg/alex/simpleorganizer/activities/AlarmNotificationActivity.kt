package com.cdg.alex.simpleorganizer.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cdg.alex.simpleorganizer.R
import com.cdg.alex.simpleorganizer.service.MediaPlayerService

class AlarmNotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notification)

    }

    fun onStopAlarmRing (view: View) {
        val intent = Intent(this, MediaPlayerService::class.java)
        this.stopService(intent)
        finish()
    }
}

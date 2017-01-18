package com.cdg.alex.simpleorganizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cdg.alex.simpleorganizer.service.MediaPlayerService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val mediaServiceIntent = Intent(context, MediaPlayerService::class.java)

        context?.startService(mediaServiceIntent)

    }
}
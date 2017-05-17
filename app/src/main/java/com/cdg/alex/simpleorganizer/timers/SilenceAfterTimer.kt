package com.cdg.alex.simpleorganizer.timers

import android.content.Context
import android.content.Intent
import android.util.Log
import com.cdg.alex.simpleorganizer.service.MediaPlayerService
import java.util.*


class SilenceAfterTimer(val context: Context): TimerTask() {
    override fun run() {
        val mediaPlayerStopIntent: Intent = Intent(context, MediaPlayerService::class.java)
        context.stopService(mediaPlayerStopIntent)
        val intent: Intent = Intent("android.intent.action.STOP").putExtra("msg_stop", true)
        context.sendBroadcast(intent)
        Log.i("TAG", "Task is running...")
    }
}
package com.cdg.alex.simpleorganizer.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.cdg.alex.simpleorganizer.R
import com.cdg.alex.simpleorganizer.activities.AlarmNotificationActivity
import com.cdg.alex.simpleorganizer.timers.SilenceAfterTimer
import java.util.*


class MediaPlayerService : Service() {

    private var songPath: String? = null
    private var mediaPlayer: MediaPlayer? = null
    private var songURI: Uri? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val shPrefSettings: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val volume: Float = (shPrefSettings.getString("volume", "50").toFloat())/100
        val silenceAfter: String = shPrefSettings.getString("silence_after", "1")
        val serviceParser: ServiceJsonParser = ServiceJsonParser()
        songPath = serviceParser.getSoundPath(this)
        val resultString: String? = songPath
        Log.i("path", resultString)
        if (resultString.equals("android.resource://com.cdg.alex.simpleorganizer/")) {
            songURI = Uri.parse(resultString + R.raw.alarm_default)
        } else songURI = Uri.parse(resultString)

        // TODO: realise this function with RingtoneManager class.
//        val ringtoneManager: Unit = RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, songURI)
        mediaPlayer = MediaPlayer.create(this, songURI)
        mediaPlayer?.setVolume(volume, volume)
        mediaPlayer?.start()

        sendNotification()

        val timer: Timer = Timer()
        timer.schedule(SilenceAfterTimer(this), getTimeFromString(silenceAfter) * 60_000)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    fun sendNotification() {
        val notif = NotificationCompat.Builder(this)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_access_alarms)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle("Alarm notification")
                .setContentText("ALARM!!!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)

        val resultIntent = Intent(this, AlarmNotificationActivity::class.java)
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(this)
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AlarmNotificationActivity::class.java)
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        notif.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, notif.build())
    }

    fun getTimeFromString(str: String): Long {
        val strArr: List<String> = str.split(" ")
        val result: Long = strArr[0].toLong()
        return result
    }
}



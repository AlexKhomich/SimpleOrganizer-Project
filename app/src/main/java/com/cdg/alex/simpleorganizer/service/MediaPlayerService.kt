package com.cdg.alex.simpleorganizer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.IBinder
import com.cdg.alex.simpleorganizer.R
import com.cdg.alex.simpleorganizer.activities.AlarmNotificationActivity


class MediaPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val volume: Float = (sharedPreferences.getString("volume", "50").toFloat())/100
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_default)
        mediaPlayer?.setVolume(volume, volume)
        mediaPlayer?.start()

        sendNotification()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    fun sendNotification () {
        val notif = Notification.Builder(this)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Alarm notification")
                .setContentText("ALARM!!!")
                .setAutoCancel(true)
                .setDefaults(Notification.PRIORITY_HIGH)

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

}

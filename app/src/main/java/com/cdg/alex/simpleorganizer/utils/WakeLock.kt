package com.cdg.alex.simpleorganizer.utils

import android.content.Context
import android.os.PowerManager

/**
 * Created by alex on 19/05/17.
 */
class WakeLock {
    companion object {
        var wakeLock: PowerManager.WakeLock? = null
        fun acquire(context: Context) {
            wakeLock?.release()
            val mPowerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Alarm")
            wakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "Alarm")
            wakeLock = mPowerManager.newWakeLock(PowerManager.ON_AFTER_RELEASE, "Alarm")
            wakeLock?.acquire()
        }

        fun release() {
            if (wakeLock != null) wakeLock?.release()
            wakeLock = null
        }
    }
}
package com.cdg.alex.simpleorganizer.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.util.*

/**
 * Created by alex on 26/10/16.
 */

class AlarmMediaPlayer(private val DATA_SOURCE: Uri, private val SILENCE_AFTER_TIME: Long) {
    private var mediaPlayer: MediaPlayer? = null

    fun startPlay(context: Context) {
        val timer = Timer()
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        } else {
            mediaPlayer = MediaPlayer.create(context, DATA_SOURCE)
            mediaPlayer!!.start()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.release()
                    }
                }
            }, SILENCE_AFTER_TIME)
        }
    }
}

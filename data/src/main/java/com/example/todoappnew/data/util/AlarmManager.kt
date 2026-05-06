package com.example.todoappnew.data.util

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var ringtone: Ringtone? = null

    fun playAlarm() {
        if (ringtone?.isPlaying == true) return
        
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()
    }

    fun stopAlarm() {
        ringtone?.stop()
        ringtone = null
    }
}

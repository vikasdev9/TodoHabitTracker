package com.example.todoappnew.data.util

import android.content.Context
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
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
        
        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        }
        
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone?.audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        }

        ringtone?.play()
    }

    fun stopAlarm() {
        ringtone?.stop()
        ringtone = null
    }
}

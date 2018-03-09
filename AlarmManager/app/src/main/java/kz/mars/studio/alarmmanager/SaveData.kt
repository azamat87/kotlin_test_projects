package kz.mars.studio.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

/**
 * Created by Asus on 04.03.2018.
 */
class SaveData(private val context: Context) {

    private var pref: SharedPreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE)


    fun saveData(hour: Int, minute: Int){
        var editor = pref.edit()
        editor.putInt("hour", hour)
        editor.putInt("minute", minute)
        editor.apply()
    }

    fun getHour(): Int {
        return pref.getInt("hour", 0)
    }

    fun getMinute(): Int {
        return pref.getInt("minute", 0)
    }

    fun setAlarm() {
        val hour: Int = getHour()
        val minute: Int = getMinute()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, myBroadcastReceiver::class.java)
        intent.putExtra("message", "alarm time")
        intent.action = "com.test.alrmmanager"

        val pi = PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pi)


    }
}
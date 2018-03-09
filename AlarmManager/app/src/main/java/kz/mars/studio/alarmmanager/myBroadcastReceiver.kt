package kz.mars.studio.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Asus on 04.03.2018.
 */
class myBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "com.test.alrmmanager") {
                val b = intent.extras
                val notify = Notifications()
                notify.Notify(context!!, b.getString("message", ""),10)
            }else if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                val saveData = SaveData(context!!)
                saveData.setAlarm()
            }
        }
    }
}
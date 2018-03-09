package kz.mars.studio.alarmmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val saveData = SaveData(applicationContext)
        tvShowTime.text = saveData.getHour().toString() + ":" + saveData.getMinute().toString()
    }

    fun BtnSetTime(view: View){
        val popTime = PopTime()
        popTime.show(fragmentManager, "Select time")

    }

    fun SetTime(hour: Int, minute: Int) {
        tvShowTime.text = hour.toString() + ":"+minute.toString()
        val saveData = SaveData(applicationContext)
        saveData.saveData(hour, minute)
        saveData.setAlarm()
    }

}

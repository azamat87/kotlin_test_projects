package kz.mars.studio.alarmmanager

import android.app.DialogFragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pop_time.view.*

/**
 * Created by Asus on 04.03.2018.
 */
class PopTime: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater!!.inflate(R.layout.pop_time, container, false)

        val buDone = view.buDone
        val timePicker = view.timePicker

        buDone.setOnClickListener {

            val ma = activity as MainActivity
            if (Build.VERSION.SDK_INT >= 23) {
                ma.SetTime(timePicker.hour, timePicker.minute)
            } else {
                ma.SetTime(timePicker.currentHour, timePicker.currentMinute)
            }
            dismiss()
        }

        return view
    }
}
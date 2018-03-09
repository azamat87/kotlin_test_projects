package kz.mars.studio.findage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun buFindAgeEvent(view: View) {
        val yearOfBirth = edYearOfBirth.text.toString().toInt()
        if (yearOfBirth == 0) {
            tvShowAge.text = "Invalid input"
            return
        }
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val myAge = currentYear - yearOfBirth
        tvShowAge.text = "Your Age is $myAge years"
    }
}

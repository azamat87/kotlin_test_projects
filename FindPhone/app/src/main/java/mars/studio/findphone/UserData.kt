package mars.studio.findphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

/**
 * Created by Asus on 09.03.2018.
 */
class UserData(val context: Context) {

    var sharedPref: SharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    fun savePhone(number: String) {
        val editor = sharedPref.edit()
        editor.putString("phoneNumber", number)
        editor.apply()

    }

    fun loadPhoneNumber():String {
        val phoneNumber = sharedPref.getString("phoneNumber", "")

        return phoneNumber
    }

    fun isFirstTimeLoad() {
        val phoneNumber = sharedPref.getString("phoneNumber", "")
        if (phoneNumber == "") {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
    }

    fun saveContactInfo() {
        var listOfTrackers = ""
        for ((key, value) in myTrackers) {
            if (listOfTrackers.length == 0) {
                listOfTrackers = key + "%" + value
            } else {
                listOfTrackers += "%" + key + "%" + value
            }
        }
        if (listOfTrackers.length == 0) {
            listOfTrackers = "empty"
        }

        val editor = sharedPref.edit()
        editor.putString("my_trackers", listOfTrackers)
        editor.apply()

    }

    fun loadContactInfo() {
        myTrackers.clear()
        var listOfTrackers = sharedPref.getString("my_trackers", "empty")

        if (listOfTrackers != "empty") {
            val userInfo = listOfTrackers.split("%").toTypedArray()
            var i = 0

            while (i < userInfo.size) {
                myTrackers.put(userInfo[i], userInfo[i+1])
                i += 2
            }
        }

    }

    companion object {
        var myTrackers: MutableMap<String, String> = HashMap()

        fun formatPhoneNumber(number: String): String {
            var onlyNumber = number.replace("[^0-9]".toRegex(), "")
//            if (number[0] == '+') {
//                onlyNumber = "+" + number
//            }

            return onlyNumber
        }
    }

}
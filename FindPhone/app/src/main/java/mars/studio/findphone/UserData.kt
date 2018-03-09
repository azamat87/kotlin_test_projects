package mars.studio.findphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

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
        val phoneNumber = sharedPref.getString("phoneNumber", "0")
        if (phoneNumber == "0") {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
        return phoneNumber
    }

}
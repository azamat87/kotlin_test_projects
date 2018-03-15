package mars.studio.twitterweb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

/**
 * Created by Asus on 14.03.2018.
 */
class SaveSettings(val context: Context) {

    val shared: SharedPreferences = context.getSharedPreferences("myRef", Context.MODE_PRIVATE)


    fun saveSettings(userId: String) {
        val editor = shared.edit()
        editor.putString("userID", userId)
        editor.apply()
    }

    fun loadSettings() {
        userID = shared.getString("userID", "")

        if (userID == "") {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    companion object {
        var userID = ""
    }
}
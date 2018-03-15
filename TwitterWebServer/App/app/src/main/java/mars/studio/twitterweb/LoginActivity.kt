package mars.studio.twitterweb

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }

    fun buLoginEvent(view: View) {
        val url = "http://dec.local/register.php?" + "email="+ed_email.text.toString()+"&password="+ed_password.text.toString()
        MyAsyncTask().execute(url)
    }

    fun buToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    // http
    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var url = URL(params[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                var inString = convertStremToString(urlConnect.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val json = JSONObject(values[0])

                if (json.getString("msg") == "pass login") {
                    val userInfo = JSONArray(json.getString("info"))
                    val userCredentils = userInfo.getJSONObject(0)
                    val user_id = userCredentils.getString("user_id")

                    Toast.makeText(applicationContext, userCredentils.getString("first_name"), Toast.LENGTH_SHORT).show()

                    val saveSettings = SaveSettings(applicationContext)
                    saveSettings.saveSettings(user_id)

                    finish()
                } else {
                    bu_register.isEnabled = false
                    Toast.makeText(applicationContext, json.getString("msg"), Toast.LENGTH_SHORT).show()

                }
            } catch (ex: Exception) {

            }
        }


    }
}

package kz.mars.studio.getsunset

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getSunSet(view: View) {
        var city = edCityName.text.toString()

        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsync().execute(url)
    }

    inner class MyAsync : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 7000
                var inString = convertStremToString(urlConnection.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {

            }
            return " "
        }

        private fun convertStremToString(inputStream: InputStream?): String {
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = null
            var AllString: String = ""

            try {
                do {
                    line = bufferReader.readLine()
                    if (line != null) {
                        AllString += line
                    }
                } while (line != null)
            } catch (ex: Exception) {

            }
            return AllString
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            try {
                var json = JSONObject(values[0])
                val query = json.getJSONObject("query")
                val results = query.getJSONObject("results")
                val channel = results.getJSONObject("channel")
                val astronomy = channel.getJSONObject("astronomy")
                val sunrise = astronomy.getString("sunrise")

                tvSunSet.text = "Sun rise time is $sunrise"
            } catch (ex: Exception) {

            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }

    }
}

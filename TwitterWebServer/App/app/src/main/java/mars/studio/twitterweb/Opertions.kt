package mars.studio.twitterweb

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by Asus on 14.03.2018.
 */


fun convertStremToString(inputStream: InputStream): String {
    Log.e("myLog", " inputStream " + inputStream.markSupported())
    Log.e("myLog", " inputStream " + inputStream.available())
    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    Log.e("myLog", " bufferReader " + bufferReader.lineSequence())
    var line: String? = null
    var AllString: String = ""
    Log.e("myLog", " convertStremToString ")
    try {
        do {
            line = bufferReader.readLine()
            if (line != null) {
                AllString += line
            }
        } while (line != null)

        Log.e("myLog", " convertStremToString 2 " + AllString)
    } catch (ex: Exception) {

    }
    return AllString
}
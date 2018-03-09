package mars.studio.sensorlight

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensor: Sensor? = null
    var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

    }

    var isRunning = false
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    var mp = MediaPlayer()
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.values[0] > 40 && isRunning == false) {
            isRunning = true
            try {
                var mp = MediaPlayer()
                mp.setDataSource("")
                mp.prepare()
                mp.start()

            } catch (ex: Exception) {

            }

        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()
        sensorManager!!.unregisterListener(this)
        mp.stop()
    }
}

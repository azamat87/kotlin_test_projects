package mars.studio.sensoraccelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensor: Sensor? = null
    var sensorManager: SensorManager ?= null

    var xold = 0.0
    var yold = 0.0
    var zold = 0.0

    var threadShould = 3000.0
    var oldTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        var x = event!!.values[0]
        var y = event.values[1]
        var z = event.values[2]

        var currentTime = System.currentTimeMillis()
        if ((currentTime - oldTime) > 100) {
            var timeDiff = currentTime - oldTime
            oldTime = currentTime
            var speed = Math.abs(x + y + z - xold - yold - zold)/timeDiff*10000
            if (speed > threadShould) {
                var v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(500)
                Toast.makeText(this, "Shock", Toast.LENGTH_LONG).show()
            }
        }

    }


}

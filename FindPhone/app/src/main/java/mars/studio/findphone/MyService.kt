package mars.studio.findphone

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Asus on 11.03.2018.
 */
class MyService: Service() {

    private lateinit var mDataBaseRef: DatabaseReference

    companion object {
        var isServiceRunning = false
    }

    override fun onBind(intent: Intent?): IBinder {

        return null!!
    }

    override fun onCreate() {
        super.onCreate()
        mDataBaseRef = FirebaseDatabase.getInstance().reference
        isServiceRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var locationListener = MyLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, locationListener)

        val userData = UserData(this)
        val myPhoneNumber = userData.loadPhoneNumber()
        mDataBaseRef.child("Users").child(myPhoneNumber).child("request")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        if (myLocation == null) return
                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
                                .child("lat").setValue(myLocation!!.latitude)
                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
                                .child("lon").setValue(myLocation!!.longitude)

                        val df = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
                        val date = Date()
                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
                                .child("lastOnline").setValue(df.format(date).toString())
                    }

                    override fun onDataChange(p0: DataSnapshot?) {

                    }
                })

        return START_NOT_STICKY
    }

    var myLocation: Location?= null

    inner class MyLocationListener: LocationListener {

        constructor(): super(){
            myLocation = Location("me")
            myLocation!!.latitude = 0.0
            myLocation!!.longitude =0.0
        }

        override fun onLocationChanged(location: Location?) {
            myLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }
    }
}
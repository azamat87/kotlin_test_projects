package kz.mars.studio.pockemon

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var listPockemons = ArrayList<Pockemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermmision()
        loadPockemon()
    }

    val ACCESS_LOCATION = 123

    fun checkPermmision() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
                return
            }
        }
        GetUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation() {
        Toast.makeText(this, " GetUserLocation ", Toast.LENGTH_SHORT).show()
        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        val thread = myThread()
        thread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, " We cannot access to your location ", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

    // Get user location
    var loc: Location ?= null
    inner class MyLocationListener : LocationListener {

        constructor(){
            loc = Location("Start")
            Log.e("myLog", "MyLocationListener ")
        }
        override fun onLocationChanged(location: Location?) {
            loc = location
            Log.e("myLog", "onLocationChanged ")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }
    }

    var oldLocation: Location? = null

    inner class myThread : Thread{

        constructor() : super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run() {
            while (true) {
                try {
                    if (oldLocation!!.distanceTo(loc) == 0f) {
                        continue
                    }

                    oldLocation = loc

                    runOnUiThread{
                        mMap.clear()
                        val sydney = LatLng(loc!!.latitude, loc!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                                .title("Me"))
                                .snippet = " here is my location"
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        // pockemons
                        for (listPockemon in listPockemons) {
                            if (!listPockemon.isCatch) {

                                val pockemonLoc = LatLng(listPockemon.location!!.latitude, listPockemon.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pockemonLoc)
                                        .icon(BitmapDescriptorFactory.fromResource(listPockemon.image!!))
                                        .title(listPockemon.name))
                                        .snippet = listPockemon.des + " power: " + listPockemon.power

                                if (loc!!.distanceTo(listPockemon.location) < 2) {
                                    listPockemon.isCatch = true
                                }
                            }
                        }
                    }

                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
            }
        }
    }

    fun loadPockemon() {

        listPockemons.add(Pockemon(R.drawable.charmander,
                "Charmander", "Charmander living in japan", 55.0, 37.7789994893035, -122.401846647263))
        listPockemons.add(Pockemon(R.drawable.bulbasaur,
                "Bulbasaur", "Bulbasaur living in usa", 90.5, 37.7949568502667, -122.410494089127))
        listPockemons.add(Pockemon(R.drawable.squirtle,
                "Squirtle", "Squirtle living in iraq", 33.5, 37.7816621152613, -122.41225361824))

    }
}

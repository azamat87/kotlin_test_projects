package mars.studio.findphone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var databaseRef: DatabaseReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val bundle = intent.extras
        val number = bundle.getString("phoneNumber")

        databaseRef = FirebaseDatabase.getInstance().reference
        databaseRef!!.child("Users").child(number).child("location")
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {

                        try {
                            val td = p0!!.value as HashMap<String, Any>
                            val lat = td["lat"].toString()
                            val lon = td["lon"].toString()
                            position = LatLng(lat.toDouble(), lon.toDouble())
                            val lastOnline = td["lastOnline"].toString()
                            lastOnlineStr = lastOnline
                            loadMap()
                        }catch (ex: Exception){}

                    }
                })

    }

    fun loadMap(){
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    companion object {
        var position = LatLng(0.0, 0.0)
        var lastOnlineStr = ""

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.addMarker(MarkerOptions().position(position).title(lastOnlineStr))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }
}

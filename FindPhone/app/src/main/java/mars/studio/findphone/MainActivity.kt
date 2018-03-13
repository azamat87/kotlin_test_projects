package mars.studio.findphone

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var listOfContact = ArrayList<UserContact>()
    var adapter: ContactAdapter ?= null

    private lateinit var mDataBaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDataBaseRef = FirebaseDatabase.getInstance().reference

//        dummyData()
        adapter = ContactAdapter(this, listOfContact)
        lv_contact_main.adapter = adapter

        lv_contact_main.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val userInfo = listOfContact[position]

            val df = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
            val date = Date()
            mDataBaseRef.child("Users").child(userInfo.phoneNumber).child("request").setValue(df.format(date))

            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("phoneNumber", userInfo.phoneNumber)
            startActivity(intent)
        }

        val userData = UserData(this)
        userData.isFirstTimeLoad()

    }

    override fun onResume() {
        super.onResume()
        val userData = UserData(this)
        if (userData.loadPhoneNumber() == "") {
            return
        }
        refreshUsers()
        checkContactPermission()
        checkLocationPermission()
    }

    fun refreshUsers() {
        val userData = UserData(this)
            mDataBaseRef.child("Users")
                    .child(userData.loadPhoneNumber())
                    .child("Finders")
                    .addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError?) {

                        }

                        override fun onDataChange(data: DataSnapshot?) {
                            var td: HashMap<String, Any> ?= data!!.value as HashMap<String, Any>
                            listOfContact.clear()

                            if (td == null) {
                                listOfContact.add(UserContact("no_users", ""))
                                adapter!!.notifyDataSetChanged()
                                return
                            }
                            for (key in td.keys) {
                                val name = listOfContacts[key]
                                listOfContact.add(UserContact( name.toString(), key))
                            }
                            adapter!!.notifyDataSetChanged()
                        }
                    })


    }

//    fun dummyData() {
//        listOfContact.add(UserContact("Name 1", "5454645646"))
//        listOfContact.add(UserContact("Name 2", "5454645646"))
//        listOfContact.add(UserContact("Name 3", "5454645646"))
//        listOfContact.add(UserContact("Name 4", "5454645646"))
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.add_tracker -> {
                val intent = Intent(this, MyTrackersActivity::class.java)
                startActivity(intent)
            }
            R.id.help -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    val CONTACT_CODE = 123

    fun checkContactPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), CONTACT_CODE)
                return
            }
        }
        loadContact()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            CONTACT_CODE ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContact()
                } else {
                    Toast.makeText(this, "Cannot access to contact", Toast.LENGTH_SHORT).show()
                }
            }
            LOCTAION_CODE ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLoaction()
                } else {
                    Toast.makeText(this, "Cannot access to location", Toast.LENGTH_SHORT).show()
                }
            } else -> {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        }
    }

    val listOfContacts = HashMap<String, String>()
    private fun loadContact() {
        try {
            listOfContacts.clear()
            val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null)
            cursor.moveToFirst()
            do {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                listOfContacts.put(UserData.formatPhoneNumber(phoneNumber), name)
            } while (cursor.moveToNext())
        } catch (ex: Exception) { }
    }

    val LOCTAION_CODE = 124

    fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCTAION_CODE)
                return
            }
        }
        getUserLoaction()
    }

    fun getUserLoaction() {

        if (!MyService.isServiceRunning) {
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }
//        var locationListener = MyLocationListener()
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, locationListener)
//
//        val userData = UserData(this)
//        val myPhoneNumber = userData.loadPhoneNumber()
//        mDataBaseRef.child("Users").child(myPhoneNumber).child("request")
//                .addValueEventListener(object : ValueEventListener{
//                    override fun onCancelled(p0: DatabaseError?) {
//                        if (myLocation == null) return
//                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
//                                .child("lat").setValue(myLocation!!.latitude)
//                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
//                                .child("lon").setValue(myLocation!!.longitude)
//
//                        val df = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
//                        val date = Date()
//                        mDataBaseRef.child("Users").child(myPhoneNumber).child("location")
//                                .child("lastOnline").setValue(df.format(date).toString())
//                    }
//
//                    override fun onDataChange(p0: DataSnapshot?) {
//
//                    }
//                })
    }

//    var myLocation: Location ?= null
//
//    inner class MyLocationListener: LocationListener{
//
//        constructor(): super(){
//            myLocation = Location("me")
//            myLocation!!.latitude = 0.0
//            myLocation!!.longitude =0.0
//        }
//
//        override fun onLocationChanged(location: Location?) {
//            myLocation = location
//        }
//
//        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//
//        }
//
//        override fun onProviderEnabled(provider: String?) {
//
//        }
//
//        override fun onProviderDisabled(provider: String?) {
//
//        }
//    }
}

package mars.studio.findphone

import android.app.Activity
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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_my_trackers.*

class MyTrackersActivity : AppCompatActivity() {

    var listOfContact = ArrayList<UserContact>()
    var adapter: ContactAdapter ?= null
    private lateinit var userData : UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
        userData = UserData(this)

//        dummyData()
        adapter = ContactAdapter(this, listOfContact)
        lv_contact.adapter = adapter
        lv_contact.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val userInfo = listOfContact[position]
            UserData.myTrackers.remove(userInfo.phoneNumber)
            refreshData()

            userData.saveContactInfo()

            val mDatabase = FirebaseDatabase.getInstance().reference

            mDatabase.child("Users").child(userInfo.phoneNumber).child("Finders").child(userData.loadPhoneNumber()).removeValue()
        }
        userData.loadContactInfo()

        refreshData()
    }

    fun dummyData() {
        listOfContact.add(UserContact("Name 1", "5454645646"))
        listOfContact.add(UserContact("Name 2", "5454645646"))
        listOfContact.add(UserContact("Name 3", "5454645646"))
        listOfContact.add(UserContact("Name 4", "5454645646"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tracker_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.addContact -> {
                checkPermission()
            }
            R.id.finishActivity -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val CONTACT_CODE = 123

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), CONTACT_CODE)
                return
            }
        }
        pickContact()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            CONTACT_CODE ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact()
                } else {
                    Toast.makeText(this, "Cannot access to contact", Toast.LENGTH_SHORT).show()
                }
            } else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    val PICK_CODE = 1
    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CODE)

    }

    fun refreshData() {
        listOfContact.clear()
        for ((key, value) in UserData.myTrackers) {
            listOfContact.add(UserContact(value, key))
        }
        adapter!! .notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val contactData = data!!.data
                    val c = contentResolver.query(contactData, null, null, null, null)
                    while (c.moveToNext()) {
                        val id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                        val hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if (hasPhone == "1") {
                            val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null)
                            phones.moveToFirst()
                            val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            var phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            phoneNumber = UserData.formatPhoneNumber(phoneNumber)
                            UserData.myTrackers.put(phoneNumber, name)
//                            listOfContact.add(UserContact(name, phoneNumber))
//                            adapter!! .notifyDataSetChanged()
                            refreshData()

                            userData.saveContactInfo()

                            val mDatabase = FirebaseDatabase.getInstance().reference
                            mDatabase.child("Users").child(phoneNumber).child("Finders").child(userData.loadPhoneNumber()).setValue(true)
                        }
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}

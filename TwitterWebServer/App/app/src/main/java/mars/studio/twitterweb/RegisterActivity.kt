package mars.studio.twitterweb

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        singInAnonymously()

        iv_user.setOnClickListener {
            checkPermission()
        }
    }

    fun buRegisterEvent(view: View) {
        saveImageInFirebase()
        bu_register.isEnabled = false
    }

    fun singInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, {
            task ->
            Log.e("myLog", " task " + task.isSuccessful.toString())
        })
    }

    val READ_IMAGE = 253
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),READ_IMAGE)
                return

            }
        }
        loadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            READ_IMAGE->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(this, "Cannot access your image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    val PICK_IMAGE_CODE = 123
    private fun loadImage() {
        var intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColum = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage, filePathColum, null, null, null)
            cursor.moveToFirst()
            val colummIndex = cursor.getColumnIndex(filePathColum[0])
            val picturePath = cursor.getString(colummIndex)
            cursor.close()
            iv_user.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    fun saveImageInFirebase() {

        var currentUser = mAuth.currentUser

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://twitterweb-dbcb6.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObj = Date()
        val imagePath = splitString(currentUser?.email!!)+"." + df.format(dateObj)+".jpg"

        val imageRef = storageRef.child("images/" + imagePath)
        iv_user.isDrawingCacheEnabled = true
        iv_user.buildDrawingCache()

        val drawable = iv_user.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(this, "fail upload ", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            val downloadUrl = taskSnapshot.downloadUrl.toString()
//            myRef.child("Users").child(currentUser.uid).child("email").setValue(currentUser.email)
//            myRef.child("Users").child(currentUser.uid).child("ProfileImage").setValue(downloadUrl)
//            loadTweets()
            Log.e("myLog", " url " + downloadUrl)
            val url = "http://dec.local/register.php?first_name="+ed_name.text.toString()+
                    "&email="+ed_email.text.toString()+"&password="+ed_password.text.toString()+"&picture_path="+downloadUrl
            MyAsyncTask().execute(url)
        }
    }
    private fun splitString(str: String):String {
        var split = str.split("@")
        return split[0]
    }

    // http
    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String {
            try {
                Log.e("myLog", " doInBackground ")
                var url = URL(params[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                Log.e("myLog", " urlConnect " + urlConnect.responseMessage)
                var inString = convertStremToString(urlConnect.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {
                Log.e("myLog", " Exception ")
                ex.printStackTrace()
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val json = JSONObject(values[0])

                Toast.makeText(applicationContext, json.getString("msg"), Toast.LENGTH_SHORT).show()
                if (json.getString("msg") == "user is added") {
                    finish()
                } else {
                    bu_register.isEnabled = false
                }
            } catch (ex: Exception) {

            }
        }

    }
}

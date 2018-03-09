package kz.mars.studio.twitterdemo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val dataBase = FirebaseDatabase.getInstance()
    private var myRef = dataBase.reference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        ivImagePerson.setOnClickListener {
            checkPermission()
        }


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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
            ivImagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    private fun loginToFirebase(email: String, password: String) {

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "isSuccessful", Toast.LENGTH_SHORT).show()
                            val currentUser: FirebaseUser? = mAuth!!.currentUser!!
                            saveImageInFirebase(currentUser)
                        } else {
                            Toast.makeText(this@LoginActivity, "fail loginToFirebase ", Toast.LENGTH_SHORT).show()
                            singIn(email, password)
                        }
                    }
                })

    }

    private fun singIn(email: String, password: String){
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
            override fun onComplete(p0: Task<AuthResult>) {
                if (p0.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "isSuccessful", Toast.LENGTH_SHORT).show()
                    val currentUser: FirebaseUser? = mAuth!!.currentUser!!
                    if (currentUser != null) {
//                        myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                    }
//                    loadMain()
                } else {
                    Toast.makeText(this@LoginActivity, "fail singIn ", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun buLogin(view: View) {
        loginToFirebase(edEmail.text.toString(), edPassword.text.toString())
    }

    fun saveImageInFirebase(currentUser: FirebaseUser?) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://twitterdemo-e7270.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObj = Date()
        val imagePath = splitString(currentUser?.email!!)+"." + df.format(dateObj)+".jpg"

        val imageRef = storageRef.child("images/" + imagePath)
        ivImagePerson.isDrawingCacheEnabled = true
        ivImagePerson.buildDrawingCache()

        val drawable = ivImagePerson.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(this@LoginActivity, "fail upload ", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            val downloadUrl = taskSnapshot.downloadUrl.toString()
            myRef.child("Users").child(currentUser.uid).child("email").setValue(currentUser.email)
            myRef.child("Users").child(currentUser.uid).child("ProfileImage").setValue(downloadUrl)
            loadTweets()
        }
    }

    fun loadTweets() {
        val currentUser: FirebaseUser? = mAuth!!.currentUser!!
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
        }
    }

    private fun splitString(str: String):String {
        var split = str.split("@")
        return split[0]
    }
}

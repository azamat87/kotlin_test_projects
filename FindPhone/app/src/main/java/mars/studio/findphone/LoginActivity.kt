package mars.studio.findphone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        singInAnonymously()
    }

    fun singInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Auth Success", Toast.LENGTH_SHORT).show()

                    val user = mAuth.currentUser

                } else {
                    Toast.makeText(this@LoginActivity, "Auth fail", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun buRegisterEvent(view: View) {
        if (!edPhoneNumber.text.toString().isEmpty()) {

            val userData = UserData(this)
            userData.savePhone(edPhoneNumber.text.toString())

            val df = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
            val date = Date()
            val mData = FirebaseDatabase.getInstance().reference
            mData.child("Users").child(edPhoneNumber.text.toString()).child("request").setValue(df.format(date).toString())
            mData.child("Users").child(edPhoneNumber.text.toString()).child("Finders").setValue(df.format(date).toString())
            finish()
        } else {
            Toast.makeText(this, "Input phone number", Toast.LENGTH_SHORT).show()
        }
    }
}

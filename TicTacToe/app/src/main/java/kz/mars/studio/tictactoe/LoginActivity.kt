package kz.mars.studio.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()

    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun btnLoginEvent(view: View) {
        loginToFirebase(edEmail.text.toString(), edPassword.text.toString())
    }

    private fun loginToFirebase(email: String, password: String) {

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "isSuccessful", Toast.LENGTH_SHORT).show()
                            val currentUser: FirebaseUser? = mAuth!!.currentUser!!
                            if (currentUser != null) {
                                myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                            }

                            loadMain()
                        } else {
                            Toast.makeText(this@LoginActivity, "fail loginToFirebase ", Toast.LENGTH_SHORT).show()
                            singIn(email, password)
                        }
                    }
                })

    }

    private fun singIn(email: String, password: String){
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                if (p0.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "isSuccessful", Toast.LENGTH_SHORT).show()
                    val currentUser: FirebaseUser? = mAuth!!.currentUser!!
                    if (currentUser != null) {
                        myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                    }
                    loadMain()
                } else {
                    Toast.makeText(this@LoginActivity, "fail singIn ", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun loadMain() {
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

package mars.studio.findphone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }

    fun buRegisterEvent(view: View) {
        if (edPhoneNumber.text.toString().isEmpty()) {
            val userData = UserData(this)
            userData.savePhone(edPhoneNumber.text.toString())
            finish()
        } else {
            Toast.makeText(this, "Input phone number", Toast.LENGTH_SHORT).show()
        }
    }
}

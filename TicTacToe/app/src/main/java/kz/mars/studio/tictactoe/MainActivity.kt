package kz.mars.studio.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mBuRestart: Button
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    private var myEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mBuRestart = buRestart
        mBuRestart.setOnClickListener {
            restartGame()
        }

        var bundle = intent.extras
        myEmail = bundle.getString("email", "")
        incomingCalls()
    }

    fun buClick(view: View){
        val buSelected: Button = view as Button
        var cellID = 0
        when (buSelected.id) {
            R.id.button1 -> cellID = 1
            R.id.button2 -> cellID = 2
            R.id.button3 -> cellID = 3
            R.id.button4 -> cellID = 4
            R.id.button5 -> cellID = 5
            R.id.button6 -> cellID = 6
            R.id.button7 -> cellID = 7
            R.id.button8 -> cellID = 8
            R.id.button9 -> cellID = 9
        }
//        mPlayGame(cellID, buSelected)

        myRef.child("PlayerOnline").child(sessionID).child(cellID.toString()).setValue(myEmail)
    }

    private fun restartGame() {
        mBuRestart.visibility = View.INVISIBLE
        player1.clear()
        player2.clear()
        recreate()
    }

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    var activePlayer = 1

    private fun mPlayGame(cellID: Int, buSelected: Button) {

        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundResource(R.color.blue)
            player1.add(cellID)
            activePlayer = 2

        } else {
            buSelected.text = "O"
            buSelected.setBackgroundResource(R.color.dark_green)
            player2.add(cellID)
            activePlayer = 1
        }
        buSelected.isEnabled = false
        checkWinner()
    }

    private fun checkWinner() {
        var winner = -1
        // row 1
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }

        // row 2
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }
        // row 3
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }

        // col 3
        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }

        // col 3
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }

        // col 3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }

        //
        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2
        }

        //
        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2
        }

        if (winner != -1) {
            if (winner == 1) {
                Toast.makeText(this, " Player 1 win the game ", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, " Player 2 win the game ", Toast.LENGTH_SHORT).show()
            }
            mBuRestart.visibility = View.VISIBLE
        }
    }

    private fun autoPlay(cellID: Int) {

            var buSelecte: Button?
            when (cellID) {
                1 -> buSelecte = button1
                2 -> buSelecte = button2
                3 -> buSelecte = button3
                4 -> buSelecte = button4
                5 -> buSelecte = button5
                6 -> buSelecte = button6
                7 -> buSelecte = button7
                8 -> buSelecte = button8
                9 -> buSelecte = button9
                else -> buSelecte = button1
            }
            mPlayGame(cellID, buSelecte)

    }

    fun btnRequest(view: View) {
        var userEmail = ed_player_email.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitString(myEmail!!)+splitString(userEmail))
    }

    fun btnAccept(view: View) {
        var userEmail = ed_player_email.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitString(userEmail)+splitString(myEmail!!))
    }

    var sessionID: String? = null
    var playerSymbol: String? = null

    fun playerOnline(sessionID: String) {
        this.sessionID = sessionID
        myRef.child("PlayerOnline").removeValue()
        myRef.child("PlayerOnline").child(sessionID)
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                    }
                    override fun onDataChange(p0: DataSnapshot?) {
                        try {
                            player1.clear()
                            player2.clear()

                            val td:HashMap<String, Any> ? = p0!!.value as HashMap<String, Any>
                            if (td != null) {
                                var value: String
                                for (key in td.keys) {
                                    value = td[key] as String
                                    if (value != myEmail) {
                                        activePlayer = if (playerSymbol === "X") 1 else 2
                                    } else {
                                        activePlayer = if (playerSymbol === "X") 2 else 1
                                    }
                                    autoPlay(key.toInt())
                                }
                            }

                        } catch (ex: Exception) {
                        }
                    }
                })
    }

    var number = 0
    fun incomingCalls() {
        myRef.child("Users").child(splitString(myEmail.toString())).child("Request")
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        try {
                            val td:HashMap<String, Any> ? = p0!!.value as HashMap<String, Any>
                            if (td != null) {
                                var value: String

                                for (key in td.keys) {
                                    value = td[key] as String
                                    ed_player_email.setText(value)

                                    val notifyMe = Notifications()
                                    notifyMe.Notify(applicationContext, value + " player want to play", number)
                                    number++

                                    myRef.child("Users").child(splitString(myEmail!!)).child("Request").setValue(true)

                                    break
                                }
                            }

                        } catch (ex: Exception) {

                        }
                    }

                })
    }

    private fun splitString(str: String):String {
        var split = str.split("@")
        return split[0]
    }
}

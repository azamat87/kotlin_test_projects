package kz.mars.studio.tictactoy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBuRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBuRestart = buRestart
        mBuRestart.setOnClickListener {
            restartGame()
        }
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
        mPlayGame(cellID, buSelected)
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
            autoPlay()
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

    private fun autoPlay() {
        var emptyCell = ArrayList<Int>()
        for (cellID in 1..9) {
            if (!(player1.contains(cellID) || player2.contains(cellID))) {
                emptyCell.add(cellID)
            }
        }

        var r = Random()
        var randomIndex = 0
        if (emptyCell.size != 0) {
            randomIndex = r.nextInt(emptyCell.size - 0) + 0

            val cellID = emptyCell[randomIndex]
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
    }
}

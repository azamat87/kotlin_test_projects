package kz.mars.studio.calculator.pattern

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun buNumberEvent(view: View) {
        if (isNewOp) {
            editShowNumbers.setText("")
        }
        isNewOp = false
        val buSelect = view as Button
        var buClickValue = editShowNumbers.text.toString()
        when (buSelect.id) {
            bu0.id -> { buClickValue += "0" }
            bu1.id -> { buClickValue += "1" }
            bu2.id -> { buClickValue += "3" }
            bu4.id -> { buClickValue += "4" }
            bu5.id -> { buClickValue += "5" }
            bu6.id -> { buClickValue += "6" }
            bu7.id -> { buClickValue += "7" }
            bu8.id -> { buClickValue += "8" }
            bu9.id -> { buClickValue += "9" }
            buDot.id -> { buClickValue += "." }
            buPlusMinus.id -> { buClickValue = "-" + buClickValue }
        }

        editShowNumbers.setText(buClickValue)
    }


    var op = "*"
    var oldNumbers = ""
    var isNewOp = true
    fun buOperationEvent(view: View) {
        val buSelect = view as Button

        when (buSelect.id) {
            buMult.id -> {
                op = "*"
            }
            buDiv.id -> {
                op = "/"
            }
            buSub.id -> {
                op = "-"
            }
            buSum.id -> {
                op = "+"
            }
        }
        oldNumbers = editShowNumbers.text.toString()
        isNewOp = true
    }

    fun buEqualEvent(view: View) {
        val newNumber = editShowNumbers.text.toString()
        var finalNumber: Double ?= null
        when (op) {
            "*" -> {
                finalNumber = oldNumbers.toDouble() * newNumber.toDouble()
            }
            "/" -> {
                finalNumber = oldNumbers.toDouble() / newNumber.toDouble()
            }
            "+" -> {
                finalNumber = oldNumbers.toDouble() + newNumber.toDouble()
            }
            "-" -> {
                finalNumber = oldNumbers.toDouble() - newNumber.toDouble()
            }
        }
        editShowNumbers.setText(finalNumber.toString())
        isNewOp = true
    }

    fun buPercent(view: View) {
        val number = editShowNumbers.text.toString().toDouble() / 100
        editShowNumbers.setText(number.toString())
        isNewOp = true
    }

    fun buClean(view: View) {
        editShowNumbers.setText("0")
        isNewOp = true
    }
}

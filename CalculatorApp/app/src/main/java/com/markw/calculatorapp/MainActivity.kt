package com.markw.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_WAS_STORED = "Operand1wasStored"

class MainActivity : AppCompatActivity() {

    // variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        operationTextView.text = ""

        val operandButtonListener = View.OnClickListener { v ->
            val button = v as Button
            newNumberText.append(button.text)
        }

        // set the listener on each button
        button0.setOnClickListener(operandButtonListener)
        button1.setOnClickListener(operandButtonListener)
        button2.setOnClickListener(operandButtonListener)
        button3.setOnClickListener(operandButtonListener)
        button4.setOnClickListener(operandButtonListener)
        button5.setOnClickListener(operandButtonListener)
        button6.setOnClickListener(operandButtonListener)
        button7.setOnClickListener(operandButtonListener)
        button8.setOnClickListener(operandButtonListener)
        button9.setOnClickListener(operandButtonListener)
        buttonDecimal.setOnClickListener(operandButtonListener)

        val operationButtonListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumberText.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumberText.setText("")
            }
            pendingOperation = op
            operationTextView.text = pendingOperation
        }

        buttonEquals.setOnClickListener(operationButtonListener)
        buttonDivide.setOnClickListener(operationButtonListener)
        buttonMultiply.setOnClickListener(operationButtonListener)
        buttonMinus.setOnClickListener(operationButtonListener)
        buttonAddition.setOnClickListener(operationButtonListener)

        buttonNeg.setOnClickListener({ view ->
            val value = newNumberText.text.toString()
            if (value.isEmpty()) {
                newNumberText.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumberText.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    // can't set a negative value on nothing on "-" or "."
                    newNumberText.setText("")
                }
            }
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "")
        if (savedInstanceState.getBoolean(STATE_OPERAND1_WAS_STORED, false)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            operand1 = null
        }
        operationTextView.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_WAS_STORED, true)
        } else {
            outState.putBoolean(STATE_OPERAND1_WAS_STORED, false)
        }
    }

    private fun performOperation(value: Double, operation: String) {

        if (operand1 == null) {
            operand1 = value.toDouble()
        } else {
            var operand2 = value.toDouble()

            // perform the operation on the 2 operands
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            // similar to a switch
            when (pendingOperation) {
                "=" -> operand1 = operand2
                "/" -> operand1 = if (operand2 == 0.0) {
                                    Double.NaN // divide by zero
                                } else {
                                    operand1!! / operand2
                                }
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
            }
        }

        resultTextView.setText(operand1.toString())
        newNumberText.setText("")  // clear it out
    }
}
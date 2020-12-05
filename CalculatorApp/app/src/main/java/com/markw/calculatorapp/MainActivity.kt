package com.markw.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_WAS_STORED = "Operand1wasStored"

class MainActivity : AppCompatActivity() {

    // allows later initialization and preventing use of a nullable (optional)
    private lateinit var resultText: EditText
    private lateinit var newNumberText: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operationTextView) }

    // variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.resultTextView)
        newNumberText = findViewById(R.id.newNumberText)
        displayOperation.text = ""

        // Data input (operand) buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDecimal: Button = findViewById(R.id.buttonDecimal)

        // Operation buttons
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonAdd = findViewById<Button>(R.id.buttonAddition)
        val buttonSubtract = findViewById<Button>(R.id.buttonMinus)

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
            displayOperation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(operationButtonListener)
        buttonDivide.setOnClickListener(operationButtonListener)
        buttonMultiply.setOnClickListener(operationButtonListener)
        buttonSubtract.setOnClickListener(operationButtonListener)
        buttonAdd.setOnClickListener(operationButtonListener)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "")
        if (savedInstanceState.getBoolean(STATE_OPERAND1_WAS_STORED, false)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            operand1 = null
        }
        displayOperation.text = pendingOperation
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

        resultText.setText(operand1.toString())
        newNumberText.setText("")  // clear it out
    }
}
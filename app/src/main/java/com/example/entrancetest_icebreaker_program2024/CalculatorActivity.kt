package com.example.entrancetest_icebreaker_program2024

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private var currentNumber = 0.0
    private var previousNumber = 0.0
    private var currentOperator = Operator.NONE

    enum class Operator {
        NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)

        tvResult = findViewById(R.id.tvResult)

        // Lắng nghe sự kiện khi nhấn nút số
        val numberButtons = listOf<Button>(
            findViewById(R.id.btn_0), findViewById(R.id.btn_1), findViewById(R.id.btn_2),
            findViewById(R.id.btn_3), findViewById(R.id.btn_4), findViewById(R.id.btn_5),
            findViewById(R.id.btn_6), findViewById(R.id.btn_7), findViewById(R.id.btn_8),
            findViewById(R.id.btn_9)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }

        // Lắng nghe sự kiện khi nhấn nút phép toán
        val operatorButtons = listOf<Button>(
            findViewById(R.id.btn_tong), findViewById(R.id.btn_hieu),
            findViewById(R.id.btn_tich), findViewById(R.id.btn_thuong)
        )

        for (button in operatorButtons) {
            button.setOnClickListener {
                applyOperator(button.text.toString())
            }
        }

        // Xử lý sự kiện khi nhấn nút '='
        val equalButton = findViewById<Button>(R.id.btn_bang)
        equalButton.setOnClickListener {
            calculateResult()
        }

        // Xử lý sự kiện khi nhấn nút 'C' để xóa
        val clearButton = findViewById<Button>(R.id.btn_Clear)
        clearButton.setOnClickListener {
            clear()
        }
    }

    // Hàm thêm số vào màn hình
    private fun appendNumber(number: String) {
        val currentText = tvResult.text.toString()
        if (currentText == "0") {
            tvResult.text = number
        } else {
            tvResult.append(number)
        }
    }

    // Hàm áp dụng phép toán
    private fun applyOperator(operator: String) {
        if (tvResult.text.isNotEmpty()) {
            currentNumber = tvResult.text.toString().toDouble()
            previousNumber = currentNumber
            tvResult.text = ""
            when (operator) {
                "+" -> currentOperator = Operator.ADD
                "-" -> currentOperator = Operator.SUBTRACT
                "*" -> currentOperator = Operator.MULTIPLY
                "/" -> currentOperator = Operator.DIVIDE
            }
        }
    }

    // Hàm tính kết quả
    private fun calculateResult() {
        if (tvResult.text.isNotEmpty()) {
            val newNumber = tvResult.text.toString().toDouble()
            when (currentOperator) {
                Operator.ADD -> currentNumber += newNumber
                Operator.SUBTRACT -> currentNumber -= newNumber
                Operator.MULTIPLY -> currentNumber *= newNumber
                Operator.DIVIDE -> {
                    if (newNumber != 0.0) {
                        currentNumber /= newNumber
                    } else {
                        tvResult.text = "Error"
                        return
                    }
                }
                else -> return
            }
            tvResult.text = currentNumber.toString()
        }
    }

    // Hàm xóa màn hình
    private fun clear() {
        tvResult.text = "0"
        currentNumber = 0.0
        previousNumber = 0.0
        currentOperator = Operator.NONE
    }
}
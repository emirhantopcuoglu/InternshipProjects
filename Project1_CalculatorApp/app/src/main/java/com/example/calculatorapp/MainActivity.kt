package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder
import java.lang.UnsupportedOperationException
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private lateinit var displayResult : TextView
    private lateinit var historyResult: TextView
    val historyList = mutableListOf<String>() //Tum islemleri saklayan liste
    val handler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayResult = findViewById(R.id.displayResult)
        historyResult = findViewById(R.id.historyResult)

        //Buton tanimlamalari ve tiklama olaylarinin eklenmesi
        val buttonClear: Button = findViewById(R.id.clear)
        buttonClear.setOnClickListener {
            displayResult.text = ""
            historyResult.text = ""
            historyResult.visibility = View.GONE
        }

        val buttonDelete: Button = findViewById(R.id.delete)
        buttonDelete.setOnClickListener {
            val currentText = displayResult.text.toString()
            if (currentText.isNotEmpty()) {
                displayResult.text = currentText.dropLast(1)
            }
        }

        val buttonPercentage: Button = findViewById(R.id.percentage)
        buttonPercentage.setOnClickListener {
            displayResult.append("%")
        }

        val buttonDivison: Button = findViewById(R.id.division)
        buttonDivison.setOnClickListener {
            displayResult.append("/")
        }

        val button7: Button = findViewById(R.id.button7)
        button7.setOnClickListener {
            displayResult.append("7")
        }

        val button8: Button = findViewById(R.id.button8)
        button8.setOnClickListener {
            displayResult.append("8")
        }

        val button9: Button = findViewById(R.id.button9)
        button9.setOnClickListener {
            displayResult.append("9")
        }

        val buttonMultiply: Button = findViewById(R.id.multiply)
        buttonMultiply.setOnClickListener {
            displayResult.append("×")
        }

        val button4: Button = findViewById(R.id.button4)
        button4.setOnClickListener {
            displayResult.append("4")
        }

        val button5: Button = findViewById(R.id.button5)
        button5.setOnClickListener {
            displayResult.append("5")
        }

        val button6: Button = findViewById(R.id.button6)
        button6.setOnClickListener {
            displayResult.append("6")
        }

        val buttonSubtraction: Button = findViewById(R.id.subtraction)
        buttonSubtraction.setOnClickListener {
            displayResult.append("-")
        }

        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            displayResult.append("1")
        }
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener {
            displayResult.append("2")
        }

        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener {
            displayResult.append("3")
        }

        val buttonAddition: Button = findViewById(R.id.addition)
        buttonAddition.setOnClickListener {
            displayResult.append("+")
        }

        val button0: Button = findViewById(R.id.button0)
        button0.setOnClickListener {
            displayResult.append("0")
        }

        val buttonDecimal: Button = findViewById(R.id.decimal)
        buttonDecimal.setOnClickListener {
            displayResult.append(",")
        }

        val buttonOpeningParanthesis: Button = findViewById(R.id.openingParanthesis)
        buttonOpeningParanthesis.setOnClickListener {
            displayResult.append("(")
        }

        val buttonClosingParanthesis: Button = findViewById(R.id.closingParanthesis)
        buttonClosingParanthesis.setOnClickListener {
            displayResult.append(")")
        }

        val buttonEquals: Button = findViewById(R.id.equals)
        buttonEquals.setOnClickListener {
            displayResult.append("=")
            val expression = displayResult.text.toString()
            val result = evaluateExpression(expression)
            displayResult.text = result.toString()
            //historyResult.text = expression
            //historyResult.visibility = View.VISIBLE
        }

        val buttonHistory: Button = findViewById(R.id.buttonHistory)
        buttonHistory.setOnClickListener {
            // Geçmiş islemleri historyResult TextView'ında gostermek
            historyResult.text = historyList.joinToString("\n")
            historyResult.visibility = View.VISIBLE  // TextView'i gorunur hale getir

            // 10 saniye sonra TextView'i gizle
            handler.postDelayed({
                historyResult.visibility = View.GONE
            }, 10000)  // 10,000 ms = 10 saniye
        }
    }

    fun evaluateExpression(expression: String): Double {
        val tokens = expression.toCharArray() //Girdi parametresi --> CharArray
        val values: Stack<Double> = Stack()
        val operators: Stack<Char> = Stack() //Sayilar ve operatorler stackte saklanir

        //Bosluklar atlanir
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == ' ') {
                i++
                continue
            }
        // Sayi ve ondaliklari StringBuilder kullanarak bir araya getirip Double'a çevrilir ve values stack'ine eklenir
            if (tokens[i] in '0'..'9' || tokens[i] == '.') {
                val sbuf = StringBuilder()
                while (i < tokens.size && (tokens[i] in '0'..'9' || tokens[i] == '.')) {
                    sbuf.append(tokens[i++])
                }
                values.push(sbuf.toString().toDouble())
                i--
            } else if (tokens[i] == '(') {  // Parantezler ve operatörler operators stack'ine eklenir
                operators.push(tokens[i])
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                }
                operators.pop()
            } else if (tokens[i] in arrayOf('+', '-', '×', '/', '%')) {
                while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                }
                operators.push(tokens[i])
            }
            i++
        }

        while (!operators.empty()) {
            values.push(applyOp(operators.pop(), values.pop(), values.pop()))
        }
        val result = values.pop()
        historyList.add("$expression $result") //Her islem sonucunda historyList'e islem ve sonucunu ekler

        return result
    }
    //Islem onceligi yapan fonksiyon
    fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')'){
            return false
        }
        if ((op1 == '×' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')){
            return false
        }
        return true
    }

    //Islemleri gerceklestiren fonksiyon
    fun applyOp(op: Char, b: Double, a: Double): Double{
        return when (op){
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            '/' -> {
                if (b == 0.0) throw UnsupportedOperationException("Hatalı işlem")
                a / b
            }
            '%' -> a * (b / 100)
            else -> 0.0
        }
    }
}
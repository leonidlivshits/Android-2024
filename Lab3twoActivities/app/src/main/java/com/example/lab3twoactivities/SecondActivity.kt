package com.example.lab3twoactivities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val receivedText: TextView = findViewById(R.id.textViewReceivedText)
        val returnButton: Button = findViewById(R.id.buttonReturn)

        val userInput = intent.getStringExtra("enteredText") ?: "Нет данных"

        Log.d("SecondActivity", "Received user input: $userInput")

        receivedText.text = "Получено: $userInput"

        returnButton.setOnClickListener {
            finish()
        }
    }
}

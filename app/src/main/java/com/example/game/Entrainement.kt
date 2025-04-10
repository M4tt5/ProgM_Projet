package com.example.game

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class Entrainement : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entrainement)
        val button1 = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)

        button1.setOnClickListener {
            // Action pour jeu 1
        }

        button2.setOnClickListener {
            // Action pour jeu 2
        }
        button3.setOnClickListener {

        }
        button4.setOnClickListener{

        }
        button5.setOnClickListener{

        }
        button6.setOnClickListener {

        }
    }
}
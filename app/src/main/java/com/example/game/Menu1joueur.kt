package com.example.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class Menu1joueur : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu1joueur)
        val button1 = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button1.setOnClickListener {
            // Action pour 1 joueur
        }

        button2.setOnClickListener {
            val intent = Intent(this, Entrainement::class.java)
            startActivity(intent)
        }
    }
}
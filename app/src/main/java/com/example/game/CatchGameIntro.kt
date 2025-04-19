package com.example.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class CatchGameIntro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.catchgameintro)

        val btnStart = findViewById<Button>(R.id.btnStartGame)
        btnStart.setOnClickListener {
            val intent = Intent(this, CatchGame::class.java)
            startActivity(intent)
        }
    }
}
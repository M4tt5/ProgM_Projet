package com.example.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.game.challenges.balance.BalanceActivity
import com.example.game.challenges.shake.ShakeActivity

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
            val intent = Intent(this, TargetJeu::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, CatchGame::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            try {
                val intent = Intent(this, BalanceActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Erreur: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                Log.e("MENU", "Erreur de lancement", e)
            }
        }
        button4.setOnClickListener {
            val intent = Intent(this, ShakeActivity::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener{
            val intent = Intent(this, Quiz1Game::class.java)
            startActivity(intent)
        }
        button6.setOnClickListener {

        }
    }
}
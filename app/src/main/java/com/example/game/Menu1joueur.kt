package com.example.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.game.challenges.balance.BalanceActivity;

class Menu1joueur : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu1joueur)
        val button1 = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button1.setOnClickListener {
            // Action pour 1 joueur
        }

        // Nouveau bouton
        findViewById<Button>(R.id.btn_balance).setOnClickListener {
            try {
                val intent = Intent(this, BalanceActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Erreur: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                Log.e("MENU", "Erreur de lancement", e)
            }
        }

        button2.setOnClickListener {
            val intent = Intent(this, Entrainement::class.java)
            startActivity(intent)
        }


    }
}
package com.example.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.game.presentation.BluetoothMainActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        val button1 = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button1.setOnClickListener {
            val intent = Intent(this, Menu1joueur::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, BluetoothMainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}

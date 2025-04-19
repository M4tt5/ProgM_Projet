package com.example.game

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class PartieRapideResult : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partie_rapide_result)

        val scores = intent.getIntegerArrayListExtra("scores") ?: listOf()
        val gameNames = intent.getStringArrayListExtra("gameNames") ?: listOf()

        val resultText = findViewById<TextView>(R.id.resultText)
        val resultBuilder = StringBuilder()
        for (i in scores.indices) {
            resultBuilder.append("Jeu ${gameNames[i]} : ${scores[i]} points\n")
        }
        resultText.text = resultBuilder.toString()

        // Jouer une musique
        mediaPlayer = MediaPlayer.create(this, R.raw.win1)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
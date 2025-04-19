package com.example.game

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class CatchGame : ComponentActivity(), CatchGameView.OnCatchGameFinishedListener {

    private var isQuickPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isQuickPlay = intent.getBooleanExtra("quickPlay", false)
        showIntroScreen()
    }

    private fun showIntroScreen() {
        setContentView(R.layout.catchgame)

        val startButton = findViewById<Button>(R.id.btnStartGame)
        val retourButton = findViewById<Button>(R.id.btnRetour)

        startButton.setOnClickListener {
            startGame()
        }

        retourButton.setOnClickListener {
            finish()
        }
    }

    private fun startGame() {
        if (isQuickPlay) {
            setContentView(CatchGameView(this, null, true, this))
        } else {
            setContentView(CatchGameView(this))
        }
    }

    override fun onGameFinished(score: Int) {
        val resultIntent = intent
        resultIntent.putExtra("score", score)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
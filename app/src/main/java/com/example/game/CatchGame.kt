package com.example.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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

        if (isQuickPlay) {
            retourButton.visibility = View.GONE
        }

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
        Toast.makeText(this@CatchGame, "Score final : $score", Toast.LENGTH_LONG).show()
        val resultIntent = intent
        resultIntent.putExtra("score", score)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
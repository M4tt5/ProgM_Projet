package com.example.game

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class TapGame : ComponentActivity() {

    private var score = 0
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private lateinit var tapButton: ImageButton
    private lateinit var timer: CountDownTimer
    private var isQuickPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isQuickPlay = intent.getBooleanExtra("quickPlay", false)
        setContentView(R.layout.tapgme)

        scoreText = findViewById(R.id.scoreText)
        timerText = findViewById(R.id.timerText)
        tapButton = findViewById(R.id.tapButton)

        tapButton.setOnClickListener {
            score++
            scoreText.text = "Score : $score"
        }

        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            startButton.visibility = View.GONE
            timerText.visibility = View.VISIBLE
            scoreText.visibility = View.VISIBLE
            tapButton.visibility = View.VISIBLE
            startGame()
        }


    }

    private fun startGame() {
        score = 0

        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Temps : ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                if (isQuickPlay) {
                    Toast.makeText(this@TapGame, "Score final : $score", Toast.LENGTH_LONG).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra("score", score)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this@TapGame, "Score final : $score", Toast.LENGTH_LONG).show()
                    // On revient à l'activité Entrainement
                    val intent = Intent(this@TapGame, Entrainement::class.java)
                    this@TapGame.startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
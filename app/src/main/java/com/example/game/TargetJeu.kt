package com.example.game

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Random


class TargetJeu : ComponentActivity() {
    private var score = 0
    private var timer: CountDownTimer? = null
    private val random = Random()


    private lateinit var gameLayout: RelativeLayout
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private var isQuickPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isQuickPlay = intent.getBooleanExtra("quickPlay", false)
        showStartScreen()

    }

    private fun showStartScreen() {
        setContentView(R.layout.target_menu)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            showGameScreen()
        }
    }

    private fun showGameScreen() {
        setContentView(R.layout.target)

        gameLayout = findViewById(R.id.gameLayout)
        scoreText = findViewById(R.id.scoreText)
        timerText = findViewById(R.id.timerText)

        startGame()
    }

    private fun startGame() {
        score = 0
        updateScore()
        startTimer()
        spawnTarget()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                timerText.text = "Terminé !"
                gameLayout.removeAllViews()

                if (isQuickPlay) {
                    Toast.makeText(this@TargetJeu, "Score final : $score", Toast.LENGTH_LONG).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra("score", score)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this@TargetJeu, "Score final : $score", Toast.LENGTH_LONG).show()
                    // On revient à l'activité Entrainement
                    val intent = Intent(this@TargetJeu, Entrainement::class.java)
                    this@TargetJeu.startActivity(intent)
                }
            }
        }.start()
    }

    private fun updateScore() {
        scoreText.text = "Score : $score"
    }

    private fun spawnTarget() {
        if (timerText.text == "Terminé !") return

        val bottle = ImageView(this)
        bottle.setImageResource(R.drawable.jager)

        val size = 150
        val params = RelativeLayout.LayoutParams(size, size)

        val maxX = gameLayout.width - size
        val maxY = gameLayout.height - size
        if (maxX <= 0 || maxY <= 0) {
            gameLayout.postDelayed({ spawnTarget() }, 100)
            return
        }

        params.leftMargin = random.nextInt(maxX)
        params.topMargin = random.nextInt(maxY)
        bottle.layoutParams = params

        bottle.setOnClickListener {
            score++
            updateScore()
            gameLayout.removeView(bottle)
            spawnTarget()
        }

        gameLayout.addView(bottle)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}

package com.example.game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlin.random.Random

class ReflexGame: ComponentActivity() {

    private lateinit var bottleButton: ImageButton
    private lateinit var infoText: TextView
    private var hasStarted = false
    private var canTap = false
    private var startTime: Long = 0L
    private var score = 0
    private var isQuickPlay = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reflexgame)

        isQuickPlay = intent.getBooleanExtra("quickPlay", false)

        bottleButton = findViewById(R.id.bottleButton)
        infoText = findViewById(R.id.infoText)

        bottleButton.visibility = View.INVISIBLE

        bottleButton.setOnClickListener {
            if (!hasStarted) return@setOnClickListener

            if (!canTap) {
                infoText.text = "Trop tôt !"
                endGame(0)
            } else {
                val reactionTime = System.currentTimeMillis() - startTime
                score = (1000 - reactionTime).coerceAtLeast(0).toInt()
                infoText.text = "Temps : ${reactionTime}ms"
                endGame(score)
            }
        }
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            startCountdown()
        }
    }

    private fun startCountdown() {
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.visibility = View.GONE
        hasStarted = true
        infoText.text = "Attends l'apparition..."

        val delay = Random.nextLong(1500L, 4000L)
        handler.postDelayed({
            canTap = true
            startTime = System.currentTimeMillis()
            bottleButton.visibility = View.VISIBLE
            infoText.text = "MAINTENANT !"
        }, delay)
    }

    private fun endGame(score: Int) {
        canTap = false
        hasStarted = false

        handler.postDelayed({
            if (isQuickPlay) {
                Toast.makeText(this, "Score final : $score", Toast.LENGTH_LONG).show()
                val resultIntent = Intent()
                resultIntent.putExtra("score", score)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Score final : $score", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Entrainement::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)
    }
}
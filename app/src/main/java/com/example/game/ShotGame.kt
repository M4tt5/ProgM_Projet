package com.example.game

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlin.math.abs

class ShotGame : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var progressBar: ProgressBar
    private lateinit var quantityText: TextView
    private lateinit var targetText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private var currentQuantity = 0f
    private var targetQuantity = 0f  // Quantité cible aléatoire
    private var isGameActive = false
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shotgame)

        progressBar = findViewById(R.id.progressBar)
        quantityText = findViewById(R.id.quantityText)
        targetText = findViewById(R.id.targetText)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        // Initialiser le texte des objectifs
        targetText.text = "Objectif : $targetQuantity cL"

        // Configurer le bouton pour commencer le jeu
        startButton.setOnClickListener {
            startGame()
        }

        // Configurer le bouton pour arrêter le jeu
        stopButton.setOnClickListener {
            stopGame()
        }

        // Initialiser le gestionnaire de capteurs
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    private fun startGame() {
        isGameActive = true
        currentQuantity = 0f
        progressBar.progress = 0
        quantityText.text = "Quantité : $currentQuantity cL"
        // Générer une quantité cible aléatoire entre 1 et 10 cL
        targetQuantity = (1..10).random().toFloat()
        targetText.text = "Cible : $targetQuantity cL"
        // Masquer le bouton "Démarrer"
        startButton.visibility = View.GONE
        // Démarrer un timer de 5 secondes
        startTimer()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)

    }

    private fun stopGame() {
        isGameActive = false
        sensorManager.unregisterListener(this)
        //Calcul du score entre la quantité versée et la quantité cible
        // Afficher un Toast avec la quantité finale versée et le score
        val score = calculateScore()
        Toast.makeText(this, "Score : $score / 100", Toast.LENGTH_LONG).show()
        // Réafficher le bouton "Démarrer" pour permettre de recommencer
        startButton.visibility = View.VISIBLE

        // Annuler le timer si le joueur arrête le jeu manuellement
        timer?.cancel()

        // On revient à l'activité Entrainement
        val intent = Intent(this@ShotGame, Entrainement::class.java)
        this@ShotGame.startActivity(intent)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Vous pouvez utiliser le temps restant pour afficher un compte à rebours si vous le souhaitez
            }

            override fun onFinish() {
                // Lorsque le timer arrive à zéro, arrêter le jeu automatiquement
                if (isGameActive) {
                    stopGame()
                }
            }
        }.start()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || !isGameActive) return

        // Récupérer l'inclinaison du téléphone sur l'axe X
        val x = event.values[0]  // Inclinaison gauche/droite (axe X)

        // Utiliser l'inclinaison sur l'axe X pour déterminer le sens du versage
        val pourAmount = abs(x) * 0.1f // Ajuster la quantité de liquide en fonction de l'inclinaison

        if (x > 0) {
            // Pencher à droite
            currentQuantity += pourAmount
        } else if (x < 0) {
            // Pencher à gauche
            currentQuantity -= pourAmount
        }

        // Limiter la quantité à un minimum de 0 (ne pas aller en dessous de 0)
        if (currentQuantity < 0) {
            currentQuantity = 0f
        }

        // Mettre à jour la ProgressBar et le texte
        progressBar.progress = (currentQuantity * 100 / targetQuantity).toInt()
        quantityText.text = "Quantité : %.1f cL".format(currentQuantity)

        // Vous pouvez aussi choisir d'afficher le score total, sans limiter l'objectif
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ne rien faire ici pour l'instant
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (isGameActive) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun calculateScore(): Int {
        val error = abs(currentQuantity - targetQuantity)
        val maxError = 10f // Valeur max d'erreur tolérée
        val normalizedScore = ((1 - (error / maxError)) * 100).coerceIn(0f, 100f)
        return normalizedScore.toInt()
    }
}

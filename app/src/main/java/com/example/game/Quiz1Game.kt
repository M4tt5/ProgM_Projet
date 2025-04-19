package com.example.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class Quiz1Game : ComponentActivity() {

    data class Question(
        val question: String,
        val options: List<String>,
        val correctIndex: Int
    )

    private val questions = listOf(
        Question("En quelle année le Jägermeister a-t-il été créé ?", listOf("1834", "1878", "1934", "1945"), 2),
        Question("Dans quel pays est né le Jägermeister ?", listOf("Autriche", "Suisse", "Allemagne", "Belgique"), 2),
        Question("Quel est l’ingrédient principal du Jägermeister ?", listOf("Raisins", "Mélange de 56 herbes", "Pommes", "Houblon"), 1),
        Question("Quel animal figure sur la bouteille de Jägermeister ?", listOf("Ours", "Cerf", "Loup", "Lion"), 1),
        Question("Que signifie le mot “Jägermeister” ?", listOf("Maître de la chasse", "Liqueur de chasse", "Maître chasseur", "Gardien des bois"), 2),
        Question("Quelle est la couleur emblématique de la bouteille de Jägermeister ?", listOf("Rouge", "Bleu", "Vert", "Noir"), 2),
        Question("Quel est le degré d’alcool du Jägermeister ?", listOf("38°", "35%", "35°", "48°"), 2),
        Question("Quelle est la particularité du Jägermeister en shot ?", listOf("Servi très froid", "Avec du citron", "Flambé", "Mélangé à du sucre"), 0)
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private var isQuickPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz1game)

        // Vérifie si c'est une partie rapide
        isQuickPlay = intent.getBooleanExtra("quickPlay", false)
        showAccueil()
    }

    private fun showAccueil() {
        val accueilText = findViewById<TextView>(R.id.accueil_text)
        val startButton = findViewById<Button>(R.id.start_button)

        accueilText.visibility = TextView.VISIBLE
        startButton.visibility = Button.VISIBLE

        findViewById<TextView>(R.id.question_text).visibility = TextView.GONE
        findViewById<Button>(R.id.answer1).visibility = Button.GONE
        findViewById<Button>(R.id.answer2).visibility = Button.GONE
        findViewById<Button>(R.id.answer3).visibility = Button.GONE
        findViewById<Button>(R.id.answer4).visibility = Button.GONE

        startButton.setOnClickListener {
            accueilText.visibility = TextView.GONE
            startButton.visibility = Button.GONE
            showQuestion()
        }
    }

    private fun showQuestion() {
        val questionText = findViewById<TextView>(R.id.question_text)
        val button1 = findViewById<Button>(R.id.answer1)
        val button2 = findViewById<Button>(R.id.answer2)
        val button3 = findViewById<Button>(R.id.answer3)
        val button4 = findViewById<Button>(R.id.answer4)

        findViewById<TextView>(R.id.question_text).visibility = TextView.VISIBLE
        findViewById<Button>(R.id.answer1).visibility = Button.VISIBLE
        findViewById<Button>(R.id.answer2).visibility = Button.VISIBLE
        findViewById<Button>(R.id.answer3).visibility = Button.VISIBLE
        findViewById<Button>(R.id.answer4).visibility = Button.VISIBLE

        val current = questions[currentQuestionIndex]

        questionText.text = current.question
        val buttons = listOf(button1, button2, button3, button4)
        for (i in 0 until 4) {
            buttons[i].text = current.options[i]
            buttons[i].setOnClickListener {
                if (i == current.correctIndex) {
                    score++
                    Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Mauvaise réponse...", Toast.LENGTH_SHORT).show()
                }
                nextQuestion()
            }
        }
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestion()
        } else {
            showFinalScore()
        }
    }

    private fun showFinalScore() {
        val questionText = findViewById<TextView>(R.id.question_text)
        questionText.text = "Quiz terminé ! Score : $score / ${questions.size}"

        findViewById<Button>(R.id.answer1).visibility = Button.GONE
        findViewById<Button>(R.id.answer2).visibility = Button.GONE
        findViewById<Button>(R.id.answer3).visibility = Button.GONE
        findViewById<Button>(R.id.answer4).visibility = Button.GONE

        if (isQuickPlay) {
            val resultIntent = Intent()
            resultIntent.putExtra("score", score)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
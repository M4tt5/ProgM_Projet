package com.example.game

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.game.challenges.balance.BalanceActivity
import com.example.game.challenges.shake.ShakeActivity

class PartieRapide : ComponentActivity() {
    private val gameClasses = listOf(
        CatchGame::class.java,
        TargetJeu::class.java,
        BalanceActivity::class.java,
        ShakeActivity::class.java,
        ShotGame::class.java,
        Quiz1Game::class.java,
        TapGame::class.java,
        ReflexGame::class.java
    )

    private lateinit var selectedGames: List<Class<out ComponentActivity>>
    private var currentGameIndex = 0
    private val scores = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Choisir 3 jeux aléatoires différents
        selectedGames = gameClasses.shuffled().take(3)
        launchNextGame()
    }

    private fun launchNextGame() {
        if (currentGameIndex < selectedGames.size) {
            val intent = Intent(this, selectedGames[currentGameIndex])
            intent.putExtra("quickPlay", true)
            startActivityForResult(intent, 100)
        } else {
            val resultIntent = Intent(this, PartieRapideResult::class.java)
            resultIntent.putIntegerArrayListExtra("scores", ArrayList(scores))
            resultIntent.putStringArrayListExtra("gameNames", ArrayList(selectedGames.map { it.simpleName }))
            startActivity(resultIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val score = data?.getIntExtra("score", 0) ?: 0
            scores.add(score)
            currentGameIndex++
            launchNextGame()
        }
    }
}
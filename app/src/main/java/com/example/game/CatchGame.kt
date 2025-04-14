package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity

class CatchGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Utilisation directe de la vue personnalisée comme contenu de l'activité
        setContentView(CatchGameView(this))
    }
}
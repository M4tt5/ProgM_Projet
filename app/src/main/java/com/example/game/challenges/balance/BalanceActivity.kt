package com.example.game.challenges.balance

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.game.R
import com.example.game.ui.theme.GameTheme
import com.example.game.utils.SoundPlayer

class BalanceActivity : ComponentActivity() {
    private val viewModel: BalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quickPlay = intent.getBooleanExtra("quickPlay", false)
        setContent {
            GameTheme {
                BalanceChallengeScreen(viewModel = viewModel, quickPlay = quickPlay)
            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun BalanceChallengeScreen(viewModel: BalanceViewModel, quickPlay: Boolean) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Gestion du son de fin
    LaunchedEffect(state.gameEnded) {
        if (state.gameEnded) {
            val activity = context.findActivity()
            val resultIntent = Intent()
            if (!quickPlay) {
                val soundRes = if (state.hasWon) R.raw.win else R.raw.loose
                SoundPlayer.play(context, soundRes)
            }
            resultIntent.putExtra("score", state.currentScore)
            activity?.setResult(Activity.RESULT_OK, resultIntent)
            activity?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Défi d'Équilibre",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Affichage conditionnel du résultat
        if (state.gameEnded) {
            Text(
                text = if (state.hasWon) "Gagné ! (${state.currentScore}/${state.maxScore})"
                else "Perdu... (${state.currentScore}/${state.maxScore})",
                color = if (state.hasWon) Color(0xFF4CAF50) else Color(0xFFF44336),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))


        }

        // Zone de jeu
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Cercle de fond
                drawCircle(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    radius = size.minDimension / 2,
                    center = center
                )

                // Zone safe (verte transparente)
                drawCircle(
                    color = Color.Green.copy(alpha = 0.1f),
                    radius = 20f,
                    center = center
                )

                // Point mobile
                drawCircle(
                    color = state.dotColor,
                    radius = if (state.isInSafeZone) 25f else 15f,
                    center = center + state.dotPosition
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Informations de jeu
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Temps: ${state.timeRemaining.toInt()}s",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Score: ${state.currentScore}/${state.maxScore}",
                style = MaterialTheme.typography.headlineSmall,
                color = when {
                    state.currentScore >= 100 -> Color(0xFF4CAF50)
                    state.currentScore >= 50 -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }
            )
            Text(
                text = if (state.isInSafeZone) "Zone safe (+5pts/5s)" else "Hors zone (-25pts/s)",
                color = if (state.isInSafeZone) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!(quickPlay && state.isRunning)) {
            Button(
                onClick = {
                    if (state.isRunning) viewModel.endChallenge()
                    else viewModel.startChallenge(context)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isRunning) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
            ) {
                Text(if (state.isRunning) "Arrêter" else "Commencer")
            }
        }
    }
}
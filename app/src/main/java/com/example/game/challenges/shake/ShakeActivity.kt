package com.example.game.challenges.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.game.R


class ShakeActivity : ComponentActivity(), SensorEventListener {

    private val viewModel: ShakeViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quickPlay = intent.getBooleanExtra("quickPlay", false)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        setContent {
            val x by viewModel.x.collectAsState()
            val y by viewModel.y.collectAsState()
            val score by viewModel.score.collectAsState()
            val ended by viewModel.gameEnded.collectAsState()
            val running by viewModel.running.collectAsState()

            val config = LocalConfiguration.current
            val density = resources.displayMetrics.density
            val screenWidth = config.screenWidthDp * density
            val screenHeight = config.screenHeightDp * density

            LaunchedEffect(ended) {
                if (ended) {
                    if (!quickPlay) {
                        val res = if (score >= 100) R.raw.win else R.raw.loose
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer.create(this@ShakeActivity, res)
                        mediaPlayer?.start()
                    }

                    // Si on est en partie rapide, on renvoie juste le score
                    if (quickPlay) {
                        val resultIntent = intent
                        resultIntent.putExtra("score", score)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Score : $score", modifier = Modifier.padding(16.dp))

                Canvas(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()) {
                    drawCircle(Color.Red, 30f, Offset(x, y))
                }

                Button(
                    onClick = { viewModel.startGame(screenWidth, screenHeight) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = !running
                ) {
                    Text("Lancer le jeu")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
        val dx = -event.values[0] * 0.5f
        val dy = event.values[1] * 0.5f
        viewModel.boost(dx, dy)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

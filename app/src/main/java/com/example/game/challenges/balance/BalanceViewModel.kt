package com.example.game.challenges.balance

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.*

class BalanceViewModel : ViewModel(), SensorEventListener {

    // Configuration
    private val safeZoneRadius = 20f
    private val maxRadius = 150f
    private val amplificationFactor = 25f

    // Scores
    private val penaltyPerSecond = 25
    private val rewardPer5Seconds = 5
    private val winThreshold = 80
    private val maxScore = 150

    // État initial
    private val _state = MutableStateFlow(
        BalanceState(
            currentScore = maxScore,
            maxScore = maxScore,
            isRunning = false,
            gameEnded = false
        )
    )
    val state: StateFlow<BalanceState> = _state

    // Capteurs
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var vibrator: Vibrator? = null

    // Suivi du temps
    private var timeOutsideSafeZone = 0
    private var timeInsideSafeZone = 0

    fun startChallenge(context: Context) {
        // Réinitialisation complète
        timeOutsideSafeZone = 0
        timeInsideSafeZone = 0

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        _state.update {
            it.copy(
                isRunning = true,
                gameEnded = false,
                dotPosition = Offset.Zero,
                dotColor = Color.Green,
                currentScore = maxScore,
                timeRemaining = 30f,
                hasWon = false
            )
        }

        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        viewModelScope.launch {
            while (_state.value.timeRemaining > 0 && _state.value.isRunning) {
                delay(1000)

                // Gestion du temps
                if (_state.value.isInSafeZone) {
                    timeInsideSafeZone++
                    if (timeInsideSafeZone % 5 == 0) {
                        _state.update { state ->
                            state.copy(
                                currentScore = min(maxScore, state.currentScore + rewardPer5Seconds)
                            )
                        }
                    }
                } else {
                    timeOutsideSafeZone++
                    _state.update { state ->
                        state.copy(
                            currentScore = max(0, state.currentScore - penaltyPerSecond)
                        )
                    }
                }

                _state.update { it.copy(timeRemaining = it.timeRemaining - 1) }
            }
            endChallenge()
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - _state.value.lastUpdate) < 50) return

        val (x, y, _) = event.values.map { it * amplificationFactor }
        val newPosition = Offset(
            x.coerceIn(-maxRadius, maxRadius),
            y.coerceIn(-maxRadius, maxRadius)
        )
        val distance = sqrt(newPosition.x.pow(2) + newPosition.y.pow(2))
        val isInSafeZoneNow = distance <= safeZoneRadius

        // Vibrations uniquement en dehors de la zone safe
        if (!isInSafeZoneNow) {
            vibrator?.vibrate(VibrationEffect.createOneShot(15, 50))
        }

        _state.update {
            it.copy(
                dotPosition = newPosition,
                dotColor = if (isInSafeZoneNow) Color.Green else Color.Red,
                isInSafeZone = isInSafeZoneNow,
                lastUpdate = currentTime
            )
        }
    }

    fun endChallenge() {
        sensorManager?.unregisterListener(this)
        _state.update {
            it.copy(
                isRunning = false,
                gameEnded = true,
                hasWon = it.currentScore >= winThreshold
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

data class BalanceState(
    val isRunning: Boolean = false,
    val gameEnded: Boolean = false,
    val timeRemaining: Float = 30f,
    val dotPosition: Offset = Offset.Zero,
    val dotColor: Color = Color.Green,
    val isInSafeZone: Boolean = true,
    val currentScore: Int = 0,
    val maxScore: Int = 150,
    val lastUpdate: Long = 0,
    val hasWon: Boolean = false
)
package com.example.game.challenges.shake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max
import kotlin.math.min

class ShakeViewModel : ViewModel() {

    private val _x = MutableStateFlow(500f)
    private val _y = MutableStateFlow(800f)
    private val _score = MutableStateFlow(0)
    private val _gameEnded = MutableStateFlow(false)
    private val _running = MutableStateFlow(false)

    val x = _x.asStateFlow()
    val y = _y.asStateFlow()
    val score = _score.asStateFlow()
    val gameEnded = _gameEnded.asStateFlow()
    val running = _running.asStateFlow()

    private var vx = 5f
    private var vy = 7f
    private var width = 1000f
    private var height = 1800f

    fun startGame(screenWidth: Float, screenHeight: Float) {
        width = screenWidth
        height = screenHeight
        _x.value = width / 2
        _y.value = height / 2
        _score.value = 0
        vx = 5f
        vy = 7f
        _gameEnded.value = false
        _running.value = true

        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 15_000 && _running.value) {
                delay(16)
                updatePosition()
            }
            _running.value = false
            _gameEnded.value = true
        }
    }

    private fun updatePosition() {
        var newX = _x.value + vx
        var newY = _y.value + vy

        if (newX <= 0 || newX >= width) {
            vx *= -1
            _score.value += 1
        }
        if (newY <= 0 || newY >= height) {
            vy *= -1
            _score.value += 1
        }

        _x.value = min(max(newX, 0f), width)
        _y.value = min(max(newY, 0f), height)
    }

    fun boost(dx: Float, dy: Float) {
        if (_running.value) {
            vx += dx
            vy += dy
        }
    }
}

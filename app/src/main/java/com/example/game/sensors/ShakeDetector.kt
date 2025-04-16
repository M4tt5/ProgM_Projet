package com.example.game.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class ShakeDetector(private val onShakeListener: OnShakeListener) : SensorEventListener {
    interface OnShakeListener {
        fun onShake(count: Int)
    }

    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    private var shakeCount = 0
    private val shakeThreshold = 800 // Sensibilité des secousses

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastUpdate) > 100) {
            val diffTime = (currentTime - lastUpdate)
            lastUpdate = currentTime

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

            if (speed > shakeThreshold) {
                shakeCount++
                onShakeListener.onShake(shakeCount)
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }
}
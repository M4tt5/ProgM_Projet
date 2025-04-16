// app/src/main/java/com/example/game/utils/SoundPlayer.kt
package com.example.game.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

object SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, @RawRes soundRes: Int) {
        stop() // Arrête tout son précédent
        mediaPlayer = MediaPlayer.create(context, soundRes).apply {
            setVolume(1f, 1f)
            setOnCompletionListener { release() }
            start()
        }
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
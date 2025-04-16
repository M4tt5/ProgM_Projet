package com.example.game

import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class CatchGameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val bottleBitmap = BitmapFactory.decodeResource(resources, R.drawable.jager)
    private val caddieBitmap = BitmapFactory.decodeResource(resources, R.drawable.caddie)
    private val fauxBottleBitmap = BitmapFactory.decodeResource(resources, R.drawable.faux_jager)

    private lateinit var scaledBottle: Bitmap
    private lateinit var scaledCaddie: Bitmap
    private lateinit var scaledFauxBottle: Bitmap

    private var caddieX = 0f
    private var caddieY = 0f
    private var caddieWidth = 0
    private var caddieHeight = 0

    private val bottles = mutableListOf<Bottle>()
    private var score = 0

    private var timeLeft = 30 // en secondes
    private var isGameRunning = true
    private lateinit var timer: CountDownTimer

    private var spawnInterval = 1000L // temps initial entre deux bouteilles (1 sec)

    private val scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        typeface = Typeface.DEFAULT_BOLD
    }

    init {
        startGame()
    }

    private fun startGame() {
        isGameRunning = true
        score = 0
        timeLeft = 30

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()

                // Accélération progressive : toutes les 5 secondes, on diminue l'intervalle
                when (timeLeft) {
                    25 -> spawnInterval = 800
                    20 -> spawnInterval = 700
                    15 -> spawnInterval = 600
                    10 -> spawnInterval = 500
                    5  -> spawnInterval = 400
                }
            }

            override fun onFinish() {
                isGameRunning = false
            }
        }.start()

        postDelayed(::spawnBottle, 1000)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Taille fixe proportionnelle à la largeur de l'écran
        val bottleWidth = (w * 0.1).toInt()     // 10% de la largeur
        val bottleHeight = (h * 0.1).toInt()    // 10% de la hauteur
        val caddieWidthRatio = (w * 0.2).toInt() // 20% de la largeur
        val caddieHeightRatio = (h * 0.1).toInt() // 10% de la hauteur

        scaledBottle = Bitmap.createScaledBitmap(bottleBitmap, bottleWidth, bottleHeight, true)
        scaledCaddie = Bitmap.createScaledBitmap(caddieBitmap, caddieWidthRatio, caddieHeightRatio, true)
        scaledFauxBottle = Bitmap.createScaledBitmap(fauxBottleBitmap, bottleWidth, bottleHeight, true)

        caddieWidth = scaledCaddie.width
        caddieHeight = scaledCaddie.height
        caddieY = h - caddieHeight.toFloat() - 20f
        caddieX = (w / 2 - caddieWidth / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isGameRunning) {
            val iterator = bottles.iterator()
            while (iterator.hasNext()) {
                val bottle = iterator.next()
                bottle.y += 10f

                val bitmap = if (bottle.isReal) scaledBottle else scaledFauxBottle
                canvas.drawBitmap(bitmap, bottle.x, bottle.y, null)

                if (bottle.y + bitmap.height >= caddieY &&
                    bottle.x + bitmap.width >= caddieX &&
                    bottle.x <= caddieX + caddieWidth
                ) {
                    iterator.remove()
                    if (bottle.isReal) score++ else score--
                }
            }
        }

        // Dessiner le caddie
        canvas.drawBitmap(scaledCaddie, caddieX, caddieY, null)

        // Afficher le score
        canvas.drawText("Score : $score", 20f, 80f, scorePaint)

        // Afficher le temps restant
        canvas.drawText("Temps : $timeLeft s", 20f, 150f, scorePaint)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            caddieX = event.x - caddieWidth / 2
        }
        return true
    }

    private fun spawnBottle() {
        if (!isGameRunning) return
        val x = Random.nextInt(0, width - scaledBottle.width).toFloat()
        val isReal = Random.nextFloat() < 0.7f // 70% chance que ce soit un vrai jager
        bottles.add(Bottle(x, 0f, isReal))
        postDelayed(::spawnBottle, spawnInterval)
    }

    data class Bottle(var x: Float, var y: Float, val isReal: Boolean)
}
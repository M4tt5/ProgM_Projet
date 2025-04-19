package com.example.game

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.random.Random

class CatchGameView(context: Context, attrs: AttributeSet? = null, private val isQuickPlay: Boolean = false, private val listener: OnCatchGameFinishedListener? = null) : View(context, attrs) {
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

    private var realBottleCount = 0
    private val maxRealBottles = 30
    private var totalBottlesToSpawn = 0
    private var bottlesSpawned = 0

    private var isGameRunning = true


    private var spawnInterval =400L // temps entre deux bouteilles

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
        realBottleCount = 0
        bottlesSpawned = 0

        // Déterminer combien de fausses bouteilles il y aura (entre 10 et 20)
        val fakeBottles = Random.nextInt(10, 21)
        totalBottlesToSpawn = maxRealBottles + fakeBottles

        postDelayed(::spawnBottle, 500)
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

                // Collision avec le caddie
                if (bottle.y + bitmap.height >= caddieY &&
                    bottle.x + bitmap.width >= caddieX &&
                    bottle.x <= caddieX + caddieWidth
                ) {
                    iterator.remove()
                    if (bottle.isReal) score++ else score--
                }

                //Supprimer si hors écran
                else if (bottle.y > height) {
                    iterator.remove()
                }
            }
            if (bottles.isEmpty() && bottlesSpawned >= totalBottlesToSpawn) {
                isGameRunning = false
                if (isQuickPlay) {
                    listener?.onGameFinished(score)
                } else {
                    // En mode entraînement, on affiche le score dans un Toast et on revient à l'écran d'entraînement
                    Toast.makeText(context, "Score final : $score", Toast.LENGTH_LONG).show()

                    // On revient à l'activité Entrainement
                    val intent = Intent(context, Entrainement::class.java)
                    context.startActivity(intent)
                }
            }
        }

        // Dessiner le caddie
        canvas.drawBitmap(scaledCaddie, caddieX, caddieY, null)

        // Afficher le score
        canvas.drawText("Score : $score", 20f, 80f, scorePaint)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            caddieX = event.x - caddieWidth / 2
        }
        return true
    }

    private fun spawnBottle() {
        if (!isGameRunning || bottlesSpawned >= totalBottlesToSpawn) return

        val x = Random.nextInt(0, width - scaledBottle.width).toFloat()
        val isReal: Boolean

        if (realBottleCount < maxRealBottles) {
            // Tant qu’on n’a pas atteint 30 vraies bouteilles, 70% chance de vraie
            isReal = Random.nextFloat() < 0.7f
            if (isReal) realBottleCount++
        } else {
            // Sinon, forcément faux
            isReal = false
        }

        bottles.add(Bottle(x, 0f, isReal))
        bottlesSpawned++

        // Nouveau spawn tant qu’il reste des bouteilles à générer
        if (bottlesSpawned < totalBottlesToSpawn) {
            postDelayed(::spawnBottle, spawnInterval)
        }
    }

    interface OnCatchGameFinishedListener {
        fun onGameFinished(score: Int)
    }

    data class Bottle(var x: Float, var y: Float, val isReal: Boolean)
}
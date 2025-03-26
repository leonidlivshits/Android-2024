package com.example.lab17touch

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var textView: TextView
    private lateinit var rootLayout: FrameLayout
    private lateinit var touchCircle: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        rootLayout = findViewById(R.id.rootLayout)
        touchCircle = findViewById(R.id.touchCircle)

        // Инициализация GestureDetector с обработчиком жестов
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                updateUI("Одиночный тап")
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                updateUI("Двойной тап")
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                updateUI("Долгое нажатие")
            }

            @Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                updateUI("Резкое движение")
                return true
            }

            @Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                updateUI("Скролл")
                return true
            }
        })

        // Обработчик касаний для корневого layout
        rootLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Показываем кружок и перемещаем его в точку касания
                    touchCircle.visibility = View.VISIBLE
                    moveCircle(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    // Перемещаем кружок за пальцем
                    moveCircle(event.x, event.y)
                }
                MotionEvent.ACTION_UP -> {
                    // Скрываем кружок, когда палец убран
                    touchCircle.visibility = View.INVISIBLE
                }
            }
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun moveCircle(x: Float, y: Float) {
        touchCircle.x = x - touchCircle.width / 2
        touchCircle.y = y - touchCircle.height / 2
    }

    private fun updateUI(gestureText: String) {
        textView.text = gestureText
        rootLayout.setBackgroundColor(generateRandomColor())

        Handler(Looper.getMainLooper()).postDelayed({
            textView.text = "Лившиц Леонид, БПИ 235"
            rootLayout.setBackgroundColor(Color.WHITE)
        }, 5000)
    }

    private fun generateRandomColor(): Int {
        return Color.argb(
            255,
            (0..255).random(),
            (0..255).random(),
            (0..255).random()
        )
    }
}
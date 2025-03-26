package com.example.lab9menu

import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView.text = "Лившиц Леонид, БПИ 235"

        // Регистрируем TextView для контекстного меню
        registerForContextMenu(textView)
    }

    // Создание опционального меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Обработка выбора пунктов опционального меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_red -> {
                textView.setTextColor(Color.RED)
                true
            }
            R.id.menu_green -> {
                textView.setTextColor(Color.GREEN)
                true
            }
            R.id.menu_blue -> {
                textView.setTextColor(Color.BLUE)
                true
            }
            R.id.menu_toggle_visibility -> {
                if (textView.visibility == View.VISIBLE) {
                    textView.visibility = View.INVISIBLE
                    item.title = "Показать текст"
                } else {
                    textView.visibility = View.VISIBLE
                    item.title = "Скрыть текст"
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Создание контекстного меню
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    // Обработка выбора пунктов контекстного меню
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_clear_text -> {
                textView.text = ""
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
package com.example.lab8widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class WidgetConfigActivity : AppCompatActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var editTextUrl: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        // Получаем ID виджета
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // Если виджет уже существует, загружаем текущий URL
        editTextUrl = findViewById(R.id.editTextUrl)
        val prefs = getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
        val currentUrl = prefs.getString("url_$appWidgetId", "https://yandex.ru")
        editTextUrl.setText(currentUrl)

        // Сохраняем URL и завершаем настройку
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val url = editTextUrl.text.toString()
            saveUrl(url)
            finishConfig()
        }
    }

    private fun saveUrl(url: String) {
        val prefs = getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("url_$appWidgetId", url).apply()

        // Обновляем виджет
        val appWidgetManager = AppWidgetManager.getInstance(this)
        MyWidget().updateAppWidget(this, appWidgetManager, appWidgetId)
    }

    private fun finishConfig() {
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}
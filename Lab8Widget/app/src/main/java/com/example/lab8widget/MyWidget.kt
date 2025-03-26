package com.example.lab8widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

class MyWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Получаем URL из SharedPreferences
        val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
        val url = prefs.getString("url_$appWidgetId", "https://yandex.ru")

        // Создаем Intent для открытия сайта
        val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val pendingOpenUrlIntent = PendingIntent.getActivity(context, 0, openUrlIntent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_button, pendingOpenUrlIntent)

        // Создаем Intent для редактирования URL
        val editIntent = Intent(context, WidgetConfigActivity::class.java)
        editIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingEditIntent = PendingIntent.getActivity(context, appWidgetId, editIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.widget_edit_button, pendingEditIntent)

        // Обновляем виджет
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
package com.example.lab15sqlite

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var slider: Slider
    private lateinit var selectedItemText: TextView
    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView // Объявляем как поле класса
    private var items = emptyList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        slider = findViewById(R.id.itemsSlider)
        selectedItemText = findViewById(R.id.selectedItemText)

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.itemsRecyclerView) // Инициализируем
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(emptyList()) { item ->
            showEditDialog(item)
        }
        recyclerView.adapter = adapter

        setupSlider()
        setupAddButton() // Добавляем вызов метода
        refreshData()
    }

    private fun setupSlider() {
        slider.addOnChangeListener { _, value, _ ->
            val index = value.toIntCoerced()
            if (index in items.indices) {
                // Прокрутка RecyclerView к выбранному элементу
                recyclerView.smoothScrollToPosition(index)
                // Анимация выделения
                animateItemSelection(items[index].text)
            }
        }
    }

    private fun setupAddButton() {
        val addButton = findViewById<Button>(R.id.addButton)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)

        addButton.setOnClickListener {
            val text = inputEditText.text.toString()
            if (text.isNotBlank()) {
                dbHelper.addItem(text)
                inputEditText.text.clear()
                refreshData()
            }
        }
    }

    private fun Float.toIntCoerced() = coerceIn(0f, (items.size - 1).toFloat()).toInt()

    private fun refreshData() {
        items = dbHelper.getAllItems().reversed()
        adapter.updateItems(items)
        updateSlider()
        updateUIVisibility()
    }

    private fun updateSlider() {
        when {
            items.size > 1 -> {
                slider.visibility = Slider.VISIBLE
                slider.valueFrom = 0f
                slider.valueTo = (items.size - 1).toFloat()
                slider.value = 0f
                slider.stepSize = 1f
            }
            items.size == 1 -> {
                slider.visibility = Slider.GONE
                animateItemSelection(items.first().text)
            }
            else -> {
                slider.visibility = Slider.GONE
                selectedItemText.visibility = Slider.GONE
            }
        }
    }

    private fun updateUIVisibility() {
        if (items.isEmpty()) {
            slider.visibility = Slider.GONE
            selectedItemText.visibility = Slider.GONE
        } else {
            selectedItemText.visibility = Slider.VISIBLE
            // Видимость слайдера теперь управляется в updateSlider()
        }
    }

    private fun animateItemSelection(text: String) {
        selectedItemText.text = text
        selectedItemText.apply {
            scaleX = 0.8f
            scaleY = 0.8f
            alpha = 0.5f
            animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .alpha(1f)
                .setDuration(300)
                .withEndAction {
                    animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }
    }

    private fun showEditDialog(item: Item) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)
        val editText = dialogView.findViewById<EditText>(R.id.editDialogText)
        editText.setText(item.text)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Обновить") { _, _ ->
                val newText = editText.text.toString()
                if (newText.isNotBlank()) {
                    dbHelper.updateItem(item.id, newText)
                    refreshData() // Используем refreshData вместо refreshList
                }
            }
            .setNegativeButton("Удалить") { _, _ ->
                dbHelper.deleteItem(item.id)
                refreshData() // Используем refreshData вместо refreshList
            }
            .setNeutralButton("Отмена", null)
            .show()
    }
}
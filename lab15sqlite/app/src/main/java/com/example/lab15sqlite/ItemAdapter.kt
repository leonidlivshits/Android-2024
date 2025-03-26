package com.example.lab15sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    // Текущая выделенная позиция
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemText)

        init {
            itemView.setOnClickListener {
                // Обновляем выделение при клике
                val previous = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previous)
                notifyItemChanged(selectedPosition)
                onItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.text

        // Анимация появления элемента
        if (holder.adapterPosition > lastAnimatedPosition) {
            holder.itemView.apply {
                scaleX = 0.7f
                scaleY = 0.7f
                alpha = 0f
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(OvershootInterpolator(2.0f))
                    .start()
            }
            lastAnimatedPosition = holder.adapterPosition
        }

        // Анимация выделения
        holder.itemView.animate()
            .scaleX(if (position == selectedPosition) 1.1f else 1f)
            .scaleY(if (position == selectedPosition) 1.1f else 1f)
            .setDuration(200)
            .start()

        // Изменение цвета фона для выделенного элемента
        holder.itemView.setBackgroundResource(
            if (position == selectedPosition) R.drawable.selected_item_background
            else R.drawable.default_item_background
        )
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Item>) {
        items = newItems
        selectedPosition = RecyclerView.NO_POSITION
        lastAnimatedPosition = -1
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        val previous = selectedPosition
        selectedPosition = position
        notifyItemChanged(previous)
        notifyItemChanged(position)
    }

    companion object {
        private var lastAnimatedPosition = -1
    }
}
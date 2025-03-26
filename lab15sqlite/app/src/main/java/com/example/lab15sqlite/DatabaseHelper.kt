package com.example.lab15sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "items.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TEXT = "text"
        private const val COLUMN_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TEXT TEXT,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(text: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, text)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_TEXT, COLUMN_CREATED_AT),
            null, null, null, null,
            "$COLUMN_CREATED_AT DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val text = it.getString(it.getColumnIndexOrThrow(COLUMN_TEXT))
                val createdAt = it.getString(it.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                items.add(Item(id, text, createdAt))
            }
        }
        return items
    }

    fun updateItem(id: Long, newText: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, newText)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteItem(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todo.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK = "task"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TASK TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(task: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK, task)
        val id = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return id
    }

    fun getAllTasks(): List<TodoItem> {
        val tasks = mutableListOf<TodoItem>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK))
                    tasks.add(TodoItem(id, task))
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return tasks
    }

    fun deleteTask(id: Long): Boolean {
        val db = this.writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows > 0
    }

}

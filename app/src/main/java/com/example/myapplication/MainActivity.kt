package com.example.myapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), TodoAdapter.OnItemClickListener {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var dbHelper: TodoDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTask = findViewById<EditText>(R.id.etTask)
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        todoAdapter = TodoAdapter(mutableListOf(), this)
        dbHelper = TodoDbHelper(this)

        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddTask.setOnClickListener {
            val task = etTask.text.toString()
            if (task.isNotEmpty()) {
                val id = dbHelper.insertTask(task)
                if (id != -1L) {
                    todoAdapter.addTodoItem(TodoItem(id, task))
                    etTask.text.clear()
                } else {
                    Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        loadTasks()
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks()
        todoAdapter.setTodoItems(tasks)
    }

    override fun onItemClick(id: Long) {
        deleteTask(id)
    }

    private fun deleteTask(id: Long) {
        val isDeleted = dbHelper.deleteTask(id)

        if (isDeleted) {
            todoAdapter.removeTodoItem(id)
            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

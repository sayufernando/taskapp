package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todoItems: MutableList<TodoItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = todoItems[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
        notifyItemInserted(todoItems.size - 1)
    }

    fun setTodoItems(todoItems: List<TodoItem>) {
        this.todoItems.clear()
        this.todoItems.addAll(todoItems)
        notifyDataSetChanged()
    }

    fun removeTodoItem(id: Long) {
        val index = todoItems.indexOfFirst { it.id == id }
        if (index != -1) {
            todoItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvTask: TextView = itemView.findViewById(R.id.tvTask)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(todoItem: TodoItem) {
            tvTask.text = todoItem.task
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(todoItems[position].id)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Long)
    }
}

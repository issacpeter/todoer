package com.todoer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoer.data.model.Todo
import com.todoer.databinding.TodoItemViewBinding
import com.todoer.utils.Extensions
import com.todoer.utils.Extensions.toStringDate
import com.todoer.utils.Repeat
import java.util.ArrayList

class TodoAdapter(var todoList: List<Todo>? = ArrayList<Todo>()): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var onTodoItemClickedListener: OnTodoItemClickedListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoItemViewBinding.inflate(inflater, parent, false)
        return TodoViewHolder(binding)
    }

    fun setTodos(todos: List<Todo>) {
        this.todoList = todos.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(todoList.isNullOrEmpty()) 0 else todoList!!.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int){
        holder.binding.root.setOnClickListener{onTodoItemClickedListener!!.onTodoItemClicked(todoList!!.get(position))}
        holder.binding.root.setOnLongClickListener{
            onTodoItemClickedListener!!.onTodoItemLongClicked(todoList!!.get(position))
            true
        }
        holder.onBindViews(position)
    }

    inner class TodoViewHolder(val binding: TodoItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindViews(position: Int){
            if (itemCount != 0){
                binding.title.text = todoList?.get(position)?.title
                binding.detail.text = todoList?.get(position)?.detail
                binding.dateTime.text = todoList?.get(position)?.updatedAt?.toStringDate() ?: ""
                binding.repeat.text = when(todoList?.get(position)?.repeat){
                    Repeat.DAILY.ordinal -> "Repeats Daily"
                    Repeat.WEEKLY.ordinal -> "Repeats weekly"
                    else -> ""
                }
            }

        }
    }

    fun setTodoItemClickedListener(onTodoItemClickedListener: OnTodoItemClickedListener){
        this.onTodoItemClickedListener = onTodoItemClickedListener
    }

    interface OnTodoItemClickedListener{
        fun onTodoItemClicked(todo: Todo)
        fun onTodoItemLongClicked(todo: Todo)
    }
}
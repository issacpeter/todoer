package com.todoer.data.repository

import androidx.lifecycle.LiveData
import com.todoer.data.api.RetrofitService
import com.todoer.data.local.TodoListDatabase
import com.todoer.data.model.Todo

class MainRepository constructor(private val todoDatabase: TodoListDatabase? = null, val retrofitService: RetrofitService? = null) {

    suspend fun login(email: String, password: String) = retrofitService?.login(email, password)

    suspend fun getloadAllTasks(): List<Todo>? {
        return todoDatabase?.getTodoDao()?.getTodoList()
    }

    suspend fun addTodo(todo: Todo) : Long? {
        return todoDatabase?.getTodoDao()?.saveTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) : Int? {
        return todoDatabase?.getTodoDao()?.updateTodo(todo)
    }

    suspend fun removeTodo(todo: Todo) : Int? {
        return todoDatabase?.getTodoDao()?.removeTodo(todo)
    }

}
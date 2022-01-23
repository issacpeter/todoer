package com.todoer.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.todoer.data.model.Todo

@Dao
interface TodoDao{

    @Query("SELECT*FROM todo ORDER BY tId ASC")
    suspend fun getTodoList(): List<Todo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTodo(todo: Todo): Long

    @Update
    suspend fun updateTodo(todo: Todo): Int

    @Delete
    suspend fun removeTodo(todo: Todo): Int
}
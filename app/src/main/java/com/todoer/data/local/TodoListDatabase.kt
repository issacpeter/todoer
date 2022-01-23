package com.todoer.data.local


import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.todoer.data.model.Todo


@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TodoListDatabase: RoomDatabase(){

    abstract fun getTodoDao(): TodoDao

    companion object {
        val databaseName = "tododatabase"
        var todoListDatabase: TodoListDatabase? = null
        val LOCK = Any()

//        fun getInstance(context: Context): TodoListDatabase?{
//            if (todoListDatabase == null){
//                synchronized(LOCK) {
//                    todoListDatabase = Room.databaseBuilder(context,
//                        TodoListDatabase::class.java,
//                        TodoListDatabase.databaseName)
////                        .allowMainThreadQueries()
//                        .build()
//                }
//
//            }
//            return todoListDatabase
//        }

        fun getInstance(context: Context): TodoListDatabase =
            todoListDatabase ?: synchronized(this) { todoListDatabase ?: buildDatabase(context).also { todoListDatabase = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, TodoListDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
    }
}
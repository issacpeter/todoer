package com.todoer.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todo")
class Todo(
    @ColumnInfo(name = "todo_title")
    var title:String = "",
    @ColumnInfo(name = "todo_detail")
    var detail:String = "",
    @ColumnInfo(name = "repeat")
    var repeat:Int = 0,
    @ColumnInfo(name = "updated_at")
    var updatedAt:Date? = null,
    @PrimaryKey(autoGenerate = true) var tId: Int = 0)
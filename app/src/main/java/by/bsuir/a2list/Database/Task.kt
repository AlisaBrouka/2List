package by.bsuir.a2list.Database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "task")
    val task: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "reminder_time")
    val reminder_time: String = "",

    @NonNull
    @ColumnInfo(name = "isDone")
    val isDone: Boolean = false
)
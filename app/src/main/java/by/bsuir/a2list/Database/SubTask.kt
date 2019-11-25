package by.bsuir.a2list.Database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("original_task"),
            onDelete = ForeignKey.CASCADE)
    ])

data class SubTask (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "original_task")
    val id_original: Int,

    @ColumnInfo(name = "task")
    val task: String,

    @ColumnInfo(name = "reminder_time")
    val reminder_time: String = "",

    @ColumnInfo(name = "isDone")
    val isDone: Boolean = false
)
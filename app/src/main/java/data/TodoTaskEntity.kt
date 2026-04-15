package pl.oleksandra.pam.lab06.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.oleksandra.pam.lab06.Priority
import pl.oleksandra.pam.lab06.TodoTask
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val deadline: LocalDate,
    var isDone: Boolean,
    val priority: Priority
) {
    fun toModel(): TodoTask {
        return TodoTask(id, title, deadline, isDone, priority)
    }

    companion object {
        // Zamiana z UI -> Bazy
        fun fromModel(model: TodoTask): TodoTaskEntity {
            return TodoTaskEntity(
                id = model.id,
                title = model.title,
                deadline = model.deadline,
                isDone = model.isDone,
                priority = model.priority
            )
        }
    }
}
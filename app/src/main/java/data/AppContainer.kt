package pl.oleksandra.pam.lab06.data
import android.content.Context
import pl.oleksandra.pam.lab8.NotificationHandler
interface AppContainer {
    val todoTaskRepository: TodoTaskRepository
    val notificationHandler: NotificationHandler
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val todoTaskRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(AppDatabase.getInstance(context).taskDao())
    }
    override val notificationHandler: NotificationHandler by lazy {
        NotificationHandler(context)
    }
}
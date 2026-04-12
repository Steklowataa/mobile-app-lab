package pl.wsei.pam.lab06.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.wsei.pam.lab06.Priority
import pl.wsei.pam.lab06.TodoTask
import java.time.LocalDate

class FormViewModel(private val repository: TodoTaskRepository) : ViewModel() {

    // Stan UI formularza - obserwowany przez Compose
    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    // Funkcja zapisu - wywoływana po kliknięciu "Zapisz"
    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
        }
    }

    // Aktualizacja stanu przy każdej zmianie w polu formularza
    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        return uiState.title.isNotBlank()
    }
}

// Klasy pomocnicze do stanu UI
data class TodoTaskUiState(
    var todoTask: TodoTaskForm = TodoTaskForm(),
    val isValid: Boolean = false
)

data class TodoTaskForm(
    val id: Int = 0,
    val title: String = "",
    val deadline: Long = LocalDateConverter.toMillis(LocalDate.now()),
    val isDone: Boolean = false,
    val priority: String = Priority.Low.name
)

// Funkcje mapowania (Konwersja między modelem bazy a modelem formularza)
fun TodoTaskForm.toTodoTask(): TodoTask = TodoTask(
    id = id,
    title = title,
    deadline = LocalDateConverter.fromMillis(deadline),
    isDone = isDone,
    priority = Priority.valueOf(priority)
)

fun TodoTask.toTodoTaskForm(): TodoTaskForm = TodoTaskForm(
    id = id,
    title = title,
    deadline = LocalDateConverter.toMillis(deadline),
    isDone = isDone,
    priority = priority.name
)
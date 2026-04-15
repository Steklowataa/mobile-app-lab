package pl.oleksandra.pam.lab06.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.oleksandra.pam.lab06.TodoTask

// Klasa przechowująca stan UI - domyślnie pusta lista
data class ListUiState(val items: List<TodoTask> = listOf())

class ListViewModel(val repository: TodoTaskRepository) : ViewModel() {

    // Strumień danych, który automatycznie aktualizuje UI, gdy baza się zmieni
    val listUiState: StateFlow<ListUiState> = repository.getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
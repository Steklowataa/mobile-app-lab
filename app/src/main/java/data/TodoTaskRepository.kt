package pl.oleksandra.pam.lab06.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.oleksandra.pam.lab06.TodoTask

interface TodoTaskRepository {
    fun getAllAsStream(): Flow<List<TodoTask>>
    fun getItemAsStream(id: Int): Flow<TodoTask?>
    suspend fun insertItem(item: TodoTask)
    suspend fun deleteItem(item: TodoTask)
    suspend fun updateItem(item: TodoTask)
}

class DatabaseTodoTaskRepository(private val dao: TodoTaskDao) : TodoTaskRepository {

    override fun getAllAsStream(): Flow<List<TodoTask>> {
        return dao.findAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getItemAsStream(id: Int): Flow<TodoTask?> {
        // Zwraca konkretne zadanie lub null, jeśli nie istnieje
        return dao.find(id).map { it?.toModel() }
    }

    override suspend fun insertItem(item: TodoTask) {
        // Zamieniamy TodoTask (model UI) na TodoTaskEntity (model bazy) i zapisujemy
        dao.insert(TodoTaskEntity.fromModel(item))
    }

    override suspend fun deleteItem(item: TodoTask) {
        dao.remove(TodoTaskEntity.fromModel(item))
    }

    override suspend fun updateItem(item: TodoTask) {
        // Implementacja metody UPDATE
        dao.update(TodoTaskEntity.fromModel(item))
    }
}
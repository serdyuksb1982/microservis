package ru.serdyuk.micro.planner.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.Task
import ru.serdyuk.micro.planner.todo.repo.TaskRepository
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class TaskService(private val taskRepository: TaskRepository) {
    fun findAll(userId: Long): List<Task> {
        return taskRepository.findByUserIdOrderByTitleAsc(userId)
    }

    fun add(task: Task): Task {
        return taskRepository.save(task)
    }

    fun update(task: Task): Task {
        return taskRepository.save(task)
    }

    fun deleteById(id: Long) {
        taskRepository.deleteById(id)
    }

    fun findByParams(
        text: String?,
        completed: Boolean?,
        priorityId: Long?,
        categoryId: Long?,
        userId: Long,
        dateFrom: Date?,
        dateTo: Date?,
        paging: PageRequest
    ): Page<Task> {
        return taskRepository.findByParams(text, completed, priorityId, categoryId, userId, dateFrom, dateTo, paging)
    }

    fun findById(id: Long): Task {
        return taskRepository.findById(id).get()
    }
}
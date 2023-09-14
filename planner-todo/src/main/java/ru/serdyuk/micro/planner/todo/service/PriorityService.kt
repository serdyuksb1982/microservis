package ru.serdyuk.micro.planner.todo.service

import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.Priority
import ru.serdyuk.micro.planner.todo.repo.PriorityRepository
import javax.transaction.Transactional

@Service
@Transactional
class PriorityService(private val priorityRepository: PriorityRepository) {
    fun findAll(userId: Long): List<Priority> {
        return priorityRepository.findByUserIdOrderByTitleAsc(userId)
    }

    fun add(priority: Priority): Priority {
        return priorityRepository.save(priority) //save обновляет или создает новый объект, если его не было
    }

    fun update(priority: Priority): Priority {
        return priorityRepository.save(priority)
    }

    fun deleteById(id: Long) {
        priorityRepository.deleteById(id)
    }

    fun find(text: String?, userId: Long): List<Priority> {
        return priorityRepository.findByTitle(text, userId)
    }

    fun findById(id: Long): Priority {
        return priorityRepository.findById(id).get()
    }
}
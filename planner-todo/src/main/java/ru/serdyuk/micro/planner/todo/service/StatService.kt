package ru.serdyuk.micro.planner.todo.service

import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.Stat
import ru.serdyuk.micro.planner.todo.repo.StatRepository
import javax.transaction.Transactional

@Service
@Transactional
class StatService(private val repository: StatRepository) {
    fun findStat(userId: Long): Stat {
        return repository.findByUserId(userId)
    }
}
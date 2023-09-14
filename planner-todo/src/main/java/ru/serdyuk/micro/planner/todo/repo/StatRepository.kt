package ru.serdyuk.micro.planner.todo.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.serdyuk.micro.planner.entity.Stat

//
@Repository
interface StatRepository : CrudRepository<Stat, Long> {
    fun findByUserId(id: Long): Stat? // связь один к одному
}
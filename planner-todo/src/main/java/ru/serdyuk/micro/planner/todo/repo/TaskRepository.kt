package ru.serdyuk.micro.planner.todo.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.serdyuk.micro.planner.entity.Task
import java.util.*

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    @Query(
        "SELECT t from Task t where " +
                "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title, '%'))) and " +
                "(:completed is null  or t.completed=:completed) and " +
                "(:priorityId is null or t.priority.id=:priorityId) and " +
                "(:categoryId is null or t.category.id=:categoryId) and " +
                "(:categoryId is null or t.category.id=:categoryId) and " +
                "(" +
                "(cast(:dateFrom as timestamp) is null or t.taskDate>=:dateFrom) and " +
                "(cast(:dateTo as timestamp) is null or t.taskDate<=:dateTo)" +
                ") and " +
                "(t.userId=:userId)"
    )
    fun  // показывать задачи только определенного пользователя, а не все
    // искать по всем переданным параметрам (пустые параметры учитываться не будут)
            findByParams(
        @Param("title") title: String?,
        @Param("completed") completed: Boolean?,
        @Param("priorityId") priorityId: Long?,
        @Param("categoryId") categoryId: Long?,
        @Param("userId") userId: Long,
        @Param("dateFrom") dateFrom: Date?,
        @Param("dateTo") dateTo: Date?,
        pageable: Pageable
    ): Page<Task>

    // поиск всех задач конкретного пользователя
    fun findByUserIdOrderByTitleAsc(userId: Long): List<Task>
}
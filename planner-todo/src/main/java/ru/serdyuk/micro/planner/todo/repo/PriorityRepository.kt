package ru.serdyuk.micro.planner.todo.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.serdyuk.micro.planner.entity.Priority

@Repository
interface PriorityRepository : JpaRepository<Priority, Long> {
    fun findByUserIdOrderByTitleAsc(userId: Long): List<Priority>

    @Query(
        "SELECT p FROM Priority p where " +
                "(:title is null or :title='' " +
                " or lower(p.title) like lower(concat('%', :title, '%') ) ) " +
                " and p.userId=:id " +  // фильтрация для конкретного пользователя
                " order by p.title asc "
    )
    fun findByTitle(@Param("title") title: String?, @Param("id") id: Long): List<Priority>
}
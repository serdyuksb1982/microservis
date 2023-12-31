package ru.serdyuk.micro.planner.todo.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.serdyuk.micro.planner.entity.Category

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByUserIdOrderByTitleAsc(userId: Long): List<Category>

    @Query(
        "SELECT c FROM Category c where " +
                "(:title is null or :title='' " +
                " or lower(c.title) like lower(concat('%', :title, '%') ) ) " +
                " and c.userId=:id " +  // фильтрация для конкретного пользователя
                " order by c.title asc "
    )
    fun findByTitles(@Param("title") title: String?, @Param("id") id: Long): List<Category>
}
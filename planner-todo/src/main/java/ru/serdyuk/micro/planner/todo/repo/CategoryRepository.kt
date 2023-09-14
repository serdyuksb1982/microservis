package ru.serdyuk.micro.planner.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.serdyuk.micro.planner.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdOrderByTitleAsc(Long userId);

    @Query(
            "SELECT c FROM Category c where " +
                    "(:title is null or :title='' " +
                    " or lower(c.title) like lower(concat('%', :title, '%') ) ) " +
                    " and c.userId=:id " + // фильтрация для конкретного пользователя
                    " order by c.title asc " // сортировка по названию
    )
    List<Category> findByTitles(@Param("title") String title, @Param("id") Long id);
}

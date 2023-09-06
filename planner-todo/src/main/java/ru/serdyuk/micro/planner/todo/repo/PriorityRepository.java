package ru.serdyuk.micro.planner.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.serdyuk.micro.planner.entity.Priority;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserIdOrderByTitleAsc(Long userId);

    @Query(
            "SELECT p FROM Priority p where " +
                    "(:title is null or :title='' " +
                    " or lower(p.title) like lower(concat('%', :title, '%') ) ) " +
                    " and p.userId=:id " + // фильтрация для конкретного пользователя
                    " order by p.title asc " // сортировка по названию
    )
    List<Priority> findByTitle(@Param("title") String title, @Param("id") Long id);
}

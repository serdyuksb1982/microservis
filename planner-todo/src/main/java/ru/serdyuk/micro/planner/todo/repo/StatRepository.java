package ru.serdyuk.micro.planner.todo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.serdyuk.micro.planner.entity.Stat;

//
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserId(Long id);// связь один к одному
}

package ru.serdyuk.micro.planner.todo.service;

import org.springframework.stereotype.Service;
import ru.serdyuk.micro.planner.entity.Stat;
import ru.serdyuk.micro.planner.todo.repo.StatRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatService {

    private final StatRepository repository;

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(Long userId) {
        return repository.findByUserId(userId);
    }
}

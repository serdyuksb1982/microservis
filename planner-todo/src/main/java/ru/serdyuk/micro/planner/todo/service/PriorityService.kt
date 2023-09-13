package ru.serdyuk.micro.planner.todo.service;

import org.springframework.stereotype.Service;
import ru.serdyuk.micro.planner.entity.Priority;
import ru.serdyuk.micro.planner.todo.repo.PriorityRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }


    public List<Priority> findAll(Long userId) {
        return priorityRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority);//save обновляет или создает новый объект, если его не было
    }

    public Priority update(Priority priority) {
        return priorityRepository.save(priority);
    }

    public void deleteById(Long id) {
        priorityRepository.deleteById(id);
    }

    public List<Priority> find(String text, Long userId) {
        return priorityRepository.findByTitle(text, userId);
    }

    public Priority findById(Long id) {
        return priorityRepository.findById(id).get();
    }
}

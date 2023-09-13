package ru.serdyuk.micro.planner.todo.service;

import org.springframework.stereotype.Service;
import ru.serdyuk.micro.planner.entity.Category;
import ru.serdyuk.micro.planner.todo.repo.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAll(Long userId) {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Category add(Category category) {
        return categoryRepository.save(category);//save обновляет или создает новый объект, если его не было
    }

    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> findByTitle(String text, Long userId) {
        return categoryRepository.findByTitles(text, userId);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }
}

package ru.serdyuk.micro.planner.todo.service

import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.Category
import ru.serdyuk.micro.planner.todo.repo.CategoryRepository
import javax.transaction.Transactional

@Service
@Transactional
class CategoryService(private val categoryRepository: CategoryRepository) {
    fun findAll(userId: Long): List<Category> {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId)
    }

    fun add(category: Category): Category {
        return categoryRepository.save(category) //save обновляет или создает новый объект, если его не было
    }

    fun update(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun deleteById(id: Long) {
        categoryRepository.deleteById(id)
    }

    fun findByTitle(text: String?, userId: Long): List<Category> {
        return categoryRepository.findByTitles(text, userId)
    }

    fun findById(id: Long): Category {
        return categoryRepository.findById(id).get()
    }
}
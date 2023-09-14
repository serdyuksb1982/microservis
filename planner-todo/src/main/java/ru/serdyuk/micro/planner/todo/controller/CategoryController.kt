package ru.serdyuk.micro.planner.todo.controller

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.serdyuk.micro.planner.entity.Category
import ru.serdyuk.micro.planner.todo.feign.UserFeignClient
import ru.serdyuk.micro.planner.todo.search.CategorySearchValues
import ru.serdyuk.micro.planner.todo.service.CategoryService

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService,
    @Qualifier("ru.serdyuk.micro.planner.todo.feign.UserFeignClient") private val userFeignClient: UserFeignClient
    ) {

    @PostMapping("/all")
    fun findAll(@RequestBody userId: Long): List<Any> {
        return categoryService.findAll(userId)
    }

    @PostMapping("/add")
    fun add(@RequestBody category: Category): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (category.id != null && category.id != 0L) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть ошибка
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }
        // если передали пустое значение title
        if (category.title == null || category.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title must be null...", HttpStatus.NOT_ACCEPTABLE)
        }

        /*if (userWebClientBuilder.userExists(category.getUserId())) {
            return ResponseEntity.ok(categoryService.add(category));
        }
        return new  ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);*/

        // подписка на результат
        /*userWebClientBuilder.userExistsAsync(category.getUserId()).subscribe(user -> System.out.println("user = " + user));
        return new  ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);*/
        // вызов мс через feign interface
        val userResponseEntity = userFeignClient.findUserById(category.userId!!) ?: return ResponseEntity<Any>(
                "система пользователей недоступна, попробуйте позже!",
                HttpStatus.NOT_FOUND
            )
        return if (userResponseEntity.body != null) { //если response не пустой
            ResponseEntity.ok(categoryService.add(category))
        } else ResponseEntity<Any>("user id=" + category.userId + " not found", HttpStatus.NOT_ACCEPTABLE)
    }

    @PutMapping("/update")
    fun update(@RequestBody category: Category): ResponseEntity<Any> {
        //
        if (category.id == null || category.id == 0L) {
            return ResponseEntity<Any>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }
        if (category.title == null || category.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        categoryService.update(category)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // для удаления используем тип запроса delete и передаем id для удаеления
    // можно также использовать метод post и передавать id в теле запроса
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        //можно обойтись и без try-catch, тогда будет возвращаться полная ошибка
        try {
            categoryService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // поиск по любым параметрам CategorySearchValues
    @PostMapping("/search")
    fun search(@RequestBody categorySearchValues: CategorySearchValues): ResponseEntity<Any> {
        if (categorySearchValues.userId == 0L) {
            return ResponseEntity<Any>("missed param: user Id", HttpStatus.NOT_ACCEPTABLE)
        }
        // поиск категорий пользователя по названию
        val list = categoryService.findByTitle(categorySearchValues.title, categorySearchValues.userId)
        return ResponseEntity.ok(list)
    }

    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {

        val category: Category? = try {
            categoryService.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(category)
    }
}
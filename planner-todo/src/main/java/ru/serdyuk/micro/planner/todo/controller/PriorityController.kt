package ru.serdyuk.micro.planner.todo.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.serdyuk.micro.planner.entity.Priority
import ru.serdyuk.micro.planner.todo.search.PrioritySearchValues
import ru.serdyuk.micro.planner.todo.service.PriorityService
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder

/***
 * Чтобы дать меньше шансов для взлома (например, CSRF-атак): POST/PUT запросы могут изменять/фильтровать закрытые данные
 * т.е. GET-запросы не должны использоваться для изменения/получения секретных данных
 * Если возникнет exception - вернется код 500 (Internal Server Error), поэтому не нужно все действия оборачивать в try-catch
 * Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
 * иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON
 * Названия методов могут быть любыми, главное не дублировать их имена и URL mapping.
 */
@RestController
@RequestMapping("/priority")
class PriorityController(
    private val priorityService: PriorityService,
    private val userWebClientBuilder: UserWebClientBuilder
) {

    @PostMapping("/all")
    fun findAll(@RequestBody userId: Long): List<Any> {
        return priorityService.findAll(userId)
    }

    @PostMapping("/add")
    fun add(@RequestBody priority: Priority): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (priority.id != null && priority.id != 0L) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть ошибка
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }
        // если передали пустое значение title
        if (priority.title == null || priority.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title must be null...", HttpStatus.NOT_ACCEPTABLE)
        }
        return if (userWebClientBuilder.userExists(priority.userId!!)) {
            ResponseEntity.ok(priorityService.add(priority))
        } else ResponseEntity<Any>("user id=" + priority.userId + " not found", HttpStatus.NOT_ACCEPTABLE)
    }

    @PutMapping("/update")
    fun update(@RequestBody priority: Priority): ResponseEntity<Any> {
        //
        if (priority.id == null || priority.id == 0L) {
            return ResponseEntity<Any>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }
        if (priority.title == null || priority.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        priorityService.update(priority)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // для удаления используем тип запроса delete и передаем id для удаеления
    // можно также использовать метод post и передавать id в теле запроса
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        //можно обойтись и без try-catch, тогда будет возвращаться полная ошибка
        try {
            priorityService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    fun search(@RequestBody prioritySearchValues: PrioritySearchValues): ResponseEntity<Any> {
        if (prioritySearchValues.userId == 0L) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }
        // поиск категорий пользователя по названию
        val list = priorityService.find(prioritySearchValues.title, prioritySearchValues.userId)
        return ResponseEntity.ok(list)
    }

    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        val priority: Priority? = try {
            priorityService.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(priority)
    }
}
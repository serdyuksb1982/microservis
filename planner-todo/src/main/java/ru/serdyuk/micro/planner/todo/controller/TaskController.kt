package ru.serdyuk.micro.planner.todo.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.serdyuk.micro.planner.entity.Task
import ru.serdyuk.micro.planner.todo.search.TaskSearchValues
import ru.serdyuk.micro.planner.todo.service.TaskService
import ru.serdyuk.micro.planner.utils.resttemplate.UserRestBuilder
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/task") // basic URI

class TaskController(
    private val taskService: TaskService,
    private val userWebClientBuilder: UserWebClientBuilder) {

    companion object {
        const val ID_COLUMN = "id"
    }

    //получение всех данных
    @PostMapping("/all")
    fun findAll(@RequestBody userId: Long): ResponseEntity<List<Task>> {
        return ResponseEntity.ok(taskService.findAll(userId))
    }

    // добавление данных
    @PostMapping("/add")
    fun add(@RequestBody task: Task): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (task.id != null && task.id != 0L) {
            // id создается в БД автоматически (autoincrement)
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }
        if (task.title == null || task.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        return if (userWebClientBuilder.userExists(task.userId!!)) {
            ResponseEntity.ok(taskService.add(task))
        } else ResponseEntity<Any>("user id=" + task.userId + " not found", HttpStatus.NOT_ACCEPTABLE)
        // возвращаем ошибку, если нет юзера
    }

    // обновление
    @PutMapping("/update")
    fun update(@RequestBody task: Task): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (task.id == null && task.id == 0L) {
            return ResponseEntity<Any>("redundant param: id ", HttpStatus.NOT_ACCEPTABLE)
        }
        // если передали пустое значение title
        if (task.title == null || task.title!!.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        taskService.update(task)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // удаление
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        try {
            taskService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    // получение объекта по id
    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        val task: Task? = try {
            taskService.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(task)
    }

    @PostMapping("/search")
    @Throws(ParseException::class)
    fun search(@RequestBody taskSearchValues: TaskSearchValues): ResponseEntity<Any> {
        // сначала считываем TaskSearchValues, берем оттуда все значения и сохраняем в локальные переменные, заодно проводим проверку
        val title = if (taskSearchValues.title != null) taskSearchValues.title else null
        val completed = if (taskSearchValues.completed != null && taskSearchValues.completed == 1) true else false
        val priorityId = if (taskSearchValues.priorityId != null) taskSearchValues.priorityId else null
        val categoryId = if (taskSearchValues.categoryId != null) taskSearchValues.categoryId else null
        val sortColumn = taskSearchValues.sortColumn
        val sortDirection = taskSearchValues.sortDirection
        val pageNumber = taskSearchValues.pageNumber
        val pageSize = taskSearchValues.pageSize
        val userId = taskSearchValues.userId

        // проверка на обязательные параметры
        if (userId == 0L) {
            return ResponseEntity<Any>("missed param: user Id.", HttpStatus.NOT_ACCEPTABLE)
        }

        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00-00 до 23-59
        var dateFrom: Date? = null
        var dateTo: Date? = null

        // выставить 00:00 для начальной даты (если она указана)
        if (taskSearchValues.dateFrom != null) {
            val calendarFrom = Calendar.getInstance()
            calendarFrom.time = taskSearchValues.dateFrom
            calendarFrom[Calendar.HOUR_OF_DAY] = 0
            calendarFrom[Calendar.MINUTE] = 1
            calendarFrom[Calendar.SECOND] = 1
            calendarFrom[Calendar.MILLISECOND] = 1
            dateFrom = calendarFrom.time
        }

        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.dateTo != null) {
            val calendarTo = Calendar.getInstance()
            calendarTo.time = taskSearchValues.dateTo
            calendarTo[Calendar.HOUR_OF_DAY] = 23
            calendarTo[Calendar.MINUTE] = 59
            calendarTo[Calendar.SECOND] = 59
            calendarTo[Calendar.MILLISECOND] = 999
            dateTo = calendarTo.time
        }

        // направление сортировки
        val direction =
            if (sortDirection == null || sortDirection.trim { it <= ' ' }.length == 0 || sortDirection.trim { it <= ' ' } == "asc") Sort.Direction.ASC else Sort.Direction.DESC

        // объект сортировки, который содержит столбец и направление сортировки
        val sort = Sort.by(direction, sortColumn, ID_COLUMN)
        // объект постраничности
        val pageRequest = PageRequest.of(pageNumber, pageSize, sort)
        // результат запроса с постраничным выводом
        val result =
            taskService.findByParams(title, completed, priorityId, categoryId, userId, dateFrom, dateTo, pageRequest)
        // результат запрсоса
        return ResponseEntity.ok(result)
    }


}
package ru.serdyuk.micro.planner.users.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.serdyuk.micro.planner.entity.User
import ru.serdyuk.micro.planner.users.mq.func.MessageFuncActions
import ru.serdyuk.micro.planner.users.search.UserSearchValues
import ru.serdyuk.micro.planner.users.service.UserService
import java.text.ParseException

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    var messageFuncActions: MessageFuncActions
) {
    // static const
    companion object {
        const val ID_COLUMN = "id" //имя столбца id
    }

    // add user
    @PostMapping("/add")
    fun add(@RequestBody user: User): ResponseEntity<Any> {
        //проверка на обязательные параметры
        if (user.id != null && user.id != 0L) {
            // id создается автоинкрементально автоматически JPA
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.email == null || user.email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.password == null || user.password.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: password", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.username == null || user.username.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: userName", HttpStatus.NOT_ACCEPTABLE)
        }

        // adding user
        val tmpUser = userService.add(user)
        // отправляем сообщение в очередь для генерации тестовых данных асинхронно!!!
            messageFuncActions.sendNewUserMessage(tmpUser.id)

        return ResponseEntity.ok(tmpUser)
    }

    // получение объекта по id
    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        val userOptional = userService.findById(id) //получаем контейнер Optional
        try {
            if (userOptional.isPresent) { //если объект существует в контейнере, то получаем его методом get()
                return ResponseEntity.ok(userOptional.get())
            }
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
        }
        return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
    }

    // обновление записи
    @PutMapping("/update")
    fun update(@RequestBody user: User): ResponseEntity<Any> {
        if (user.id == null || user.id == 0L) {
            return ResponseEntity<Any>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.email == null || user.email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.password == null || user.password.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: password", HttpStatus.NOT_ACCEPTABLE)
        }
        if (user.username == null || user.username.trim().isEmpty()) {
            ResponseEntity<Any>("missed param: user name", HttpStatus.NOT_ACCEPTABLE)
        }
        //save работает как на добавление, так и на обновление данных
        userService.update(user)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/deletebyid")
    fun deleteByUserId(@RequestBody userId: Long): ResponseEntity<Any> {
        try {
            userService.deleteByUserId(userId)
        } catch (exception: EmptyResultDataAccessException) {
            exception.printStackTrace()
            return ResponseEntity<Any>("userId=$userId not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/deletebyemail")
    fun deleteByUserEmail(@RequestBody email: String): ResponseEntity<Any> {
        try {
            userService.deleteByEmail(email)
        } catch (exception: EmptyResultDataAccessException) {
            exception.printStackTrace()
            return ResponseEntity<Any>("email=$email not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/email")
    fun findByEmail(@RequestBody email: String): ResponseEntity<Any> {
        val user = userService.findByEmail(email)
        return if (user == null || user.email.trim().isEmpty()) {
            ResponseEntity<Any>("missed param: email=$email not found", HttpStatus.NOT_ACCEPTABLE)
        } else ResponseEntity<Any>(user, HttpStatus.OK)
    }

    @PostMapping("/search")
    @Throws(ParseException::class)
    fun search(@RequestBody userSearchValues: UserSearchValues): ResponseEntity<Any> {
        // все заполненные условия проверяются условием ИЛИ
        // можно передавать не полный емайл, а любой текст для поиска
        val email = userSearchValues.email



        val sortColumn = userSearchValues.sortColumn
        val sortDirection = userSearchValues.sortDirection
        val pageNumber = userSearchValues.pageNumber
        val pageSize = userSearchValues.pageSize
        //TODO внимание! здесь возможен nullPointerException!!, для этого используем ЭЛВИС-Оператор
        val username = userSearchValues.username ?: "" // "Элвис" оператор, если username null, присвоется пустое значение
        // направление сортировки
        val direction = if (sortDirection.trim().isEmpty() || sortDirection.trim() == "asc") Sort.Direction.ASC else Sort.Direction.DESC
        // объект сортировки, который содержит столбец и направление
        val sort = Sort.by(direction, sortColumn, ID_COLUMN)
        // объект постраничного отображения
        val pageRequest = PageRequest.of(pageNumber, pageSize, sort)
        // результат запроса с постраничным выводом
        val result = userService.findByParams(email, username, pageRequest)
        return ResponseEntity.ok(result)
    }


}
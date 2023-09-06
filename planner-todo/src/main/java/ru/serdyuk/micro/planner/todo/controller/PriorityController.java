package ru.serdyuk.micro.planner.todo.controller;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.serdyuk.micro.planner.entity.Priority;
import ru.serdyuk.micro.planner.todo.search.PrioritySearchValues;
import ru.serdyuk.micro.planner.todo.service.PriorityService;
import ru.serdyuk.micro.planner.utils.resttemplate.UserRestBuilder;
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder;

import java.util.List;
import java.util.NoSuchElementException;

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
public class PriorityController {

    private final PriorityService priorityService;

    private UserRestBuilder userRestBuilder;

    private final UserWebClientBuilder userWebClientBuilder;

    public PriorityController(PriorityService priorityService, UserWebClientBuilder userWebClientBuilder) {
        this.priorityService = priorityService;
        this.userWebClientBuilder = userWebClientBuilder;
    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody Long userId) {
        return priorityService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        // проверка на обязательные параметры
        if (priority.getId() != null && priority.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть ошибка
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        // если передали пустое значение title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be null...", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userWebClientBuilder.userExists(priority.getUserId())) {
            return ResponseEntity.ok(priorityService.add(priority));
        }

        return new  ResponseEntity("user id=" + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        //
        if (priority.getId() == null || priority.getId() == 0){
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        priorityService.update(priority);
        return new ResponseEntity(HttpStatus.OK);
    }

    // для удаления используем тип запроса delete и передаем id для удаеления
    // можно также использовать метод post и передавать id в теле запроса
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        //можно обойтись и без try-catch, тогда будет возвращаться полная ошибка
        try {
            priorityService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {
        if (prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }
        // поиск категорий пользователя по названию
        List<Priority> list = priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        Priority priority;
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
    }
}

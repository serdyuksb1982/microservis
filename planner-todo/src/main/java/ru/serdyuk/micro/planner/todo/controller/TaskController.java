package ru.serdyuk.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.serdyuk.micro.planner.entity.Task;
import ru.serdyuk.micro.planner.todo.search.TaskSearchValues;
import ru.serdyuk.micro.planner.todo.service.TaskService;
import ru.serdyuk.micro.planner.utils.resttemplate.UserRestBuilder;
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")// basic URI
public class TaskController {

    public static final String ID_COLUMN = "id";
    private final TaskService taskService;

    private UserRestBuilder userRestBuilder;

    private final UserWebClientBuilder userWebClientBuilder;

    public TaskController(TaskService taskService, UserWebClientBuilder userWebClientBuilder) {
        this.taskService = taskService;
        this.userWebClientBuilder = userWebClientBuilder;
    }

    //получение всех данных
    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(taskService.findAll(userId));
    }

    // добавление данных
    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        // проверка на обязательные параметры
        if(task.getId() != null && task.getId() != 0) {
            // id создается в БД автоматически (autoincrement)
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userWebClientBuilder.userExists(task.getUserId())) {
            return ResponseEntity.ok(taskService.add(task));
        }

        return new  ResponseEntity("user id=" + task.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);// возвращаем ошибку, если нет юзера
    }

    // обновление
    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        // проверка на обязательные параметры
        if(task.getId() == null && task.getId() == 0) {
            return new ResponseEntity("redundant param: id ", HttpStatus.NOT_ACCEPTABLE);
        }
        // если передали пустое значение title
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        taskService.update(task);
        return new  ResponseEntity(HttpStatus.OK);
    }

    // удаление
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            taskService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // получение объекта по id
    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        Task task = null;
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) throws ParseException {
        // сначала считываем TaskSearchValues, берем оттуда все значения и сохраняем в локальные переменные, заодно проводим проверку
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1 ? true : false;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber();
        Integer pageSize = taskSearchValues.getPageSize();

        Long userId = taskSearchValues.getUserId();

        // проверка на обязательные параметры
        if (userId == null || userId == 0) {
            return new ResponseEntity("missed param: user Id.", HttpStatus.NOT_ACCEPTABLE);
        }

        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00-00 до 23-59

        Date dateFrom = null;
        Date dateTo = null;

        // выставить 00:00 для начальной даты (если она указана)
        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 1);
            calendarFrom.set(Calendar.SECOND, 1);
            calendarFrom.set(Calendar.MILLISECOND, 1);

            dateFrom = calendarFrom.getTime();
        }

        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.getDateTo() != null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime();
        }

        // направление сортировки
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // объект сортировки, который содержит столбец и направление сортировки
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
        // объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        // результат запроса с постраничным выводом

        Page<Task> result = taskService.findByParams(title, completed, priorityId, categoryId, userId, dateFrom, dateTo, pageRequest);
        // результат запрсоса
        return ResponseEntity.ok(result);
    }
}

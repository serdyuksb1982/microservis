package ru.serdyuk.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.serdyuk.micro.planner.entity.Category;
import ru.serdyuk.micro.planner.entity.User;
import ru.serdyuk.micro.planner.todo.feign.UserFeignClient;
import ru.serdyuk.micro.planner.todo.search.CategorySearchValues;
import ru.serdyuk.micro.planner.todo.service.CategoryService;
import ru.serdyuk.micro.planner.utils.resttemplate.UserRestBuilder;
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final UserRestBuilder userRestBuilder;

    private final UserWebClientBuilder userWebClientBuilder;

    private final UserFeignClient userFeignClient;

    public CategoryController(CategoryService categoryService, UserRestBuilder userRestBuilder, UserWebClientBuilder userWebClientBuilder, UserFeignClient userFeignClient) {
        this.categoryService = categoryService;
        this.userRestBuilder = userRestBuilder;
        this.userWebClientBuilder = userWebClientBuilder;
        this.userFeignClient = userFeignClient;
    }


    @PostMapping("/all")
    public List<Category> findAll(@RequestBody Long userId) {
        return categoryService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        // проверка на обязательные параметры
        if (category.getId() != null && category.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть ошибка
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        // если передали пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be null...", HttpStatus.NOT_ACCEPTABLE);
        }

        /*if (userWebClientBuilder.userExists(category.getUserId())) {
            return ResponseEntity.ok(categoryService.add(category));
        }
        return new  ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);*/

        // подписка на результат
        /*userWebClientBuilder.userExistsAsync(category.getUserId()).subscribe(user -> System.out.println("user = " + user));
        return new  ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);*/
        // вызов мс через feign interface

        ResponseEntity<User> result = userFeignClient.findUserById(category.getUserId());

        if (result == null) {
            return new ResponseEntity("система пользователей недоступна, попробуйте позже!", HttpStatus.NOT_FOUND);
        }

        if (result.getBody() != null) { //если current User не пустой
            return ResponseEntity.ok(categoryService.add(category));
        }
        return new  ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {
        //
        if (category.getId() == null || category.getId() == 0){
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK);
    }

    // для удаления используем тип запроса delete и передаем id для удаеления
    // можно также использовать метод post и передавать id в теле запроса
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        //можно обойтись и без try-catch, тогда будет возвращаться полная ошибка
        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // поиск по любым параметрам CategorySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {
        if (categorySearchValues.getUserId() == null || categorySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: user Id", HttpStatus.NOT_ACCEPTABLE);
        }
        // поиск категорий пользователя по названию
        List<Category> list = categoryService.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getUserId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Category category = null;
        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }
}

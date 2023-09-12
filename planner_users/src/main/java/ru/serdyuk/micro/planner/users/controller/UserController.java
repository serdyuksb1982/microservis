package ru.serdyuk.micro.planner.users.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.serdyuk.micro.planner.entity.User;
import ru.serdyuk.micro.planner.users.mq.func.MessageFuncActions;
import ru.serdyuk.micro.planner.users.search.UserSearchValues;
import ru.serdyuk.micro.planner.users.service.UserService;
import ru.serdyuk.micro.planner.utils.webclient.UserWebClientBuilder;

import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final String ID_COLUMN = "id";
    private final UserService userService;

    public UserWebClientBuilder userWebClientBuilder;

    public MessageFuncActions messageFuncActions;

    public UserController(UserService userService, UserWebClientBuilder userWebClientBuilder, MessageFuncActions messageFuncActions) {
        this.userService = userService;
        this.userWebClientBuilder = userWebClientBuilder;
        this.messageFuncActions = messageFuncActions;
    }


    // add user
    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        //проверка на обязательные параметры
        if (user.getId() != null && user.getId() != 0) {
            // id создается автоинкрементально автоматически JPA
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: userName", HttpStatus.NOT_ACCEPTABLE);
        }

        // adding user
        user = userService.add(user);
        /*if (user != null) {
            // заполнение начальными данными пользователя (в параллельном потоке!!!!)
            userWebClientBuilder.initUserDataLoading(user.getId()).subscribe(result -> {
                System.out.println("user populated: " + result);
            });
            return ResponseEntity.ok(user);
        }*/

       /* if (user != null) {// если пользователь добавился
            messageProducer.initUserData(user.getId());}*/
        if (user != null) {
            messageFuncActions.sendNewUserMessage(user.getId());
        }

        return ResponseEntity.ok(user);
    }

    // получение объекта по id
    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long id) {
        Optional<User> userOptional = userService.findById(id);//получаем контейнер Optional
        try {
            if (userOptional.isPresent()) {//если объект существует в контейнере, то получаем его методом get()
                return ResponseEntity.ok(userOptional.get());
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();

        }
        return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: user name", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(userService.update(user));
    }

    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody Long userId) {
        try {
            userService.deleteByUserId(userId);
        } catch (EmptyResultDataAccessException exception) {
            exception.printStackTrace();
            return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
        try {
            userService.deleteByEmail(email);
        } catch (EmptyResultDataAccessException exception) {
            exception.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<User> findByEmail(@RequestBody String email) {
        User user = userService.findByEmail(email);
        if (user == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) throws ParseException {

        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;

        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

        /*if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("missed param: user email", HttpStatus.NOT_ACCEPTABLE);
        }*/

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber();
        Integer pageSize = userSearchValues.getPageSize();

        Sort.Direction direction = sortDirection == null
                || sortDirection.trim().length() == 0
                || sortDirection.trim().equals("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(
                direction,
                sortColumn,
                ID_COLUMN
        );

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> result = userService.findByParams(email, username, pageRequest);
        return ResponseEntity.ok(result);
    }
}

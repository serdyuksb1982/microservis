package ru.serdyuk.micro.planner.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.serdyuk.micro.planner.entity.User;
import ru.serdyuk.micro.planner.users.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User add(User user) {
        return userRepository.save(user); // save in repository JPA как обновляет уже сущ. объект, так и создает новый...
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteByUserId(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> findByParams(String username, String password, PageRequest paqing) {
        return userRepository.findByParams(username, password, paqing);
    }

}

package ru.serdyuk.micro.planner.users.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.serdyuk.micro.planner.entity.User
import ru.serdyuk.micro.planner.users.repo.UserRepository
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
open class UserService(private val userRepository: UserRepository) {

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun add(user: User): User {
        return userRepository.save(user) // save in repository JPA как обновляет уже сущ. объект, так и создает новый...
    }

    fun update(user: User): User {
        return userRepository.save(user)
    }

    fun deleteByUserId(id: Long) {
        userRepository.deleteById(id)
    }

    fun deleteByEmail(email: String) {
        userRepository.deleteByEmail(email)
    }

    fun findById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun findByParams(username: String?,email: String, paqing: PageRequest): Page<User?> {
        return userRepository.findByParams(username, email, paqing)
    }
}
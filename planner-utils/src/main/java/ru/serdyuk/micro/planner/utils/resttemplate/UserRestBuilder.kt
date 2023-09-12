package ru.serdyuk.micro.planner.utils.resttemplate

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.serdyuk.micro.planner.entity.User

@Component
class UserRestBuilder {

    companion object {
        private const val baseUrl = "http://localhost:8765/planner-users/user/"
    }

    fun userExists(userId: Long): Boolean {
        val restTemplate = RestTemplate()
        val request = HttpEntity(userId)
        var response: ResponseEntity<User?>? = null
        try {
            response = restTemplate.exchange("$baseUrl/id", HttpMethod.POST, request, User::class.java)
            if (response.statusCode == HttpStatus.OK) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}
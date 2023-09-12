package ru.serdyuk.micro.planner.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_data", schema = "users", catalog = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val email: String? = null
    val username: String? = null

    @Column(name = "userpassword")
    val password: String? = null

    /*@ManyToMany(mappedBy = "users")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val roles: Set<Role>? = null*/
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return id == user.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "User{" +
                "username='" + username + '\'' +
                '}'
    }
}
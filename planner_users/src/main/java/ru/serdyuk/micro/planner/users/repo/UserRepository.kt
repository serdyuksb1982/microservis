package ru.serdyuk.micro.planner.users.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.serdyuk.micro.planner.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {//User not null!, id not null!

    fun findByEmail(Email: String): User? //email not null!, return User must be null

    fun deleteByEmail(email: String)

    @Query(
        "SELECT u FROM User u WHERE " +
        "(:email is null or :email='' or lower(u.email) like lower(concat('%', :email, '%') ) ) " +  // and - означает выборку обоих условий (email and username), or - один из двух параметров
        "and " +
        "(:username is null or :username='' or lower(u.username) like lower(concat('%', :username, '%') ) )")
    //функция поиска по всем переданным параметрам(пустые параметры не будут учитываться
    fun findByParams(
                    @Param("username") username: String?,
                    @Param("email") email: String,

                    pageable: Pageable
    ): Page<User?>
}
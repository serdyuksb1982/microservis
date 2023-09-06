package ru.serdyuk.micro.planner.users.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.serdyuk.micro.planner.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String Email);

    void deleteByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:email is null or :email='' or lower(u.email) like lower(concat('%', :email, '%') ) ) " +
            // and - означает выборку обоих условий (email and username), or - один из двух параметров
            "and " +
            "(:username is null or :username='' or lower(u.username) like lower(concat('%', :username, '%') ) )"
    )
    Page<User> findByParams(@Param("email") String email,
                            @Param("username") String username,
                            Pageable pageable
    );
}

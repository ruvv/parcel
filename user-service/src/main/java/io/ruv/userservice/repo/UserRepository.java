package io.ruv.userservice.repo;


import io.ruv.userservice.api.UserRole;
import io.ruv.userservice.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from users u where u.username = :username and :role member u.roles")
    Optional<User> findOneByUsernameAndHasRole(@Param("username") String username,
                                               @Param("role") UserRole role);

    @Query("select u from users u where :role member u.roles")
    List<User> findByHasRole(@Param("role") UserRole role, Pageable pageable);
}

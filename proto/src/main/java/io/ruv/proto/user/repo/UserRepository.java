package io.ruv.proto.user.repo;

import io.ruv.proto.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from users u where :role member u.roles")
//            countQuery = "select count(*) from user where ARRAY_CONTAINS(roles, :role)",
//            value = "select * from user where ARRAY_CONTAINS(roles, :role)")
    List<User> findByHasRole(@Param("role") String role, Pageable pageable);
}

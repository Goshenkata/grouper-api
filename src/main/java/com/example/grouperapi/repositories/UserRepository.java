package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//    @Query("select e from User e where e.username like concat('%', :query, '%')")
//    @Query("select e from User e")
//    List<User> getQueryResult(String query);
}

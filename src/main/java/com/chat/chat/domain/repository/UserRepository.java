package com.chat.chat.domain.repository;

import com.chat.chat.domain.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByName(String name);
   boolean existsByName(String name);

   // TODO
}

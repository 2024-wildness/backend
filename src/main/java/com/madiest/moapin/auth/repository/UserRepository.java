package com.madiest.moapin.auth.repository;

import com.madiest.moapin.auth.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for User entity database operations. */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}

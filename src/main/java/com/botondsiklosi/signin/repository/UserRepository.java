package com.botondsiklosi.signin.repository;

import com.botondsiklosi.signin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

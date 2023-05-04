package com.apply.banking.repository;

import com.apply.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByLastname(String lastname);

    Optional<User> findByEmail(String email);
}

package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    long countByRole(String role);

}
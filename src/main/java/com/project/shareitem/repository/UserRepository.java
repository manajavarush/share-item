package com.project.shareitem.repository;

import com.project.shareitem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String userEmail);
}

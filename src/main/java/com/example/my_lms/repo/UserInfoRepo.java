package com.example.my_lms.repo;

import com.example.my_lms.model.userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<userinfo, Long> {
    Optional<userinfo> findByUsername(String username);
}

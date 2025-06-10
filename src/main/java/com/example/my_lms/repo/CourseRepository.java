package com.example.my_lms.repo;

import com.example.my_lms.model.Course; // Import Course instead of Post
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom query methods can be added here if needed
}

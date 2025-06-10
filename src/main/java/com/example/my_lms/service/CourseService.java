package com.example.my_lms.service;

import com.example.my_lms.model.Course;
import com.example.my_lms.model.userinfo;
import com.example.my_lms.repo.CourseRepository;
import com.example.my_lms.repo.UserInfoRepo;
import jakarta.transaction.Transactional;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private UserInfoRepo userInfoRepository;

    @Autowired
    private CourseRepository courseRepository;


    // Method to get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Method to get a course by its ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public void addCourseInDb(Course course)
    {
        courseRepository.save(course);
    }
    public List<Course> getCoursesByUserId(Long userId) {
        userinfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getCourses());
    }

    // Fetch courses for a user by their username
    public List<Course> getCoursesByUsername(String username) {
        // Find user by username and return their courses
        userinfo user = userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getCourses()); // Return the courses associated with the user
    }


    // Method to add a course to a user
    @Transactional
    public void addCourseToUser(Long userId, Long courseId) {
        userinfo user = userInfoRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if the user is already enrolled in the course
        if (!user.getCourses().contains(course)) {
            user.getCourses().add(course); // Update user-course relationship
            userInfoRepository.save(user); // Save the user, Hibernate will handle the join table
        } else {
            System.out.println("Relationship already exists");;
        }
    }


    // Method to edit course details
    public Optional<Course> editCourse(Long id, Course course) {
        // Find the course by ID
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            // Get the existing course from the database
            Course existingCourse = optionalCourse.get();

            // Update the existing course with the new course details
            existingCourse.setName(course.getName());
            existingCourse.setDescription(course.getDescription());
            existingCourse.setInstructor(course.getInstructor());

            // Save the updated course
            courseRepository.save(existingCourse);

            // Return the updated course
            return Optional.of(existingCourse);
        } else {
            // Return an empty Optional if the course is not found
            return Optional.empty();
        }
    }

    // Method to delete a course by its ID
    public void deleteCourseById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            // Remove the course from all users' course list
            for (userinfo user : course.getUsers()) {
                user.getCourses().remove(course);
                userInfoRepository.save(user); // Save user with updated course list
            }

            // Remove the course from the course repository
            courseRepository.delete(course);
        } else {
            throw new RuntimeException("Course not found");
        }
    }

    // Method to remove a course from a user
    public Optional<Course> removeCourseFromUser(Long userId, Long courseId) {
        userinfo user = userInfoRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        if (user.getCourses().contains(course)) {  // Check if the user is actually enrolled in the course
            // Remove the course from the user's course list
            user.getCourses().remove(course);

            // Remove the user from the course's user list (bidirectional update)
            course.getUsers().remove(user);

            // Save changes to both entities
            userInfoRepository.save(user);
            courseRepository.save(course);
        }

        return Optional.of(course);
    }


}

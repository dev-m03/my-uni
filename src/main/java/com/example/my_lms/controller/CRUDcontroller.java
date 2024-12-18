package com.example.my_lms.controller;

import com.example.my_lms.model.Course;
import com.example.my_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CRUDcontroller {
    @Autowired
    private CourseService service;

    @GetMapping("/courses")
    public List<Course> getAllCourses()
    {
        return service.getAllCourses();
    }
    @GetMapping("/courses/{id}")
    public Optional<Course> getCourseById(@PathVariable Long id)
    {
        return service.getCourseById(id);
    }
    @GetMapping("/user/courses/{userId}")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        List<Course> courses = service.getCoursesByUserId(userId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/add-course")
    public void addCourseInDb(@RequestBody Course course)
    {
         service.addCourseInDb(course);
    }
    @PostMapping("/user/add-course/{userId}/{courseId}")
    public ResponseEntity<Void> addCourseToUser(@PathVariable Long userId, @PathVariable Long courseId) {
        try {

            service.addCourseToUser(userId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // Return 201 status if course is added successfully
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if user or course not found
        }
    }

    @PutMapping("/course/update-course/{id}")
    public Optional<Course> editCourse(@PathVariable Long Id, @RequestBody Course course) {
        return service.editCourse(Id,course);


    }
    @DeleteMapping("/delete-course/{id}")
    public void deleteCourseById(@PathVariable Long id){
         service.deleteCourseById(id);
    }
    @DeleteMapping("/user/delete-course/{userId}/{courseId}")
    public ResponseEntity<Course> removeCourseFromUser(@PathVariable Long userId, @PathVariable Long courseId) {
        Optional<Course> course = service.removeCourseFromUser(userId, courseId);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}


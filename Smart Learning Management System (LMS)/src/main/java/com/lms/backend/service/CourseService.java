package com.lms.backend.service;

import com.lms.backend.dto.CourseDto;
import com.lms.backend.entity.Course;
import com.lms.backend.entity.Role;
import com.lms.backend.entity.User;
import com.lms.backend.exception.BadRequestException;
import com.lms.backend.exception.ResourceNotFoundException;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // Retrieve courses with searching, pagination, sorting (cached)
    @Cacheable(value = "courses", key = "{#category, #keyword, #pageable.pageNumber, #pageable.pageSize, #pageable.sort.toString()}")
    @Transactional(readOnly = true)
    public Page<CourseDto> searchCourses(String category, String keyword, Pageable pageable) {
        Page<Course> coursePage = courseRepository.searchCourses(category, keyword, pageable);
        return coursePage.map(this::convertToDto);
    }

    // Retrieve course by ID (cached)
    @Cacheable(value = "course", key = "#id")
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return convertToDto(course);
    }

    // Create course (evicts cache)
    @CacheEvict(value = {"courses", "course"}, allEntries = true)
    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        // Validate Instructor
        User instructor = userRepository.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", courseDto.getInstructorId()));

        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new BadRequestException("User with ID " + courseDto.getInstructorId() + " is not an INSTRUCTOR");
        }

        Course course = new Course(
                courseDto.getTitle(),
                courseDto.getDescription(),
                courseDto.getCategory(),
                courseDto.getInstructorId(),
                courseDto.getDifficultyLevel()
        );

        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    // Update course (evicts cache)
    @CacheEvict(value = {"courses", "course"}, allEntries = true)
    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        // Validate Instructor if updated
        User instructor = userRepository.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", courseDto.getInstructorId()));

        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new BadRequestException("User with ID " + courseDto.getInstructorId() + " is not an INSTRUCTOR");
        }

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCategory(courseDto.getCategory());
        course.setInstructorId(courseDto.getInstructorId());
        course.setDifficultyLevel(courseDto.getDifficultyLevel());

        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    // Delete course (evicts cache)
    @CacheEvict(value = {"courses", "course"}, allEntries = true)
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
    }

    // Map entity to DTO
    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto(course);
        // Set Instructor name
        userRepository.findById(course.getInstructorId()).ifPresent(user -> {
            dto.setInstructorName(user.getName());
        });
        return dto;
    }
}

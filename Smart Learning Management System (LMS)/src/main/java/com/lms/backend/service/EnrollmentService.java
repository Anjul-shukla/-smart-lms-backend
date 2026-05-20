package com.lms.backend.service;

import com.lms.backend.dto.EnrollmentDto;
import com.lms.backend.entity.*;
import com.lms.backend.exception.BadRequestException;
import com.lms.backend.exception.ResourceNotFoundException;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.EnrollmentRepository;
import com.lms.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    // Enroll a student in a course
    @Transactional
    public EnrollmentDto enrollStudent(Long studentId, Long courseId) {
        // Validate Student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("Only students can enroll in courses. User ID " + studentId + " is an " + student.getRole());
        }

        // Validate Course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        // Prevent Duplicate Enrollments
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new BadRequestException("Student is already enrolled in this course!");
        }

        Enrollment enrollment = new Enrollment(student, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return new EnrollmentDto(savedEnrollment);
    }

    // Unenroll a student from a course
    @Transactional
    public void unenrollStudent(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student " + studentId + " in course " + courseId));

        enrollmentRepository.delete(enrollment);
    }

    // Get all enrollments for a specific student
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getStudentEnrollments(Long studentId) {
        // Validate student exists
        if (!userRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }
        
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(EnrollmentDto::new)
                .collect(Collectors.toList());
    }

    // Update progress on a course
    @Transactional
    public EnrollmentDto updateProgress(Long studentId, Long courseId, Double progress) {
        if (progress < 0.0 || progress > 100.0) {
            throw new BadRequestException("Progress percentage must be between 0.0 and 100.0");
        }

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student " + studentId + " in course " + courseId));

        enrollment.setProgress(progress);
        
        if (progress >= 100.0) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
        } else {
            enrollment.setStatus(EnrollmentStatus.ACTIVE);
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return new EnrollmentDto(updatedEnrollment);
    }

    // Get all enrollments for an instructor's courses
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getInstructorEnrollments(Long instructorId) {
        // Validate Instructor
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", instructorId));

        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new BadRequestException("User ID " + instructorId + " is not an INSTRUCTOR");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseInstructorId(instructorId);
        return enrollments.stream()
                .map(EnrollmentDto::new)
                .collect(Collectors.toList());
    }
}

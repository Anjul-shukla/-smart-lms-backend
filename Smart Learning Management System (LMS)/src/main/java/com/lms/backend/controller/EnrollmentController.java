package com.lms.backend.controller;

import com.lms.backend.dto.EnrollmentDto;
import com.lms.backend.security.UserPrincipal;
import com.lms.backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // POST /api/enroll/{courseId} - Enroll in a course (Student only)
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        EnrollmentDto enrollmentDto = enrollmentService.enrollStudent(userPrincipal.getId(), courseId);
        return new ResponseEntity<>(enrollmentDto, HttpStatus.CREATED);
    }

    // DELETE /api/enroll/{courseId} - Unenroll from a course (Student only)
    @DeleteMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> unenrollStudent(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        enrollmentService.unenrollStudent(userPrincipal.getId(), courseId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully unenrolled from course ID: " + courseId);
        return ResponseEntity.ok(response);
    }

    // GET /api/student/enrollments - View enrolled courses for currently logged-in student
    @GetMapping("/student/enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollments(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments(userPrincipal.getId());
        return ResponseEntity.ok(enrollments);
    }

    // PUT /api/student/enrollments/{courseId}/progress - Update progress on an enrollment
    @PutMapping("/student/enrollments/{courseId}/progress")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<EnrollmentDto> updateProgress(
            @PathVariable Long courseId,
            @RequestParam Double progress,
            @RequestParam(required = false) Long studentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long targetStudentId = studentId;
        if (targetStudentId == null) {
            targetStudentId = userPrincipal.getId();
        } else {
            // Check authorization: if student is updating another student's ID, forbid it
            boolean isStudent = userPrincipal.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            if (isStudent && !userPrincipal.getId().equals(targetStudentId)) {
                throw new org.springframework.security.access.AccessDeniedException("Students can only modify their own progress.");
            }
        }

        EnrollmentDto updatedEnrollment = enrollmentService.updateProgress(targetStudentId, courseId, progress);
        return ResponseEntity.ok(updatedEnrollment);
    }

    // GET /api/instructor/enrollments - View enrolled students in instructor's courses
    @GetMapping("/instructor/enrollments")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<List<EnrollmentDto>> getInstructorEnrollments(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<EnrollmentDto> enrollments = enrollmentService.getInstructorEnrollments(userPrincipal.getId());
        return ResponseEntity.ok(enrollments);
    }
}

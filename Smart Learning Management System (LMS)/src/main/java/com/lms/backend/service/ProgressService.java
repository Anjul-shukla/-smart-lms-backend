package com.lms.backend.service;

import com.lms.backend.dto.EnrollmentDto;
import com.lms.backend.dto.ProgressDto;
import com.lms.backend.entity.Enrollment;
import com.lms.backend.entity.EnrollmentStatus;
import com.lms.backend.entity.Role;
import com.lms.backend.entity.User;
import com.lms.backend.exception.BadRequestException;
import com.lms.backend.exception.ResourceNotFoundException;
import com.lms.backend.repository.EnrollmentRepository;
import com.lms.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProgressService(EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProgressDto calculateStudentProgress(Long studentId) {
        // Validate Student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("User ID " + studentId + " is not a STUDENT");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        int enrolledCount = enrollments.size();
        int completedCount = 0;
        double sumProgress = 0.0;

        for (Enrollment enrollment : enrollments) {
            sumProgress += enrollment.getProgress();
            if (enrollment.getStatus() == EnrollmentStatus.COMPLETED || enrollment.getProgress() >= 100.0) {
                completedCount++;
            }
        }

        double averageProgress = enrolledCount == 0 ? 0.0 : sumProgress / enrolledCount;

        // Performance Score Formula: (completed courses * 50) + (average progress * 0.5)
        double performanceScore = (completedCount * 50.0) + (averageProgress * 0.5);

        // Round performance score and average progress to 2 decimal places
        performanceScore = Math.round(performanceScore * 100.0) / 100.0;
        averageProgress = Math.round(averageProgress * 100.0) / 100.0;

        // Activity Summary
        String activitySummary = String.format("Student %s is enrolled in %d courses, has completed %d, with an average progress of %.1f%%.",
                student.getName(), enrolledCount, completedCount, averageProgress);

        List<EnrollmentDto> enrollmentDtos = enrollments.stream()
                .map(EnrollmentDto::new)
                .collect(Collectors.toList());

        return new ProgressDto(
                studentId,
                student.getName(),
                student.getEmail(),
                enrolledCount,
                completedCount,
                averageProgress,
                performanceScore,
                activitySummary,
                enrollmentDtos
        );
    }
}

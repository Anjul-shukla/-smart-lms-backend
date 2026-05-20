package com.lms.backend.repository;

import com.lms.backend.entity.Enrollment;
import com.lms.backend.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStudentId(Long studentId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<Enrollment> findByCourseInstructorId(Long instructorId);
}

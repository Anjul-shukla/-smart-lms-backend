package com.lms.backend.repository;

import com.lms.backend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
           "(:category IS NULL OR LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Course> searchCourses(
            @Param("category") String category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    List<Course> findByInstructorId(Long instructorId);
}

package com.lms.backend.dto;

import com.lms.backend.entity.Enrollment;
import com.lms.backend.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public class EnrollmentDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseTitle;
    private String courseCategory;
    private Double progress;
    private EnrollmentStatus status;
    private LocalDateTime enrolledDate;

    // Constructors
    public EnrollmentDto() {
    }

    public EnrollmentDto(Enrollment enrollment) {
        this.id = enrollment.getId();
        if (enrollment.getStudent() != null) {
            this.studentId = enrollment.getStudent().getId();
            this.studentName = enrollment.getStudent().getName();
            this.studentEmail = enrollment.getStudent().getEmail();
        }
        if (enrollment.getCourse() != null) {
            this.courseId = enrollment.getCourse().getId();
            this.courseTitle = enrollment.getCourse().getTitle();
            this.courseCategory = enrollment.getCourse().getCategory();
        }
        this.progress = enrollment.getProgress();
        this.status = enrollment.getStatus();
        this.enrolledDate = enrollment.getEnrolledDate();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }
}

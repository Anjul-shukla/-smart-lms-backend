package com.lms.backend.dto;

import java.util.List;

public class ProgressDto {
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Integer enrolledCoursesCount;
    private Integer completedCoursesCount;
    private Double averageProgress;
    private Double performanceScore;
    private String activitySummary;
    private List<EnrollmentDto> enrollments;

    // Constructors
    public ProgressDto() {
    }

    public ProgressDto(Long studentId, String studentName, String studentEmail, Integer enrolledCoursesCount,
                       Integer completedCoursesCount, Double averageProgress, Double performanceScore,
                       String activitySummary, List<EnrollmentDto> enrollments) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.enrolledCoursesCount = enrolledCoursesCount;
        this.completedCoursesCount = completedCoursesCount;
        this.averageProgress = averageProgress;
        this.performanceScore = performanceScore;
        this.activitySummary = activitySummary;
        this.enrollments = enrollments;
    }

    // Getters and Setters
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

    public Integer getEnrolledCoursesCount() {
        return enrolledCoursesCount;
    }

    public void setEnrolledCoursesCount(Integer enrolledCoursesCount) {
        this.enrolledCoursesCount = enrolledCoursesCount;
    }

    public Integer getCompletedCoursesCount() {
        return completedCoursesCount;
    }

    public void setCompletedCoursesCount(Integer completedCoursesCount) {
        this.completedCoursesCount = completedCoursesCount;
    }

    public Double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(Double averageProgress) {
        this.averageProgress = averageProgress;
    }

    public Double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(Double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getActivitySummary() {
        return activitySummary;
    }

    public void setActivitySummary(String activitySummary) {
        this.activitySummary = activitySummary;
    }

    public List<EnrollmentDto> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<EnrollmentDto> enrollments) {
        this.enrollments = enrollments;
    }
}

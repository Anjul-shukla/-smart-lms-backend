package com.lms.backend.dto;

import com.lms.backend.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be under 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be under 1000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must be under 100 characters")
    private String category;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    @NotBlank(message = "Difficulty level is required")
    @Size(max = 50, message = "Difficulty level must be under 50 characters")
    private String difficultyLevel;

    private String instructorName; // Optional for response convenience

    // Constructors
    public CourseDto() {
    }

    public CourseDto(Long id, String title, String description, String category, Long instructorId, String difficultyLevel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.instructorId = instructorId;
        this.difficultyLevel = difficultyLevel;
    }

    public CourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.category = course.getCategory();
        this.instructorId = course.getInstructorId();
        this.difficultyLevel = course.getDifficultyLevel();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
}

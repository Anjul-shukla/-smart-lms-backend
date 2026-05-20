package com.lms.backend.controller;

import com.lms.backend.dto.ProgressDto;
import com.lms.backend.security.UserPrincipal;
import com.lms.backend.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // GET /api/student/{id}/progress - Fetch overall progress of student
    @GetMapping("/student/{id}/progress")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ProgressDto> getStudentProgress(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        // Security check: Students are only allowed to see their own academic analytics
        boolean isStudent = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        if (isStudent && !userPrincipal.getId().equals(id)) {
            throw new org.springframework.security.access.AccessDeniedException("Students can only access their own learning metrics.");
        }

        ProgressDto progressDto = progressService.calculateStudentProgress(id);
        return ResponseEntity.ok(progressDto);
    }
}

package com.lms.backend.controller;

import com.lms.backend.dto.ProgressDto;
import com.lms.backend.exception.BadRequestException;
import com.lms.backend.security.UserPrincipal;
import com.lms.backend.service.EmailService;
import com.lms.backend.service.ProgressService;
import com.lms.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final ProgressService progressService;
    private final EmailService emailService;

    @Autowired
    public ReportController(ReportService reportService, ProgressService progressService, EmailService emailService) {
        this.reportService = reportService;
        this.progressService = progressService;
        this.emailService = emailService;
    }

    // GET /api/student/{id}/report/pdf - Download progress report PDF (Student / Instructor / Admin)
    @GetMapping("/student/{id}/report/pdf")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<byte[]> getPdfReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        // Security check: Students are only allowed to see their own academic analytics
        boolean isStudent = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        if (isStudent && !userPrincipal.getId().equals(id)) {
            throw new org.springframework.security.access.AccessDeniedException("Students can only download their own progress reports.");
        }

        try (ByteArrayInputStream bis = reportService.generateProgressReportPdf(id)) {
            byte[] pdfBytes = bis.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "LMS_Progress_Report_Student_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (IOException e) {
            throw new BadRequestException("Error occurred while generating progress PDF bytes: " + e.getMessage());
        }
    }

    // GET /api/student/{id}/report/email - Dispatch progress report email (Student / Instructor / Admin)
    @GetMapping("/student/{id}/report/email")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, String>> emailPdfReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        // Security check: Students are only allowed to see their own academic analytics
        boolean isStudent = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        if (isStudent && !userPrincipal.getId().equals(id)) {
            throw new org.springframework.security.access.AccessDeniedException("Students can only email their own progress reports.");
        }

        ProgressDto progress = progressService.calculateStudentProgress(id);

        try (ByteArrayInputStream bis = reportService.generateProgressReportPdf(id)) {
            byte[] pdfBytes = bis.readAllBytes();

            // Asynchronous email dispatch using @Async in EmailService
            emailService.sendReportEmailWithAttachment(progress.getStudentEmail(), progress.getStudentName(), pdfBytes);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Academic progress report is being generated and sent to " + progress.getStudentEmail() + " asynchronously.");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            throw new BadRequestException("Error occurred while capturing progress report PDF bytes: " + e.getMessage());
        }
    }
}

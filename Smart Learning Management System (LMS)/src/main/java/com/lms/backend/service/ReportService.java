package com.lms.backend.service;

import com.lms.backend.dto.EnrollmentDto;
import com.lms.backend.dto.ProgressDto;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    private final ProgressService progressService;

    @Autowired
    public ReportService(ProgressService progressService) {
        this.progressService = progressService;
    }

    public ByteArrayInputStream generateProgressReportPdf(Long studentId) {
        ProgressDto progress = progressService.calculateStudentProgress(studentId);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Custom fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(26, 35, 126));
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(26, 35, 126));
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

            // Document Header
            Paragraph title = new Paragraph("Smart LMS - Student Progress Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(4f);
            document.add(title);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Paragraph generatedOn = new Paragraph("Generated on: " + LocalDateTime.now().format(formatter), subtitleFont);
            generatedOn.setAlignment(Element.ALIGN_CENTER);
            generatedOn.setSpacingAfter(20f);
            document.add(generatedOn);

            // Add thin separation line
            Paragraph separator = new Paragraph("______________________________________________________________________________", subtitleFont);
            separator.setSpacingAfter(15f);
            document.add(separator);

            // Student Details Section
            Paragraph sectionTitleUser = new Paragraph("Student Profile Details", sectionTitleFont);
            sectionTitleUser.setSpacingAfter(10f);
            document.add(sectionTitleUser);

            PdfPTable userTable = new PdfPTable(2);
            userTable.setWidthPercentage(100);
            userTable.setSpacingAfter(20f);
            userTable.setWidths(new float[]{1, 3});

            addTableCell(userTable, "Student ID:", boldFont);
            addTableCell(userTable, String.valueOf(progress.getStudentId()), normalFont);

            addTableCell(userTable, "Student Name:", boldFont);
            addTableCell(userTable, progress.getStudentName(), normalFont);

            addTableCell(userTable, "Email Address:", boldFont);
            addTableCell(userTable, progress.getStudentEmail(), normalFont);

            addTableCell(userTable, "Performance Score:", boldFont);
            addTableCell(userTable, String.valueOf(progress.getPerformanceScore()), normalFont);

            addTableCell(userTable, "Enrolled Courses:", boldFont);
            addTableCell(userTable, String.valueOf(progress.getEnrolledCoursesCount()), normalFont);

            addTableCell(userTable, "Completed Courses:", boldFont);
            addTableCell(userTable, String.valueOf(progress.getCompletedCoursesCount()), normalFont);

            addTableCell(userTable, "Average Progress:", boldFont);
            addTableCell(userTable, progress.getAverageProgress() + "%", normalFont);

            document.add(userTable);

            // Enrolled Courses Table Section
            Paragraph sectionTitleCourses = new Paragraph("Course-wise Academic Progress", sectionTitleFont);
            sectionTitleCourses.setSpacingAfter(10f);
            document.add(sectionTitleCourses);

            if (progress.getEnrollments().isEmpty()) {
                Paragraph noCourses = new Paragraph("No courses enrolled currently.", normalFont);
                noCourses.setSpacingAfter(20f);
                document.add(noCourses);
            } else {
                PdfPTable coursesTable = new PdfPTable(4);
                coursesTable.setWidthPercentage(100);
                coursesTable.setSpacingAfter(25f);
                coursesTable.setWidths(new float[]{3, 2, 2, 2});

                // Headers
                addTableHeaderCell(coursesTable, "Course Title", tableHeaderFont);
                addTableHeaderCell(coursesTable, "Category", tableHeaderFont);
                addTableHeaderCell(coursesTable, "Progress (%)", tableHeaderFont);
                addTableHeaderCell(coursesTable, "Status", tableHeaderFont);

                for (EnrollmentDto enrollment : progress.getEnrollments()) {
                    addTableCell(coursesTable, enrollment.getCourseTitle(), normalFont);
                    addTableCell(coursesTable, enrollment.getCourseCategory(), normalFont);
                    addTableCell(coursesTable, String.format("%.1f%%", enrollment.getProgress()), normalFont);
                    addTableCell(coursesTable, enrollment.getStatus().toString(), normalFont);
                }

                document.add(coursesTable);
            }

            // Achievements Badge Section
            Paragraph sectionTitleBadge = new Paragraph("Achievements & Accolades", sectionTitleFont);
            sectionTitleBadge.setSpacingAfter(10f);
            document.add(sectionTitleBadge);

            Paragraph badgeText = new Paragraph();
            badgeText.setFont(normalFont);
            badgeText.setSpacingAfter(20f);

            boolean hasBadges = false;
            if (progress.getPerformanceScore() >= 100.0) {
                badgeText.add("🏅 [GOLD BADGE] - Achieved a performance score greater than or equal to 100!\n");
                hasBadges = true;
            }
            if (progress.getCompletedCoursesCount() >= 2) {
                badgeText.add("🎓 [ELITE GRADUATE] - Successfully completed at least 2 courses!\n");
                hasBadges = true;
            }
            if (progress.getAverageProgress() >= 75.0 && progress.getEnrolledCoursesCount() > 0) {
                badgeText.add("⭐ [HIGH ACHIEVER] - Maintained an average progress above 75%!\n");
                hasBadges = true;
            }

            if (!hasBadges) {
                badgeText.add("🌱 [ACADEMIC BEGINNER] - Complete courses and increase average progress to earn elite badges.");
            }

            document.add(badgeText);

            // Summary Remarks Section
            Paragraph sectionRemarks = new Paragraph("Summary Academic Remarks", sectionTitleFont);
            sectionRemarks.setSpacingAfter(10f);
            document.add(sectionRemarks);

            String remarks;
            if (progress.getEnrolledCoursesCount() == 0) {
                remarks = "Student has not enrolled in any courses yet. Please browse the catalog to start learning.";
            } else if (progress.getAverageProgress() >= 80.0) {
                remarks = "Excellent performance! The student is showcasing outstanding learning dedication, maintaining top-tier course engagement and completing assignments on time.";
            } else if (progress.getAverageProgress() >= 50.0) {
                remarks = "Good progress. The student is consistently advancing, but is encouraged to dedicate extra weekly time to finish active modules.";
            } else {
                remarks = "Attention required. Course completion progress is currently low. Recommend review sessions and active feedback with instructors to regain momentum.";
            }

            Paragraph remarksParagraph = new Paragraph(remarks, normalFont);
            remarksParagraph.setSpacingAfter(30f);
            document.add(remarksParagraph);

            // Closing Footer
            Paragraph footer = new Paragraph("Thank you for choosing Smart LMS. Keep scaling up your skills!", subtitleFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(new Color(224, 224, 224));
        table.addCell(cell);
    }

    private void addTableHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(10f);
        cell.setBackgroundColor(new Color(26, 35, 126));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(new Color(26, 35, 126));
        table.addCell(cell);
    }
}

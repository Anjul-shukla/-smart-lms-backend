package com.lms.backend.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProjectDocumentationGenerator {

    public static void main(String[] args) {
        System.out.println("Generating complete project documentation PDF...");
        String destFile = "LMS_Backend_Complete_Documentation.pdf";

        Document document = new Document(PageSize.A4, 54, 54, 54, 54);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(destFile));
            document.open();

            // Styling colors
            Color primaryNavy = new Color(26, 35, 126);
            Color textGray = new Color(80, 80, 80);
            Color lightGray = new Color(245, 245, 245);
            Color borderGray = new Color(224, 224, 224);

            // Fonts
            Font mainTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, primaryNavy);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, textGray);
            Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, primaryNavy);
            Font subsectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, primaryNavy);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font codeFont = FontFactory.getFont(FontFactory.COURIER, 9, Color.DARK_GRAY);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

            // 1. Cover Page
            Paragraph spacer1 = new Paragraph(" ");
            spacer1.setSpacingAfter(100f);
            document.add(spacer1);

            Paragraph title = new Paragraph("Smart Learning Management System\n(LMS) Backend", mainTitleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            document.add(title);

            Paragraph subtitle = new Paragraph("Production-Style RESTful Backend Architecture & Implementation Blueprint", subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(40f);
            document.add(subtitle);

            Paragraph line = new Paragraph("____________________________________________________", subTitleFont);
            line.setAlignment(Element.ALIGN_CENTER);
            line.setSpacingAfter(40f);
            document.add(line);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            Paragraph metadata = new Paragraph(
                    "Prepared for: User Review\n" +
                    "Status: Completed & Compiled Successfully\n" +
                    "Date: " + LocalDateTime.now().format(formatter) + "\n" +
                    "Version: 1.0.0-SNAPSHOT\n\n" +
                    "Powered by: Spring Boot 3.1.5, Spring Security, JWT, MySQL & OpenPDF", 
                    FontFactory.getFont(FontFactory.HELVETICA, 10, textGray)
            );
            metadata.setAlignment(Element.ALIGN_CENTER);
            document.add(metadata);

            document.newPage(); // Move to next page for table of contents / sections

            // 2. Introduction & Project Goals
            Paragraph sec1Title = new Paragraph("1. Project Goal & Objectives", sectionTitleFont);
            sec1Title.setSpacingAfter(12f);
            document.add(sec1Title);

            Paragraph sec1Desc = new Paragraph(
                    "The Smart Learning Management System (LMS) Backend is an enterprise-grade backend infrastructure " +
                    "designed for online educational platforms. The architecture enables frictionless course administration, " +
                    "comprehensive enrollment control, automated tracking of student completion stages, secure role-based access mechanisms, " +
                    "and professional analytics reporting.\n\n" +
                    "Objectives completed:\n" +
                    " • Decentralized course management allowing instructors to perform full CRUD on courses.\n" +
                    " • Double-enrollment prevention guarding the transaction layer against duplicate student enrollments.\n" +
                    " • Automated progress tracking compiling completion percentages across courses.\n" +
                    " • Custom PDF report compilation containing scores, remarks, and dynamic achievements.\n" +
                    " • Multi-threaded SMTP mail notification providing immediate asynchronous PDF dispatch.",
                    normalFont
            );
            sec1Desc.setSpacingAfter(20f);
            document.add(sec1Desc);

            // 3. Technical Stack
            Paragraph sec2Title = new Paragraph("2. System Architecture & Tech Stack", sectionTitleFont);
            sec2Title.setSpacingAfter(12f);
            document.add(sec2Title);

            Paragraph sec2Desc = new Paragraph(
                    "The backend leverages the industry-standard Spring Boot framework following a strict layered architecture pattern " +
                    "(Controller -> Service -> Repository) to ensure modular development and easy scaling.",
                    normalFont
            );
            sec2Desc.setSpacingAfter(10f);
            document.add(sec2Desc);

            PdfPTable stackTable = new PdfPTable(2);
            stackTable.setWidthPercentage(100);
            stackTable.setSpacingAfter(20f);
            stackTable.setWidths(new float[]{2, 5});

            addTableHeaderCell(stackTable, "Technology Component", tableHeaderFont, primaryNavy);
            addTableHeaderCell(stackTable, "Version & Specific Role in LMS Backend", tableHeaderFont, primaryNavy);

            addTableCell(stackTable, "Java Platform", tableBodyFont);
            addTableCell(stackTable, "Java 17 (LTS) - Provides support for records, enhanced switch patterns, and modern performance improvements.", tableBodyFont);

            addTableCell(stackTable, "Spring Boot Framework", tableBodyFont);
            addTableCell(stackTable, "Spring Boot 3.1.5 - Provides auto-configurations for Web REST MVC, dependency injection, and JPA Hibernate ORM mapping.", tableBodyFont);

            addTableCell(stackTable, "Spring Security & JWT", tableBodyFont);
            addTableCell(stackTable, "Spring Security 6.0 & JSON Web Tokens (JJWT) - Handles stateless session filtering and robust Role-Based Access Control (RBAC).", tableBodyFont);

            addTableCell(stackTable, "Database Layer", tableBodyFont);
            addTableCell(stackTable, "MySQL 8.0 & Hibernate - Stores relational mappings for Users, Courses, and Enrollments with automated schema updates.", tableBodyFont);

            addTableCell(stackTable, "PDF Generation Engine", tableBodyFont);
            addTableCell(stackTable, "OpenPDF 1.3.30 (iText Open Source) - Compiles student records into beautiful, structured PDF canvases dynamically.", tableBodyFont);

            addTableCell(stackTable, "SMTP Email Dispatch", tableBodyFont);
            addTableCell(stackTable, "Spring Mail (JavaMailSender) - Constructs multipart MIME payloads to send PDF reports asynchronously via SMTP.", tableBodyFont);

            addTableCell(stackTable, "Caching Engine", tableBodyFont);
            addTableCell(stackTable, "Spring Starter Cache (ConcurrentMapCache) - Caches heavy catalog queries to reduce DB hits.", tableBodyFont);

            addTableCell(stackTable, "Asynchronous Execution", tableBodyFont);
            addTableCell(stackTable, "ThreadPoolTaskExecutor (@Async) - Offloads SMTP tasks into separate background threads to preserve HTTP thread pools.", tableBodyFont);

            document.add(stackTable);

            document.newPage();

            // 4. Database Schema
            Paragraph sec3Title = new Paragraph("3. Relational Schema & Database Design", sectionTitleFont);
            sec3Title.setSpacingAfter(12f);
            document.add(sec3Title);

            Paragraph sec3Desc = new Paragraph(
                    "The backend database is designed with normalized entities. The primary connection between students and courses is established " +
                    "via the 'enrollments' table, representing a many-to-many relationship with supplemental attributes.",
                    normalFont
            );
            sec3Desc.setSpacingAfter(12f);
            document.add(sec3Desc);

            Paragraph entity1 = new Paragraph("3.1 User Entity (users table)", subsectionTitleFont);
            entity1.setSpacingAfter(6f);
            document.add(entity1);
            Paragraph entity1Desc = new Paragraph(
                    "Stores system credentials and credentials metadata. The email is uniquely indexed.\n" +
                    " • id: BIGINT (Primary Key, GenerationType.IDENTITY)\n" +
                    " • name: VARCHAR(100) - Full name\n" +
                    " • email: VARCHAR(100) (Unique Index)\n" +
                    " • password: VARCHAR(120) - Hash coded using BCrypt Strength 10\n" +
                    " • role: VARCHAR(50) - Enumerated String (ADMIN, INSTRUCTOR, STUDENT)",
                    normalFont
            );
            entity1Desc.setSpacingAfter(12f);
            document.add(entity1Desc);

            Paragraph entity2 = new Paragraph("3.2 Course Entity (courses table)", subsectionTitleFont);
            entity2.setSpacingAfter(6f);
            document.add(entity2);
            Paragraph entity2Desc = new Paragraph(
                    "Represents individual courses on the platform.\n" +
                    " • id: BIGINT (Primary Key, GenerationType.IDENTITY)\n" +
                    " • title: VARCHAR(150) - Title of the course\n" +
                    " • description: VARCHAR(1000) - Summary outline\n" +
                    " • category: VARCHAR(100) - Classification category\n" +
                    " • instructor_id: BIGINT - Foreign Key pointing to users table\n" +
                    " • difficulty_level: VARCHAR(50) - Beginner, Intermediate, Advanced",
                    normalFont
            );
            entity2Desc.setSpacingAfter(12f);
            document.add(entity2Desc);

            Paragraph entity3 = new Paragraph("3.3 Enrollment Entity (enrollments table)", subsectionTitleFont);
            entity3.setSpacingAfter(6f);
            document.add(entity3);
            Paragraph entity3Desc = new Paragraph(
                    "Acts as the relation join. The combination of student_id and course_id is uniquely indexed.\n" +
                    " • id: BIGINT (Primary Key, GenerationType.IDENTITY)\n" +
                    " • student_id: BIGINT - Foreign Key pointing to users table (role must be STUDENT)\n" +
                    " • course_id: BIGINT - Foreign Key pointing to courses table\n" +
                    " • progress: DOUBLE - Completion percentage from 0.0 to 100.0\n" +
                    " • status: VARCHAR(50) - Enumerated String (ACTIVE, COMPLETED, DROPPED)\n" +
                    " • enrolled_date: TIMESTAMP - Registration time stamp",
                    normalFont
            );
            entity3Desc.setSpacingAfter(20f);
            document.add(entity3Desc);

            document.newPage();

            // 5. Complete API Matrix
            Paragraph sec4Title = new Paragraph("4. Complete API & Role Permission Matrix", sectionTitleFont);
            sec4Title.setSpacingAfter(12f);
            document.add(sec4Title);

            PdfPTable apiTable = new PdfPTable(4);
            apiTable.setWidthPercentage(100);
            apiTable.setSpacingAfter(20f);
            apiTable.setWidths(new float[]{3.2f, 1.3f, 2.5f, 4f});

            addTableHeaderCell(apiTable, "API Endpoint URL", tableHeaderFont, primaryNavy);
            addTableHeaderCell(apiTable, "Method", tableHeaderFont, primaryNavy);
            addTableHeaderCell(apiTable, "Authorized Roles", tableHeaderFont, primaryNavy);
            addTableHeaderCell(apiTable, "Action Summary", tableHeaderFont, primaryNavy);

            addTableCell(apiTable, "/api/auth/register", tableBodyFont);
            addTableCell(apiTable, "POST", tableBodyFont);
            addTableCell(apiTable, "Permit All", tableBodyFont);
            addTableCell(apiTable, "Registers a new user, hashes password with BCrypt.", tableBodyFont);

            addTableCell(apiTable, "/api/auth/login", tableBodyFont);
            addTableCell(apiTable, "POST", tableBodyFont);
            addTableCell(apiTable, "Permit All", tableBodyFont);
            addTableCell(apiTable, "Authenticates credentials and returns a secure JWT.", tableBodyFont);

            addTableCell(apiTable, "/api/courses", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "Permit All", tableBodyFont);
            addTableCell(apiTable, "Lists all courses with paginated search, filter & caching.", tableBodyFont);

            addTableCell(apiTable, "/api/courses/{id}", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "Permit All", tableBodyFont);
            addTableCell(apiTable, "Fetches single course details from cache or db.", tableBodyFont);

            addTableCell(apiTable, "/api/courses", tableBodyFont);
            addTableCell(apiTable, "POST", tableBodyFont);
            addTableCell(apiTable, "INSTRUCTOR, ADMIN", tableBodyFont);
            addTableCell(apiTable, "Creates a new course catalog record. Evicts caches.", tableBodyFont);

            addTableCell(apiTable, "/api/courses/{id}", tableBodyFont);
            addTableCell(apiTable, "PUT", tableBodyFont);
            addTableCell(apiTable, "INSTRUCTOR, ADMIN", tableBodyFont);
            addTableCell(apiTable, "Modifies course parameters. Evicts caches.", tableBodyFont);

            addTableCell(apiTable, "/api/courses/{id}", tableBodyFont);
            addTableCell(apiTable, "DELETE", tableBodyFont);
            addTableCell(apiTable, "INSTRUCTOR, ADMIN", tableBodyFont);
            addTableCell(apiTable, "Permanently deletes course. Evicts caches.", tableBodyFont);

            addTableCell(apiTable, "/api/enroll/{courseId}", tableBodyFont);
            addTableCell(apiTable, "POST", tableBodyFont);
            addTableCell(apiTable, "STUDENT", tableBodyFont);
            addTableCell(apiTable, "Enrolls active student in course. Blocks duplicates.", tableBodyFont);

            addTableCell(apiTable, "/api/enroll/{courseId}", tableBodyFont);
            addTableCell(apiTable, "DELETE", tableBodyFont);
            addTableCell(apiTable, "STUDENT", tableBodyFont);
            addTableCell(apiTable, "Unenrolls student and deletes relation record.", tableBodyFont);

            addTableCell(apiTable, "/api/student/enrollments", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "STUDENT", tableBodyFont);
            addTableCell(apiTable, "Returns active course subscription list of current student.", tableBodyFont);

            addTableCell(apiTable, "/api/student/enrollments/{courseId}/progress", tableBodyFont);
            addTableCell(apiTable, "PUT", tableBodyFont);
            addTableCell(apiTable, "STUDENT, INSTRUCTOR", tableBodyFont);
            addTableCell(apiTable, "Updates course progress. If 100%, changes status to COMPLETED.", tableBodyFont);

            addTableCell(apiTable, "/api/student/{id}/progress", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "STUDENT, INSTRUCTOR", tableBodyFont);
            addTableCell(apiTable, "Calculates score, completed courses, and averages.", tableBodyFont);

            addTableCell(apiTable, "/api/student/{id}/report/pdf", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "STUDENT, INSTRUCTOR", tableBodyFont);
            addTableCell(apiTable, "Generates and streams a custom progress PDF download.", tableBodyFont);

            addTableCell(apiTable, "/api/student/{id}/report/email", tableBodyFont);
            addTableCell(apiTable, "GET", tableBodyFont);
            addTableCell(apiTable, "STUDENT, INSTRUCTOR", tableBodyFont);
            addTableCell(apiTable, "Generates PDF and sends via multi-threaded async email.", tableBodyFont);

            addTableCell(apiTable, "/api/users", tableBodyFont);
            addTableCell(apiTable, "GET/POST/PUT/DELETE", tableBodyFont);
            addTableCell(apiTable, "ADMIN", tableBodyFont);
            addTableCell(apiTable, "Full user profile controls for administrative staff.", tableBodyFont);

            document.add(apiTable);

            document.newPage();

            // 6. Complete Walkthrough of Code
            Paragraph sec5Title = new Paragraph("5. In-Depth Project Walkthrough & Implementation Details", sectionTitleFont);
            sec5Title.setSpacingAfter(12f);
            document.add(sec5Title);

            Paragraph walk1 = new Paragraph("5.1 JWT Stateless Security Implementation", subsectionTitleFont);
            walk1.setSpacingAfter(6f);
            document.add(walk1);
            Paragraph walk1Desc = new Paragraph(
                    "Authentication is implemented using stateless token filtering. " +
                    "Upon successful authentication at '/api/auth/login', the JwtTokenProvider generates a secure JWT compact string, " +
                    "signing it with an HMAC SHA-512 secret key configured in application.properties. " +
                    "The payload encapsulates standard metadata, User ID, and the parsed Role. " +
                    "Subsequent calls pass this key inside the 'Authorization: Bearer' header. " +
                    "The JwtAuthenticationFilter intercepts the requests, extracts the JWT, decodes the claims, " +
                    "retrieves standard credentials via CustomUserDetailsService, and builds a UsernamePasswordAuthenticationToken " +
                    "which is stored inside the Spring SecurityContextHolder.",
                    normalFont
            );
            walk1Desc.setSpacingAfter(12f);
            document.add(walk1Desc);

            Paragraph walk2 = new Paragraph("5.2 Student Progress Analytics & Performance Score", subsectionTitleFont);
            walk2.setSpacingAfter(6f);
            document.add(walk2);
            Paragraph walk2Desc = new Paragraph(
                    "Academic metrics are dynamically aggregated by ProgressService. Every enrollment records progress from 0.0 to 100.0. " +
                    "When a student hits 100.0, the status transitions automatically from ACTIVE to COMPLETED. " +
                    "The Performance Score is computed in real-time on the service layer using the formula:\n\n" +
                    "        Score = (Completed Courses * 50) + (Average Progress * 0.5)\n\n" +
                    "All analytics are aggregated inside a clean, unexposed ProgressDto returned to the controller.",
                    normalFont
            );
            walk2Desc.setSpacingAfter(12f);
            document.add(walk2Desc);

            Paragraph walk3 = new Paragraph("5.3 Dynamic PDF Compilation (OpenPDF)", subsectionTitleFont);
            walk3.setSpacingAfter(6f);
            document.add(walk3);
            Paragraph walk3Desc = new Paragraph(
                    "The ReportService utilizes the OpenPDF toolkit to construct vector document grids. " +
                    "It dynamically draws title paragraphs, separation dividers, metadata profile lists, and a catalog matrix " +
                    "documenting course-wise progress and status. " +
                    "It also dynamically evaluates student metrics and assigns special achievements:\n" +
                    " • [GOLD BADGE] - unlocked if performance score reaches or exceeds 100.\n" +
                    " • [ELITE GRADUATE] - unlocked if completed courses count is at least 2.\n" +
                    " • [HIGH ACHIEVER] - unlocked if student maintains a progress average above 75%.\n" +
                    "Remarks are calculated contextually, pointing out areas of success or recommending extra focus.",
                    normalFont
            );
            walk3Desc.setSpacingAfter(12f);
            document.add(walk3Desc);

            Paragraph walk4 = new Paragraph("5.4 Asynchronous Email Notifications (@Async)", subsectionTitleFont);
            walk4.setSpacingAfter(6f);
            document.add(walk4);
            Paragraph walk4Desc = new Paragraph(
                    "Offloading high-latency networking operations is critical for UI performance. " +
                    "When a user triggers '/api/student/{id}/report/email', the report is generated, " +
                    "and the bytes are forwarded to EmailService's sendReportEmailWithAttachment method. " +
                    "This method is annotated with @Async('mailExecutor') and is routed to a custom ThreadPoolTaskExecutor thread " +
                    "configured in AsyncConfig. The calling HTTP thread receives an immediate 200 OK success payload " +
                    "notifying that the email process has started in the background, while the SMTP delivery completes safely " +
                    "without blocking user interaction.",
                    normalFont
            );
            walk4Desc.setSpacingAfter(20f);
            document.add(walk4Desc);

            document.newPage();

            // 7. Seeding & Verification
            Paragraph sec6Title = new Paragraph("6. Database Seeding & Verification Guides", sectionTitleFont);
            sec6Title.setSpacingAfter(12f);
            document.add(sec6Title);

            Paragraph seedDesc = new Paragraph(
                    "To enable immediate test cycles, the main LmsBackendApplication integrates a CommandLineRunner bean " +
                    "which checks the database state upon boot. If the databases are blank, it creates: \n" +
                    " 1. ADMIN Profile: admin@lms.com / admin123\n" +
                    " 2. INSTRUCTOR Profile: instructor@lms.com / instructor123 (Dr. Sarah Jenkins)\n" +
                    " 3. STUDENT Profile: student@lms.com / student123 (Alex Carter)\n" +
                    " 4. Three core courses taught by Dr. Jenkins.\n\n" +
                    "Steps for testing APIs using curl:\n" +
                    "  Step 1: Perform login as student:\n" +
                    "    curl -X POST http://localhost:8080/api/auth/login -H \"Content-Type: application/json\" " +
                    "-d \"{\\\"email\\\":\\\"student@lms.com\\\",\\\"password\\\":\\\"student123\\\"}\"\n\n" +
                    "  Step 2: Copy the 'accessToken' from response.\n\n" +
                    "  Step 3: Access student progress metrics (replacing <TOKEN> and <ID>):\n" +
                    "    curl -X GET http://localhost:8080/api/student/<ID>/progress -H \"Authorization: Bearer <TOKEN>\"\n\n" +
                    "  Step 4: Download PDF progress report:\n" +
                    "    curl -X GET http://localhost:8080/api/student/<ID>/report/pdf -H \"Authorization: Bearer <TOKEN>\" --output report.pdf\n\n" +
                    "  Step 5: Send PDF report asynchronously via email:\n" +
                    "    curl -X GET http://localhost:8080/api/student/<ID>/report/email -H \"Authorization: Bearer <TOKEN>\"",
                    normalFont
            );
            seedDesc.setSpacingAfter(30f);
            document.add(seedDesc);

            Paragraph conclusion = new Paragraph("This concludes the formal technical blueprint for the Smart LMS Backend Project.", subTitleFont);
            conclusion.setAlignment(Element.ALIGN_CENTER);
            document.add(conclusion);

            document.close();
            System.out.println("Complete project documentation PDF successfully generated!");

        } catch (Exception e) {
            System.err.println("Failed to generate documentation PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(new Color(224, 224, 224));
        table.addCell(cell);
    }

    private static void addTableHeaderCell(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8f);
        cell.setBackgroundColor(bgColor);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(bgColor);
        table.addCell(cell);
    }
}

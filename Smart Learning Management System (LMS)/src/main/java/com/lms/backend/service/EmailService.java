package com.lms.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends student progress report with PDF attachment asynchronously.
     */
    @Async("mailExecutor")
    public void sendReportEmailWithAttachment(String toEmail, String studentName, byte[] pdfBytes) {
        logger.info("Starting asynchronous email delivery thread to: {}", toEmail);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // Enable multipart mode for attachments
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Smart LMS Academic Progress Report - " + studentName);
            
            // Styled plain text content
            String emailBody = String.format(
                    "Hello %s,\n\n" +
                    "We are delighted to share your academic progress report from Smart LMS. " +
                    "Your latest performance scores, badges, and progress summary remarks are compiled in the attached PDF.\n\n" +
                    "Keep up the exceptional effort!\n\n" +
                    "Warm regards,\n" +
                    "The Smart LMS Academic Team",
                    studentName
            );
            helper.setText(emailBody);

            // Add attachment from byte array
            ByteArrayResource pdfAttachment = new ByteArrayResource(pdfBytes);
            helper.addAttachment("Smart_LMS_Progress_Report.pdf", pdfAttachment, "application/pdf");

            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            logger.error("Failed to construct or send progress report email to: {}. Error: {}", toEmail, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in email dispatch to: {}. Error: {}", toEmail, e.getMessage());
        }
    }
}

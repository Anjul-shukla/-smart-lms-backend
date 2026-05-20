package com.lms.backend;

import com.lms.backend.entity.Course;
import com.lms.backend.entity.Role;
import com.lms.backend.entity.User;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class LmsBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(LmsBackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LmsBackendApplication.class, args);
        logger.info("Smart Learning Management System (LMS) Backend is running successfully!");
    }

    /**
     * Seeds initial users (ADMIN, INSTRUCTOR, STUDENT) and sample courses if database is empty.
     */
    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository, 
                                         CourseRepository courseRepository, 
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            logger.info("Checking database state for initial data seeding...");

            // 1. Seed Admin User
            if (!userRepository.existsByEmail("admin@lms.com")) {
                User admin = new User(
                        "LMS Administrator",
                        "admin@lms.com",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN
                );
                userRepository.save(admin);
                logger.info("Admin user seeded successfully (admin@lms.com / admin123)");
            }

            // 2. Seed Instructor User
            User instructor = null;
            if (!userRepository.existsByEmail("instructor@lms.com")) {
                instructor = new User(
                        "Dr. Sarah Jenkins",
                        "instructor@lms.com",
                        passwordEncoder.encode("instructor123"),
                        Role.INSTRUCTOR
                );
                instructor = userRepository.save(instructor);
                logger.info("Instructor user seeded successfully (instructor@lms.com / instructor123)");
            } else {
                instructor = userRepository.findByEmail("instructor@lms.com").orElse(null);
            }

            // 3. Seed Student User
            if (!userRepository.existsByEmail("student@lms.com")) {
                User student = new User(
                        "Alex Carter",
                        "student@lms.com",
                        passwordEncoder.encode("student123"),
                        Role.STUDENT
                );
                userRepository.save(student);
                logger.info("Student user seeded successfully (student@lms.com / student123)");
            }

            // 4. Seed Sample Courses if database has no courses and we have our instructor
            if (courseRepository.count() == 0 && instructor != null) {
                Course springBoot = new Course(
                        "Spring Boot Masterclass: Zero to Hero",
                        "A comprehensive, production-oriented dive into web APIs, security integration, Hibernate JPA databases, and enterprise testing frameworks.",
                        "Software Engineering",
                        instructor.getId(),
                        "Intermediate"
                );
                courseRepository.save(springBoot);

                Course microservices = new Course(
                        "Enterprise Microservices with Spring Cloud & Docker",
                        "Master the art of building scalable, fault-tolerant cloud microservices, service discoveries, configurations, API gateways, and async streams.",
                        "Cloud Architecture",
                        instructor.getId(),
                        "Advanced"
                );
                courseRepository.save(microservices);

                Course javaBasics = new Course(
                        "Introduction to Java 17 Programming",
                        "Learn the core pillars of Java programming, standard syntax, object-oriented concepts, record types, lambdas, and functional interfaces.",
                        "Software Engineering",
                        instructor.getId(),
                        "Beginner"
                );
                courseRepository.save(javaBasics);

                logger.info("Sample course catalog seeded successfully (3 courses added)");
            }
        };
    }
}

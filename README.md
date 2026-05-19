# 📚 Smart Learning Management System (LMS) Backend

A production-ready **Spring Boot backend system** for managing online learning platforms with **JWT authentication, role-based access control, course management, enrollment tracking, PDF report generation, and automated email delivery**.

---

## 🚀 Features

### 🔐 Authentication & Security
- JWT-based authentication
- Role-based access control (ADMIN, INSTRUCTOR, STUDENT)
- BCrypt password encryption
- Secure REST APIs with Spring Security

---

### 📚 Course Management
- Create, update, delete courses
- Search & filter by category and keyword
- Pagination and sorting support
- Instructor-based course ownership

---

### 🧑‍🎓 Enrollment System
- Students can enroll/unenroll in courses
- Duplicate enrollment prevention
- Enrollment tracking with status and progress

---

### 📊 Progress Tracking
- Course-wise progress monitoring
- Overall performance score calculation


Score = (Completed Courses × 50) + (Average Progress × 0.5)


---

### 📄 PDF Report Generation
- Professional student progress reports
- Includes:
  - Student details
  - Enrolled courses
  - Progress percentage
  - Performance score
  - Completion status

---

### 📧 Email Automation
- Automatic email delivery with PDF attachment
- Async email processing using `@Async`
- SMTP support (Gmail / Mailtrap)

---

### ⚡ Performance Optimizations
- Spring Cache for frequently accessed data
- Async processing for emails and reports
- Clean DTO-based architecture

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring MVC (REST APIs)
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL Database
- Maven
- JavaMailSender (SMTP)
- OpenPDF (PDF generation)
- Spring Cache
- @Async + ThreadPoolExecutor

---

## 🏗️ Project Architecture


Controller → Service → Repository → Database
↓
DTO Layer
↓
Security + Config + Util + Exception


---

## 📂 Project Structure


com.lms.backend
├── controller
├── service
│ └── impl
├── repository
├── dto
├── entity
├── security
├── config
├── util
└── exception


---

## 🗄️ Database Entities

### 👤 User
- id
- name
- email
- password
- role (ADMIN / INSTRUCTOR / STUDENT)

---

### 📚 Course
- id
- title
- description
- category
- instructorId
- difficultyLevel

---

### 🧾 Enrollment
- id
- studentId
- courseId
- progress (%)
- status (ACTIVE / COMPLETED / DROPPED)
- enrolledDate

---

## 🔐 Role Permissions

| Role        | Permissions |
|-------------|------------|
| ADMIN       | Manage users, view all data |
| INSTRUCTOR  | Create/manage courses, view analytics |
| STUDENT     | Enroll courses, track progress, download reports |

---

## 🌐 API Endpoints

### 🔑 Authentication

POST /api/auth/register
POST /api/auth/login


---

### 📚 Courses

GET /api/courses
POST /api/courses
PUT /api/courses/{id}
DELETE /api/courses/{id}


---

### 🧑‍🎓 Enrollment

POST /api/enroll/{courseId}
GET /api/student/enrollments


---

### 📊 Progress

GET /api/student/{id}/progress


---

### 📄 Reports

GET /api/student/{id}/report/pdf
GET /api/student/{id}/report/email


---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository
```bash
git clone https://github.com/your-username/lms-backend.git
cd lms-backend
2️⃣ Create Database
CREATE DATABASE lms_db;
3️⃣ Configure Application

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
server.port=8080

jwt.secret=your_secret_key

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
4️⃣ Run Project
mvn spring-boot:run
🧪 Default Test Users
Role	Email	Password
ADMIN	admin@lms.com	admin123
INSTRUCTOR	instructor@lms.com	instructor123
STUDENT	student@lms.com	student123
📊 Performance Highlights
JWT Stateless Authentication
Async Email Processing
Spring Cache Optimization
DTO-based secure architecture
Clean modular design
📄 PDF Report Includes
Student profile
Course list
Progress percentage
Completion status
Performance score
Achievement badges
📧 Email System
Automatic report delivery
PDF attachment support
Async background processing
💡 Future Enhancements
React frontend dashboard
Microservices architecture
Docker deployment
AWS hosting (EC2 / RDS)
Real-time notifications (WebSockets)
Payment gateway integration
👨‍💻 Author

Anjul
B.Tech Computer Science (Core)

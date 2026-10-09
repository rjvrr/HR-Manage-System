# HR-Manage-System
# HR Manage System

A full-stack **Human Resource Management System (HRMS)** designed to simplify and streamline employee management, recruitment, payroll processing, and performance tracking. The application provides a centralized platform for managing essential HR operations through a modern web interface.

## 🚀 Features

- **Employee Management:** Maintain employee records, contact details, departments, job titles, salaries, and employment status.
- **Recruitment Management:** Manage candidate information and job openings.
- **Payroll Management:** Organize and maintain employee payroll records.
- **Performance Reviews:** Track employee performance and manage performance review records.
- **REST API Integration:** Connect the frontend with the backend for data management.
- **Database Integration:** Store and manage HR-related information in a MySQL database.
- **Interactive Dashboard:** Provide a centralized interface for accessing HR information.

## 🛠️ Tech Stack

### Backend
- **Java** — Core programming language
- **Spring Boot** — Backend application framework
- **Spring Data JPA / Hibernate** — Database interaction and ORM
- **Maven** — Dependency management and build automation

### Frontend
- **Angular** — Frontend framework
- **TypeScript** — Frontend programming language
- **HTML5 & CSS3** — Structure and styling
- **Angular Material** — UI components and interface design

### Database
- **MySQL** — Relational database management system

### Tools
- **Git & GitHub** — Version control and collaboration
- **Postman** — REST API testing
- **Visual Studio Code / IntelliJ IDEA** — Development environments

## 📂 Project Structure

```text
HR-Manage-System/
├── src/
│   └── main/
│       ├── java/          # Spring Boot backend source code
│       └── resources/     # Application configuration
├── frontend/              # Angular frontend application
├── pom.xml                # Maven configuration
├── mvnw                   # Maven wrapper (Unix)
├── mvnw.cmd               # Maven wrapper (Windows)
├── .gitignore
└── README.md
```

## ⚙️ Prerequisites

Before running the application, ensure you have installed:

- Java JDK
- Maven (or use the included Maven wrapper)
- Node.js and npm
- Angular CLI
- MySQL Server

## 💻 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/rjvrr/HR-Manage-System.git
cd HR-Manage-System
```

### 2. Configure the Database

Create a MySQL database and configure the database URL, username, and password in the backend application's configuration file.

Update `src/main/resources/application.properties` with your local settings. Never commit real database passwords or other credentials to GitHub.

### 3. Run the Backend

From the project root, run:

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Alternatively, if Maven is installed:**
```bash
mvn spring-boot:run
```

### 4. Run the Frontend

Open a separate terminal:

```bash
cd frontend
npm install
ng serve
```

Open the local URL displayed in the terminal, typically `http://localhost:4200`.

## 🔌 API Endpoints

The backend exposes REST APIs under the `/api` base path.

| Endpoint | Purpose |
|---|---|
| `/api/employees` | Employee management |
| `/api/candidates` | Candidate management |
| `/api/jobs` | Job opening management |
| `/api/payroll` | Payroll records |
| `/api/performance-reviews` | Performance review records |

*The availability of individual operations depends on the implemented backend endpoints.*

## 🎯 Project Objective

The objective of HR Manage System is to reduce manual HR administration, centralize employee information, improve access to recruitment and payroll records, and provide a maintainable foundation for digital HR operations.

## 🔮 Future Enhancements

- Role-based access control for HR administrators and employees
- Employee attendance and leave management
- Automated payroll calculations
- Recruitment workflow and application tracking
- Reports, analytics, and dashboard visualizations
- Email notifications and document management

## 👨‍💻 Contributing

Contributions, suggestions, and bug reports are welcome. Fork the repository, create a feature branch, and submit a pull request.

## 📄 License

No license has been specified yet. All rights are reserved by default unless a license is added to the repository.

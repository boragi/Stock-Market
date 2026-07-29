# 📈 Stock Market Application

A full-stack **Stock Market Trading Application** built using **Spring Boot, Thymeleaf, MySQL, and REST APIs**. The application provides separate modules for **Admin** and **Users**, enabling stock management, portfolio tracking, wallet recharges, and secure user authentication with email OTP verification.

## 🚀 Features

### 👨‍💼 Admin Module

- Secure admin login using credentials configured in `application.properties`
- Add stocks using stock ticker symbols
- Fetch real-time stock information using the **Alpha Vantage API**
- Manage available stocks
- View complete transaction history
- Monitor stock purchase and selling activities
- Track total platform fees collected

### 👤 User Module

- User registration with Email OTP verification
- Secure user login
- Wallet recharge using the **Razorpay Payment Gateway**
- Browse available stocks
- Buy stocks using wallet balance
- View personal investment portfolio
- Sell owned stocks
- View transaction history

## 🛠️ Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate

### Frontend
- Thymeleaf
- HTML5
- CSS3
- JavaScript

### Database
- MySQL

### APIs & Services
- Alpha Vantage API
- Razorpay API
- Java Mail Sender

### Build Tool
- Maven

## 📂 Project Architecture

```
src
├── main
│   ├── java
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── entity
│   │   ├── dto
│   │   ├── configuration
│   │   ├── utility
│   │   └── exception
│   └── resources
│       ├── templates
│       ├── static
│       └── application.properties
```

## 🔑 Key Functionalities

### Admin

- Login using configured credentials
- Add new stocks by entering stock ticker symbols
- Retrieve stock details from Alpha Vantage API
- Manage stock listings
- View all buy/sell transactions
- Monitor platform earnings

### User

- Register with Email OTP verification
- Login securely
- Recharge wallet using Razorpay
- Buy stocks
- Sell owned stocks
- Manage portfolio
- View wallet balance
- Track transaction history

## 🔄 Application Workflow

### Admin Workflow

1. Login
2. Add Stock
3. Enter Stock Symbol
4. Fetch Stock Details from Alpha Vantage API
5. Save Stock Information
6. Manage Stocks
7. View Transactions
8. View Platform Revenue

### User Workflow

1. Register
2. Verify Email via OTP
3. Login
4. Recharge Wallet
5. Browse Stocks
6. Purchase Stocks
7. View Portfolio
8. Sell Stocks
9. Check Transaction History

## 🔌 API Integrations

### 📊 Alpha Vantage API

Used to fetch live stock information based on ticker symbols.

**Fetched Data**
- Company Name
- Stock Symbol
- Current Price
- Market Information
- Other Stock Details

### 💳 Razorpay API

Used for secure wallet recharge functionality.

Users can:

- Add money to wallet
- Complete online payments
- Purchase stocks using wallet balance

### 📧 Java Mail Sender

Used for:

- Sending OTP during registration
- Email verification

## 💾 Database

The application uses **MySQL**.

Main Tables:

- Users
- Stocks
- Transactions
- Wallet
- Admin

## ⚙️ Prerequisites

Before running the project, ensure the following are installed:

- Java JDK 17 or above
- Maven
- MySQL
- Git
- IDE (IntelliJ IDEA / Eclipse / VS Code)

## 📥 Installation

### Clone the Repository

```bash
git clone https://github.com/<your-username>/<repository-name>.git
```
Move into the project directory

```bash
cd <repository-name>
```

### Configure Database

Create a MySQL database.

Example:

```sql
CREATE DATABASE stock_market;
```
Update `application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_market
spring.datasource.username=root
spring.datasource.password=your_password
```
### Configure External APIs

Add the following credentials inside `application.properties`.

```properties
# Alpha Vantage
alphavantage.api.key=YOUR_API_KEY

# Razorpay
razorpay.key=YOUR_KEY
razorpay.secret=YOUR_SECRET

# Mail
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

---

### Build the Project

```bash
mvn clean install
```

---

### Run the Application

```bash
mvn spring-boot:run
```

or

Run the main Spring Boot application from your IDE.

---

## 🌐 Access the Application

```
http://localhost:8080
```

---

## 🔒 Authentication

This project uses **HTTP Session Management** instead of Spring Security.

- Separate sessions for Admin and User
- Session-based authentication
- Role-based access control
- Session invalidation on logout

---

## 📊 Project Highlights

- Full Stack Java Application
- Session-Based Authentication
- REST API Integration
- Email OTP Verification
- Online Payment Gateway Integration
- Portfolio Management
- Real-Time Stock Data
- MVC Architecture
- Layered Architecture
- JPA & Hibernate
- MySQL Database
- Responsive UI using Thymeleaf

---

## 📷 Screenshots

You can add screenshots here.

```
Home Page
Admin Dashboard
User Dashboard
Portfolio
Wallet Recharge
Stock Purchase
Transaction History
```

---

## 🔮 Future Enhancements

- Spring Security with JWT Authentication
- Real-Time Stock Price Updates using WebSockets
- Watchlist Feature
- Stock Price Charts
- Investment Analytics Dashboard
- Email Notifications
- Mobile Responsive UI Improvements
- Docker Deployment
- CI/CD using Jenkins
- Kubernetes Deployment
- AWS EC2 Deployment

---

## 👨‍💻 Author

**Gouri Boragi**

Computer Science & Engineering Graduate

Java Full Stack Developer | Spring Boot | MySQL | REST APIs | DevOps Enthusiast

---

## ⭐ Support

If you found this project helpful, consider giving it a ⭐ on GitHub.

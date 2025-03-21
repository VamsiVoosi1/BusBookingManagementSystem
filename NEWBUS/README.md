# Bus Booking Management System

## Overview
The **Bus Booking Management System** is a web-based application developed using **Spring Boot, MySQL, and Thymeleaf**. It provides an easy-to-use interface for both **Admin** and **Passengers** to manage and book bus seats efficiently.

## Features

### **Admin Module**
- Admin can **add buses** with details such as bus name, from and to locations, date, time, and seat price.
- Admin can **add seats** for each bus, defining availability and seat layout.
- Admin can **manage bus details** including routes, schedules, and seat availability.

### **Passenger Module**
- Passengers can **search for available buses** based on location, date, time, and seat price.
- Passengers can **select seats** from the available layout and proceed with the booking.
- Passengers can **checkout** and confirm their booking.
- After successful booking, passengers can **download a PDF ticket**.
- A confirmation email is sent to the passenger upon successful booking.
- Passengers can **view their booking history** in their profile.

## Technology Stack
- **Backend:** Spring Boot (Spring MVC, JPA, Thymeleaf, Security, Mail)
- **Database:** MySQL
- **Frontend:** HTML, CSS, Thymeleaf
- **PDF Generation:** Apache PDFBox
- **Email Service:** Spring Mail

## Installation & Setup
### Prerequisites
- Java 17+
- Spring Boot
- MySQL Database
- Maven

### 
 **Access the Application**
   - Admin Panel: `http://localhost:8080/admin`
   - Passenger Panel: `http://localhost:8080/passenger`

## Usage
### **Admin Login**
- **URL:** `/admin`
- **Default Credentials:** (Change in database after first login)
  - **Email:** `admin@example.com`
  - **Password:** `admin123`

### **Passenger Registration & Login**
- Passengers need to **register** before booking a bus.
- After login, they can search, book, and download tickets.

## Conclusion
The **Bus Booking Management System** simplifies online bus reservations by providing an intuitive interface for **Admins** to manage buses and seats, while allowing **Passengers** to book and download PDF tickets seamlessly. With a robust backend and user-friendly frontend, this system ensures an efficient and hassle-free experience for users. Future improvements can include **payment integration**, **real-time seat updates**, and **mobile app support** to enhance usability further.

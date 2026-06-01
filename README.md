# AcmePlex Movie Booking System

A full-stack movie theatre booking application built with Java, Spring Boot, MySQL, and a static HTML/CSS/JavaScript frontend. The application supports movie browsing, theatre selection, showtime lookup, seat selection, user registration, payment handling, booking confirmation, cancellation credits, and email notifications.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- HTML
- CSS
- JavaScript

## Features

### Movie and Theatre Browsing

- View available movies
- Browse theatre locations
- Fetch movie details
- Select showtimes for a movie and theatre

### Seat Selection

- View seat availability by showtime
- Select regular or premium seats
- Update seat status after booking or cancellation

### User Accounts

- Register new users
- Sign in and check authentication status
- Update personal account information
- Update saved payment information
- Delete account
- Continue as a guest during checkout

### Booking and Payment Flow

- Process bookings for registered users and guests
- Support saved payment information for registered users
- Support credit card, debit card, and PayPal-style payment strategies
- Create bookings after successful payment
- Update seat and showtime capacity after booking
- Send booking confirmation and receipt emails

### Cancellations and Credits

- Cancel eligible bookings
- Generate cancellation credits
- Support credit lookup and credit application
- Send cancellation and credit notification emails

### Scheduled Jobs

- Update movie availability based on release dates
- Process annual user renewals
- Reset expired showtimes and seat availability

## Frontend Overview

The frontend is built with static HTML, CSS, and JavaScript served directly through Spring Boot from `src/main/resources/static`.

The frontend guides users through the full booking flow:

- Browse movies
- Select a theatre
- Choose a showtime
- Pick an available seat
- Sign in, sign up, or continue as a guest
- Complete payment
- View booking confirmation
- Cancel a booking when eligible
- Manage account settings

## Frontend Structure

```text
src/main/resources/static
├── index.html
├── movies.html
├── theatres.html
├── showtime.html
├── seat.html
├── payment.html
├── confirmation.html
├── cancel.html
├── notifications.html
├── settings.html
├── signin.html
├── signup.html
└── about.html
```

## Backend Structure

```text
src/main/java/com/group20
├── Config
├── PaymentStrategy
├── Repository
├── Strategy
├── controller
├── demo
├── model
├── service
└── util
```

## Main API Endpoints

### Authentication

```http
GET /api/auth/status
```

Checks whether the current user is authenticated.

### Users

```http
POST /api/user/register
GET /api/user/findByEmail
GET /api/user/check-email
GET /api/user
PUT /api/user/update/personal
PUT /api/user/update/payment
DELETE /api/user/delete
```

Handles registration, account lookup, profile updates, payment updates, and account deletion.

### Movies

```http
GET /api/Movies/available
GET /api/Movies/restricted
GET /api/Movies/{movieId}
```

Fetches available movies, restricted movies, and movie details.

### Theatres

```http
GET /api/theatres
GET /api/theatres/{theatreId}
```

Fetches all theatres or a specific theatre.

### Showtimes

```http
GET /api/{theatreId}/{movieId}/showtime
GET /api/{theatreId}/{movieId}/showtime/prebook
```

Fetches available showtimes for a selected movie and theatre.

### Seats

```http
GET /api/{showtimeId}/seats
GET /api/{showtimeId}/seats/{seatId}
```

Fetches the seat map for a showtime and retrieves individual seat details.

### Payment

```http
POST /api/{theatreId}/{movieId}/{showtimeId}/{seatId}/payment
```

Processes payment and creates a booking.

### Credits and Cancellations

```http
GET /api/credit/check/{creditId}
POST /api/credit/confirm/{creditId}
GET /api/credit/cancellation/{bookingId}
```

Checks credit balance, confirms credit usage, and handles booking cancellation credits.

## Database Models

The application uses JPA entities for:

- User
- Movie
- Theatre
- Showtime
- Seat
- Booking
- Credit

These models represent the main cinema booking workflow. Users book seats for showtimes, showtimes belong to movies and theatres, bookings connect users to seats and showtimes, and cancellations can generate credits.

## Design Patterns Used

### Strategy Pattern

Payment handling uses separate payment strategy classes for different payment methods. This keeps the payment flow extensible and separates payment-specific logic from the main booking flow.

### Observer Pattern

Booking, cancellation, and showtime reset workflows use observer-style interfaces so related state changes can update seats and showtimes cleanly.

## Running Locally

### Prerequisites

- Java 17
- Maven
- MySQL
- A local MySQL database

### 1. Clone the repository

```bash
git clone https://github.com/Raakin12/Java-Springboot-Web-App.git
cd Java-Springboot-Web-App
```

### 2. Create the database

```sql
CREATE DATABASE ENSF614;
```

### 3. Configure local settings

Update `src/main/resources/application.properties` with your own local database and email settings.

Do not commit real passwords or app passwords to GitHub.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ENSF614?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_app_password
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### 5. Open the app

```text
http://localhost:8080/index.html
```

## Notes

The project includes sample data population logic for theatres, movies, showtimes, and seats. This can be enabled when the local database needs to be seeded.

## Future Improvements

- Move credentials into environment variables
- Add DTOs for cleaner API request and response objects
- Add stronger authorization rules around account and booking operations
- Add integration tests for booking, cancellation, and payment workflows
- Replace simulated payment logic with a real payment provider sandbox
- Add screenshots and a short demo GIF
- Add Swagger or OpenAPI documentation

## Project Status

Completed academic full-stack project demonstrating backend API design, relational persistence, authentication flow, booking transactions, scheduled jobs, email notifications, and frontend integration.
EOF

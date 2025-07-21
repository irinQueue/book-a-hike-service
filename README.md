# 🗕 Book-a-Hike Service

A Spring Boot service for managing hiking trail bookings. Users can create, update, cancel, and view bookings for scheduled hikes.

---

## 🧽 Features

* List available trails for a given date
* Book a trail on a specific date and time
* Update or cancel existing bookings
* Filter bookings by bookingDate or booked-by user
* Interactive Swagger UI at startup

---

## 🛠️ Tech Stack

* Java 8
* Spring Boot
* Gradle
* PostgreSQl (containerized via Docker)
* Swagger UI

---
## ⚙️ Setup & Run

### 1. Clone the repo

```bash
git clone https://github.com/irinQueue/book-a-hike-service.git
cd book-a-hike-service
```

### 2. Build the project

```bash
./gradlew clean build
```

### 3. Start the database

If Docker is available:

```bash
docker-compose up -d
```
Otherwise, start your local MySQL and update `application.yml` with relevant credentials.

*pOStgres

* Host: `localhost`
* Port: `4327`
* DB: `booking_db`
* User/Pass: `root` / `root` (adapt as needed)

### 4. Run the application

```bash
./gradlew bootRun
```
The API will be accessible on `http://localhost:8080`.

### 5. Explore API documentation

Open Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```
Use it to test all endpoints interactively.


## 📋 API Endpoints

* **POST** `/v1/bookings` – Create a new booking
* **GET** `/v1/bookings/{bookingDate}` – List all bookings for a date
* **PUT** `/v1/bookings` – Update booking details
* **PUT** `/v1/bookings/{bookingStatus}` – Update booking status
* **GET** `/v1/bookings` – Filter bookings by `bookedBy` and `bookingDate` params

### Example Booking Payload

```json
{
  "bookedBy": {
    "name": "Alice",
    "email": "alice@example.com",
    "dob": "1990-05-15"
  },
  "bookingDate": "2025-08-10",
  "bookingStatus": "Booked",
  "trail": "Shire Trail",
  "startTime": "07:00:00",
  "endTime": "10:00:00",
  "hikers": [
    {
      "name": "Bob",
      "email": "bob@example.com",
      "dob": "1991-09-20"
    }
  ]
}
```

## 💡 Assumptions

* No limit on daily bookings per trail
* Overlapping bookings allowed
* Booking uniquely identified by combination: `user_id`, `booking_date`, `start_time`, `end_time`, and `trail_id`

---

## 🧹 Project Structure

```
src/
├── main/
│   ├── java/…               – Spring Boot app & business logic
│   └── resources/           – config files
├── test/                    – unit/integration tests
build.gradle
docker-compose.yml
gradle.properties
```

---

## ✅ Getting Started Checklist

* [ ] Clone the repo
* [ ] Configure & start MySQL
* [ ] Build the project
* [ ] Run the service
* [ ] Use Swagger to test endpoints

---

## 🏷 License
This project is licensed under the [Apache 2.0 License](LICENSE).
---
## 🔗 Contribution

Contributions, issues and feature requests are welcome! Feel free to make a PR or submit an issue.
---
Enjoy building awesome hiking experiences! 🧾

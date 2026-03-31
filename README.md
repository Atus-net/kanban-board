# Kanban Board API
Dự án Backend cho ứng dụng Kanban Board, xây dựng với Spring Boot, sử dụng PostgreSQL và MongoDB, bảo mật JWT.

## Yêu cầu hệ thống
- Java 17+
- Maven 3.8+ (hoặc sử dụng Maven Wrapper đi kèm)
- PostgreSQL 14+
- MongoDB 5+
- (Tùy chọn) Docker & Docker Compose để chạy database


## Cấu hình cơ sở dữ liệu

### 1. Tạo database PostgreSQL
```sql
CREATE DATABASE kanban_db;
CREATE USER kanban_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE kanban_db TO kanban_user;
```
### 2. Tạo database MongoDB
Không cần tạo trước, ứng dụng sẽ tự tạo khi chạy.


## Cấu hình file `application.yml`
Sao chép file `src/main/resources/application-example.yml` thành `application.yml` và điền thông tin của bạn:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kanban_db
    username: kanban_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  data:
    mongodb:
      uri: mongodb://localhost:27017/kanban_db

jwt:
  secret: your-256-bit-secret-key-base64-encoded
  expiration: 86400000   # 24h tính bằng ms
```
> Lưu ý: `jwt.secret` nên được tạo bằng lệnh `openssl rand -base64 32` và giữ bí mật.

## Chạy ứng dụng
  ### Cách 1: Dùng Maven Wrapper (không cần cài Maven)
  ```bash
  # Trên Linux/macOS
  ./mvnw spring-boot:run
  
  # Trên Windows
  mvnw.cmd spring-boot:run
  ```
  ### Cách 2: Build và chạy JAR
  ```bash
  ./mvnw clean package
  java -jar target/kanban-board-0.0.1-SNAPSHOT.jar
  ```
  Sau khi chạy, ứng dụng sẽ lắng nghe tại `http://localhost:8080`.

## Test API với Postman
  1. **Import file collection:** `.postman/Kanban Board API.postman_collection.json`
  2. **Import môi trường (nếu có):** `.postman/globals/Production.json`
  3. **Các bước cơ bản:**
      - **Đăng ký:** `POST /api/auth/signup`
      - **Đăng nhập:** `POST /api/auth/signin` → nhận token
      - **Gán token:** Trong Postman, chọn tab __Authorization__, type `Bearer Token`, paste __token__ vào.
      - **Gọi API khác:** Ví dụ `GET /api/boards`

## Cấu trúc dự án
```text
src/main/java/com/atus/kanban/
├── controller/       # REST controllers
├── service/          # Business logic
├── repository/       # JPA + MongoDB repositories
├── model/            # Entity, DTO
├── security/         # JWT, Spring Security config
├── config/           # Cấu hình chung (CORS, Swagger, …)
└── exception/        # Xử lý ngoại lệ tập trung
```

## Hỗ trợ
Mở issue trên GitHub hoặc liên hệ qua email.
```text

```

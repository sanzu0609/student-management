# 📌 Giai đoạn 1: Kết nối Database H2 & Định nghĩa Entity Student

## 🎯 Mục tiêu
Thiết lập kết nối tới H2 database (in-memory) và định nghĩa đối tượng `Student` dưới dạng JPA Entity để lưu trữ thông tin sinh viên.

---

## ✅ Acceptance Criteria
- [x] Thêm dependencies `spring-boot-starter-data-jpa` và `h2database` vào file `pom.xml`.  
- [x] Cấu hình thông tin kết nối H2 database và JPA trong file `application.properties`.  
- [ ] Tạo package `com.example.studentmanagement.model`.  
- [ ] Tạo class `Student.java` trong package trên và đánh dấu là JPA entity.  
- [ ] Class `Student` có các thuộc tính:  
  - [ ] `id`: Long, khóa chính, tự động tăng.  
  - [ ] `firstName`: String.  
  - [ ] `lastName`: String.  
  - [ ] `email`: String, duy nhất.  
  - [ ] `dateOfBirth`: LocalDate.  

---

## 📂 Tasks

### Task 1: Cập nhật `pom.xml`
- [x] Thêm các dependencies cần thiết cho JPA và H2.

### Task 2: Cấu hình `application.properties`
- [x] Thêm cấu hình kết nối H2 database.  
- [x] Bật H2 console để có thể truy cập qua trình duyệt.  
- [x] Cấu hình JPA để tự động tạo bảng.

### Task 3: Tạo package `model`
- [ ] Trong `src/main/java/com/example/studentmanagement`, tạo package mới tên là `model`.

### Task 4: Tạo class `Student.java`
- [ ] Định nghĩa class `Student` với các thuộc tính: `id`, `firstName`, `lastName`, `email`, `dateOfBirth`.  
- [ ] Đánh dấu class là `@Entity`.  
- [ ] Đảm bảo `email` là duy nhất, `id` là khóa chính tự động tăng.

---

## 🏷️ Giai đoạn 1 | Độ khó: Beginner
- [ ] Sau khi hoàn thành, chạy ứng dụng và truy cập H2 Console tại đường dẫn `/h2-console` để kiểm tra dữ liệu.
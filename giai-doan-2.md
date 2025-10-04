# 📌 Giai đoạn 2: Repository & Service cho Student

## 🎯 Mục tiêu
Tạo tầng Repository để tương tác với database và tầng Service để xử lý logic nghiệp vụ.

---

## ✅ Acceptance Criteria
- [x] Tạo interface `StudentRepository` kế thừa `JpaRepository`.  
- [x] Tạo class `StudentService` để chứa các phương thức xử lý logic.  
- [x] `StudentService` sử dụng `StudentRepository` thông qua Dependency Injection (`@Autowired`).  
- [x] `StudentService` có các phương thức cơ bản:  
  - [x] `getAllStudents()`  
  - [x] `getStudentById(Long id)`  
  - [x] `createStudent(Student student)`  
  - [x] `updateStudent(Long id, Student studentDetails)`  
  - [x] `deleteStudent(Long id)`  

---

## 📂 Tasks

### Task 1: Tạo `StudentRepository`
- [x] Trong `src/main/java/com/example/studentmanagement`, tạo package mới tên là `repository` và khai báo interface `StudentRepository`.  
- [x] Đánh dấu bằng `@Repository`.  
- [x] Kế thừa `JpaRepository<Student, Long>` để có sẵn các hàm CRUD.

### Task 2: Tạo `StudentService`
- [x] Trong package `service`, tạo class `StudentService`.  
- [x] Đánh dấu bằng `@Service`.  
- [x] Sử dụng Dependency Injection để gọi `StudentRepository`.  

### Task 3: Định nghĩa các phương thức trong `StudentService`
- [x] `getAllStudents()` → Trả về danh sách tất cả sinh viên.  
- [x] `getStudentById(Long id)` → Lấy thông tin 1 sinh viên theo ID.  
- [x] `createStudent(Student student)` → Thêm mới sinh viên.  
- [x] `updateStudent(Long id, Student studentDetails)` → Cập nhật thông tin sinh viên theo ID.  
- [x] `deleteStudent(Long id)` → Xóa sinh viên theo ID.  

---

## 🏷️ Giai đoạn 2 | Độ khó: Beginner
- [ ] Sau khi hoàn thành, có thể test nhanh bằng cách viết Controller hoặc Unit Test để gọi các hàm trong `StudentService`.



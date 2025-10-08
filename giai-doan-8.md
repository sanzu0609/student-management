# 📌 Giai đoạn 8: Integration Test cho StudentController (MockMvc + H2)

## 🎯 Mục tiêu
Viết **integration test** kiểm tra toàn bộ luồng từ **Controller → Service → Repository → H2 Database** cho các API của Student.

---

## ✅ Acceptance Criteria
- [x] Tạo test class `StudentControllerIntegrationTest`.  
- [x] Sử dụng `@SpringBootTest` và `@AutoConfigureMockMvc`.  
- [ ] Viết test case gọi các endpoint `GET / POST / PUT / DELETE` và kiểm tra:  
  - [x] HTTP status.  
  - [x] Nội dung JSON response.  

---

## 🛠️ Tasks

### Task 1: Chuẩn bị cấu hình test
- [x] Tạo class `StudentControllerIntegrationTest` trong `src/test/java/...`.  
- [x] Gắn `@SpringBootTest` (khởi động full context) và `@AutoConfigureMockMvc`.  
- [x] Inject `MockMvc` để thực hiện request ảo.  
- [x] Đảm bảo H2 được sử dụng cho test (mặc định phù hợp; có thể tạo `application-test.properties` nếu muốn).  

### Task 2: Dữ liệu khởi tạo (tuỳ chọn)
- [x] Seed sẵn một vài bản ghi (Flyway migration / SQL script / test setup) để test `GET`/`PUT`/`DELETE`.  
- [x] Xoá/đặt lại dữ liệu trước mỗi test (nếu cần) để tests **độc lập**.  

### Task 3: Test GET danh sách
- [x] Gọi `GET /api/v1/students`.  
- [x] Mong đợi `200 OK`.  
- [x] Kiểm tra JSON trả về: mảng có kích thước ≥ 0, các trường cần thiết xuất hiện.  

### Task 4: Test GET chi tiết theo ID
- [ ] Gọi `GET /api/v1/students/{id}` với ID tồn tại.  
- [ ] Mong đợi `200 OK`.  
- [ ] Kiểm tra `jsonPath` các field (`id`, `firstName`, `lastName`, `email`, `dateOfBirth`).  
- [ ] Với ID không tồn tại: mong đợi `404 Not Found` + cấu trúc lỗi chuẩn (nếu đã có GlobalExceptionHandler).  

### Task 5: Test POST tạo mới
- [ ] Chuẩn bị payload JSON hợp lệ (dùng `ObjectMapper` để serialize object).  
- [ ] Gọi `POST /api/v1/students` với `Content-Type: application/json`.  
- [ ] Mong đợi `201 Created` (hoặc `200 OK` tuỳ quy ước).  
- [ ] Kiểm tra `Location` header (nếu áp dụng) và các field trong body.  
- [ ] Thử payload không hợp lệ → mong đợi `400 Bad Request` + chi tiết lỗi validation.  

### Task 6: Test PUT cập nhật
- [ ] Tạo trước một student (seed hoặc POST).  
- [ ] Gọi `PUT /api/v1/students/{id}` với JSON cập nhật hợp lệ.  
- [ ] Mong đợi `200 OK` (hoặc `204 No Content` tuỳ quy ước) và nội dung cập nhật đúng.  
- [ ] Với ID không tồn tại → mong đợi `404 Not Found`.  

### Task 7: Test DELETE xoá
- [ ] Tạo trước một student.  
- [ ] Gọi `DELETE /api/v1/students/{id}`.  
- [ ] Mong đợi `204 No Content`.  
- [ ] Gọi lại `GET {id}` → mong đợi `404 Not Found`.  

### Task 8: (Tuỳ chọn) Test Pagination/Sorting
- [ ] Nếu API hỗ trợ `page/size/sort`, test một vài tổ hợp tham số và xác nhận trường phân trang trong JSON.  

---

## 🧪 Checklist kiểm thử (ví dụ)
- [ ] `GET /api/v1/students` → 200, mảng `content`/danh sách có kích thước hợp lý.  
- [ ] `GET /api/v1/students/{id}` (tồn tại) → 200, `jsonPath("$.id") == {id}`.  
- [ ] `GET /api/v1/students/{id}` (không tồn tại) → 404, JSON lỗi thống nhất.  
- [ ] `POST /api/v1/students` (hợp lệ) → 201 + body trả về đúng trường.  
- [ ] `POST` (không hợp lệ) → 400 + danh sách lỗi.  
- [ ] `PUT /api/v1/students/{id}` (tồn tại) → 200/204 + dữ liệu cập nhật đúng.  
- [ ] `DELETE /api/v1/students/{id}` (tồn tại) → 204, `GET {id}` → 404.  

---

## 🧰 Gợi ý kỹ thuật (không code)
- [ ] **MockMvc**: dùng `perform(...)` với `get/post/put/delete`, set `contentType`, `content`, `accept`.  
- [ ] **jsonPath**: kiểm tra field cụ thể trong JSON response (`$.id`, `$.email`, `$.errors[0].field`, ...).  
- [ ] **ObjectMapper** (Jackson): `writeValueAsString(object)` để tạo JSON từ object; `readTree` để đọc lại.  
- [ ] **Transactional/Testcontainers** (tuỳ chọn): nếu cần cô lập hơn cho DB.  

---

## 🚀 Cách chạy test (tham khảo)
- [ ] `mvn -q -Dtest=StudentControllerIntegrationTest test` hoặc `mvn test`.  
- [ ] Tích hợp CI để chạy test trên mỗi pull request.  

---

## 🎓 Giai đoạn 8 | Độ khó: Intermediate
- [ ] Sau khi hoàn tất, hệ thống có **integration test đáng tin cậy** cho các API chính, giảm rủi ro khi refactor/mở rộng.

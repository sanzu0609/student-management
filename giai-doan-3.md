# 📌 Giai đoạn 3: REST API (Controller) cho Student

## 🎯 Mục tiêu
Xây dựng các RESTful endpoints để client có thể tương tác với dữ liệu sinh viên thông qua HTTP.

---

## ✅ Acceptance Criteria
- [ ] Tạo class `StudentController`.  
- [ ] Implement các endpoints sau (prefix đề xuất: `/api/v1/students`):  
  - [ ] `GET /api/v1/students` — Lấy danh sách tất cả sinh viên.  
  - [ ] `GET /api/v1/students/{id}` — Lấy thông tin chi tiết một sinh viên theo ID.  
  - [ ] `POST /api/v1/students` — Tạo một sinh viên mới.  
  - [ ] `PUT /api/v1/students/{id}` — Cập nhật thông tin sinh viên theo ID.  
  - [ ] `DELETE /api/v1/students/{id}` — Xóa một sinh viên theo ID.  
- [ ] Kiểm thử các API bằng Postman (hoặc công cụ tương tự) và xác nhận hoạt động đúng.

---

## 📂 Tasks

### Task 1: Khởi tạo Controller
- [x] Tạo class `StudentController` trong package `controller` (hoặc cấu trúc tương đương).  
- [x] Đánh dấu bằng `@RestController` và cấu hình base path bằng `@RequestMapping("/api/v1/students")`.

### Task 2: Định nghĩa Endpoints
- [x] `GET` (list): sử dụng `@GetMapping` để trả về danh sách sinh viên.  
- [x] `GET` (detail): sử dụng `@GetMapping("/{id}")` với `@PathVariable Long id`.  
- [x] `POST` (create): sử dụng `@PostMapping`, nhận dữ liệu bằng `@RequestBody`.  
- [x] `PUT` (update): sử dụng `@PutMapping("/{id}")`, nhận `id` và `@RequestBody`.  
- [x] `DELETE` (remove): sử dụng `@DeleteMapping("/{id}")`.

### Task 3: Kết nối Service
- [x] Inject `StudentService` (Constructor Injection khuyến nghị).  
- [x] Ánh xạ dữ liệu request/response theo model đã định nghĩa.  

### Task 4: Xử lý phản hồi & lỗi
- [x] Trả mã trạng thái phù hợp: `200 OK`, `201 Created`, `204 No Content`, `404 Not Found`, `400 Bad Request`.  
- [x] Xử lý các trường hợp không tìm thấy ID và validate dữ liệu đầu vào.  

### Task 5: Kiểm thử API
- [ ] Tạo collection Postman (hoặc sử dụng cURL/HTTP client tương đương).  
- [ ] Kiểm thử toàn bộ endpoints với các case: tạo mới, lấy chi tiết, cập nhật, xóa, lỗi không tìm thấy.  
- [ ] Lưu lại minh chứng (ảnh chụp hoặc export collection).

---

## 🧪 Tiêu chí kiểm thử gợi ý
- [ ] `POST` trả về `201 Created` và body chứa thông tin sinh viên mới.  
- [ ] `GET` danh sách trả về mảng có độ dài ≥ 0.  
- [ ] `GET {id}` trả về `200 OK` với đúng bản ghi; trả `404` nếu ID không tồn tại.  
- [ ] `PUT {id}` cập nhật trường dữ liệu mong muốn; trả `404` nếu ID không tồn tại.  
- [ ] `DELETE {id}` trả về `204 No Content`; gọi lại `GET {id}` phải trả `404`.

---

## 🏷️ Giai đoạn 3 | Độ khó: Beginner
- [ ] Sau khi hoàn thành, bạn có thể tích hợp UI (React) hoặc viết test tự động cho Controller để đảm bảo chất lượng.

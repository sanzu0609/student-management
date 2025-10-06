# 📌 Giai đoạn 4: Validation dữ liệu & Thông báo lỗi API

## 🎯 Mục tiêu
Đảm bảo dữ liệu gửi lên khi **tạo/cập nhật** sinh viên là hợp lệ; nếu không hợp lệ, API trả về **400 Bad Request** với thông điệp lỗi rõ ràng.

---

## ✅ Acceptance Criteria
- [x] Thêm dependency `spring-boot-starter-validation` vào `pom.xml`.  
- [x] Áp dụng **validation annotations** lên các thuộc tính của `Student` (ví dụ: `@NotBlank`, `@Email`, `@Past`).  
- [x] Kích hoạt validation trong `StudentController` cho các endpoint `POST` và `PUT`.  
- [ ] Khi dữ liệu không hợp lệ, API **trả 400 Bad Request** kèm thông tin lỗi dễ hiểu.  

---

## 📂 Tasks

### Task 1: Bổ sung dependency Validation
- [x] Cập nhật `pom.xml` để có `spring-boot-starter-validation`.

### Task 2: Khai báo ràng buộc trên model
- [x] Xác định các ràng buộc tối thiểu cho `Student`:  
  - [x] `firstName`: không để trống.  
  - [x] `lastName`: không để trống.  
  - [x] `email`: đúng định dạng, duy nhất.  
  - [x] `dateOfBirth`: là ngày trong quá khứ (không phải tương lai).  

### Task 3: Kích hoạt validation ở Controller
- [x] Thêm `@Valid` vào tham số request của các method `POST` và `PUT`.  
- [x] Tiếp nhận kết quả kiểm tra qua `BindingResult` (nếu sử dụng) hoặc cơ chế xử lý lỗi chuẩn của Spring.  

### Task 4: Chuẩn hóa phản hồi lỗi
- [ ] Thiết kế **thông điệp lỗi** rõ ràng, gồm: field, message, rejectedValue (nếu cần).  
- [ ] Đảm bảo HTTP status là **400** khi dữ liệu không hợp lệ.  
- [ ] (Tùy chọn) Xây dựng cấu trúc error response thống nhất (code, message, errors[]).  

### Task 5: Kiểm thử Validation
- [ ] Thử `POST/PUT` với dữ liệu thiếu/không hợp lệ (vd: email sai định dạng, firstName rỗng, dateOfBirth tương lai).  
- [ ] Xác nhận API trả **400** và thông báo lỗi **đúng field**, **đúng thông điệp**.  
- [ ] Ghi lại các case test trong Postman collection (hoặc tài liệu kèm theo).

---

## 🧪 Checklist test gợi ý
- [ ] `POST /api/v1/students` với `email = "abc"` → 400 + thông điệp lỗi về email.  
- [ ] `POST` với `firstName = ""` hoặc chỉ khoảng trắng → 400 + lỗi `NotBlank`.  
- [ ] `PUT {id}` với `dateOfBirth` là ngày tương lai → 400 + lỗi `@Past`.  
- [ ] `PUT {id}` với payload hợp lệ → 200/204 và bản ghi được cập nhật.  

---

## 📝 Lưu ý triển khai
- [ ] Dùng annotation validation từ **Jakarta Validation** (ví dụ `jakarta.validation.constraints.*`).  
- [ ] Đảm bảo **localization** thông điệp (nếu cần) qua `messages.properties` (tùy chọn).  
- [ ] Với ràng buộc **email duy nhất**, cần kiểm tra ở Service/Repository và trả lỗi phù hợp (có thể 409 Conflict; hoặc 400 kèm thông điệp).  

---

## 🏷️ Giai đoạn 4 | Độ khó: Intermediate
- [ ] Sau khi hoàn tất, các API của bạn có **ràng buộc dữ liệu** rõ ràng và **phản hồi lỗi** nhất quán, sẵn sàng cho bước tiếp theo (Global Exception Handling, tiêu chuẩn hóa response).

# 📌 Giai đoạn 5: Global Exception Handling & Response lỗi thống nhất

## 🎯 Mục tiêu
Thiết lập cơ chế xử lý lỗi tập trung (global) để **chuẩn hóa cấu trúc JSON** của response lỗi và xử lý đúng mã trạng thái HTTP. Bao gồm trường hợp không tìm thấy sinh viên.

---

## ✅ Acceptance Criteria
- [x] Tạo **custom exception** `ResourceNotFoundException`.  
- [x] Trong `StudentService`, **ném** `ResourceNotFoundException` khi không tìm thấy sinh viên theo ID.  
- [x] Tạo `GlobalExceptionHandler` và đánh dấu bằng `@ControllerAdvice`.  
- [x] Trong `GlobalExceptionHandler`, xử lý:  
  - [x] `ResourceNotFoundException` → trả **404 Not Found**.  
  - [x] `MethodArgumentNotValidException` (lỗi validation) → trả **400 Bad Request**.  
- [x] Response lỗi có **cấu trúc JSON thống nhất**, ví dụ:  
  - `{"timestamp":"...","status":404,"error":"Not Found","message":"Student not found with id: 99"}`

---

## 📂 Tasks

### Task 1: Định nghĩa exception tuỳ chỉnh
- [x] Tạo class `ResourceNotFoundException`.  
- [x] Xác định thông điệp lỗi nhận tham số (vd: id).  

### Task 2: Tích hợp vào Service
- [x] Cập nhật `StudentService` để ném `ResourceNotFoundException` khi không tìm thấy ID.  
- [x] Đảm bảo tất cả luồng đọc/cập nhật/xóa đều xử lý trường hợp không tìm thấy.  

### Task 3: Tạo Global Exception Handler
- [x] Tạo class `GlobalExceptionHandler` với `@ControllerAdvice`.  
- [x] Tạo method xử lý `ResourceNotFoundException` trả về **404**.  
- [x] Tạo method xử lý `MethodArgumentNotValidException` trả về **400**, tổng hợp lỗi field.  
- [x] Chuẩn hoá **cấu trúc lỗi JSON**: `timestamp`, `status`, `error`, `message`, (tùy chọn: `path`, `errors[]`).  

### Task 4: Đồng bộ thông điệp & i18n (tuỳ chọn)
- [x] Chuẩn hoá message tiếng Việt/Anh qua `messages.properties` (nếu cần).  
- [x] Đảm bảo message gọn, dễ hiểu với người dùng cuối.  

### Task 5: Kiểm thử
- [ ] Gọi `GET /.../students/{id}` với ID không tồn tại → **404** và message đúng.  
- [ ] Gọi `POST/PUT` với payload không hợp lệ → **400** và danh sách lỗi hợp lệ.  
- [ ] Kiểm tra timestamp, status, error, message đúng format.  
- [ ] Ghi lại case test trong Postman/cURL hoặc tài liệu kèm theo.  

---

## 🧪 Checklist test gợi ý
- [ ] **404**: `GET` sinh viên không tồn tại → status 404, `error="Not Found"`.  
- [ ] **400**: `POST` với email sai định dạng → status 400, có danh sách field lỗi.  
- [ ] **400**: `PUT` với `dateOfBirth` tương lai → status 400, thông điệp rõ ràng.  
- [ ] **OK**: `POST` hợp lệ → không bị GlobalExceptionHandler bắt lỗi.  

---

## 📝 Lưu ý triển khai
- [ ] Ưu tiên **Constructor Injection** thay vì field injection.  
- [ ] Không để lộ stacktrace/thông tin nhạy cảm trong response lỗi.  
- [ ] Cân nhắc logging ở mức **WARN/ERROR** cho các ngoại lệ được xử lý.  

---

## 🏷️ Giai đoạn 5 | Độ khó: Intermediate
- [ ] Sau khi hoàn tất, toàn bộ API có **chuẩn lỗi thống nhất**, giúp client/UI dễ parse và hiển thị.

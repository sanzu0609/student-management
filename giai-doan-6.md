# 📌 Giai đoạn 6: Pagination & Sorting cho API Students

## 🎯 Mục tiêu
Cải thiện `GET /api/v1/students` để hỗ trợ **phân trang** và **sắp xếp**, tránh trả về quá nhiều dữ liệu cùng lúc và tăng hiệu năng cho client.

---

## ✅ Acceptance Criteria
- [ ] `GET /api/v1/students` nhận các **query params**:  
  - [ ] `page` – số trang (bắt đầu từ 0).  
  - [ ] `size` – số bản ghi mỗi trang.  
  - [ ] `sort` – thuộc tính sắp xếp và chiều, ví dụ: `sort=lastName,asc` hoặc `sort=firstName,desc`.
- [ ] Response trả về đối tượng dạng **Page** với:  
  - [ ] Danh sách sinh viên của trang hiện tại.  
  - [ ] Thông tin phân trang: `totalElements`, `totalPages`, `size`, `number`, `first`, `last`, `numberOfElements`, `sort`...

---

## 📂 Tasks

### Task 1: Chuẩn bị repository hỗ trợ phân trang
- [x] Đảm bảo repository kế thừa `JpaRepository` (hoặc `PagingAndSortingRepository`).  
- [x] Sử dụng phương thức `findAll(Pageable pageable)` cho truy vấn phân trang.

### Task 2: Bổ sung tham số phân trang cho Controller
- [x] Cập nhật method `GET /api/v1/students` để nhận `Pageable` (hoặc `page`, `size`, `sort` rồi build `Pageable`).  
- [x] Hỗ trợ **nhiều tiêu chí sort** (tùy chọn), ví dụ: `sort=lastName,asc&sort=firstName,asc`.

### Task 3: Chuẩn hoá response
- [x] Trả trực tiếp `Page<Student>` hoặc một DTO bao bọc (`content` + `pageInfo`).  
- [x] Đảm bảo các trường phân trang quan trọng có trong response để client hiển thị UI phân trang.

### Task 4: Quy ước & giá trị mặc định
- [ ] Thiết lập **mặc định** cho `page=0`, `size` hợp lý (ví dụ 10 hoặc 20), `sort` mặc định (ví dụ `id,asc`).  
- [ ] Xử lý trường hợp `size` vượt ngưỡng tối đa (ví dụ >100) để tránh abuse.

### Task 5: Kiểm thử
- [ ] Kiểm thử với các bộ tham số:  
  - [ ] `?page=0&size=5`  
  - [ ] `?page=1&size=5&sort=lastName,asc`  
  - [ ] `?page=0&size=10&sort=firstName,desc&sort=lastName,asc`  
- [ ] Xác nhận các trường phân trang trả về đúng: `totalElements`, `totalPages`, `size`, `number`, `content.length`.  
- [ ] Kiểm thử trường hợp tham số không hợp lệ (size âm, sort sai field) → trả về lỗi phù hợp.

---

## 🧪 Checklist test gợi ý (Postman/cURL)
- [ ] `GET /api/v1/students?page=0&size=10` → `content.length ≤ 10`.  
- [ ] `GET ...&sort=lastName,asc` → danh sách sắp xếp tăng dần theo `lastName`.  
- [ ] `GET ...&size=200` (vượt max) → bị chặn hoặc giảm về ngưỡng tối đa.  
- [ ] Điều hướng trang (`page` tăng/giảm) → `first/last` thay đổi chính xác.

---

## 📝 Lưu ý triển khai
- [ ] Ưu tiên **Constructor Injection** trong Controller/Service.  
- [ ] Nếu trả `Page<Student>` trực tiếp, cân nhắc ẩn bớt trường không cần thiết (chọn DTO nếu muốn kiểm soát format).  
- [ ] Có thể bật **Jackson `Page` serializer** mặc định hoặc tự mapping sang cấu trúc ổn định.

---

## 🎯 Giai đoạn 6 | Độ khó: Intermediate
- [ ] Sau khi hoàn tất, API sẵn sàng phục vụ dataset lớn với UX phân trang/sắp xếp mượt trên UI.

# 📌 Giai đoạn 7: Unit Test cho StudentService (JUnit 5 + Mockito)

## 🎯 Mục tiêu
Viết **unit test** cho các phương thức trong `StudentService` để kiểm tra logic nghiệp vụ một cách độc lập, **không phụ thuộc vào database thật** (mock `StudentRepository`).

---

## ✅ Acceptance Criteria
- [ ] Tạo test class `StudentServiceTest`.  
- [ ] Sử dụng Mockito để **mock** `StudentRepository`.  
- [ ] Viết test case cho các kịch bản chính:  
  - [ ] Tìm thấy sinh viên.  
  - [ ] Không tìm thấy sinh viên.  
  - [ ] Tạo sinh viên thành công.  
  - [ ] Cập nhật/xóa không tìm thấy sinh viên.  
  - [ ] Các trường hợp lỗi/ngạnh khác liên quan tới nghiệp vụ.  
- [ ] Đảm bảo test **bao phủ** được các logic quan trọng của `StudentService` (happy path + edge cases).  

---

## 📂 Tasks

### Task 1: Chuẩn bị lớp test và cấu hình Mockito
- [ ] Tạo class `StudentServiceTest`.  
- [ ] Áp dụng JUnit 5 + Mockito (annotation gợi ý): `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`.  
- [ ] Khởi tạo `StudentService` với `StudentRepository` đã mock.  

### Task 2: Viết test cho chức năng đọc
- [ ] **getAllStudents**: trả về danh sách đúng số lượng/phần tử.  
- [ ] **getStudentById** — *tìm thấy*: trả về đối tượng đúng.  
- [ ] **getStudentById** — *không tìm thấy*: ném exception phù hợp (ví dụ `ResourceNotFoundException`).  

### Task 3: Viết test cho chức năng tạo
- [ ] **createStudent** — happy path: trả về đối tượng đã tạo với thuộc tính hợp lệ.  
- [ ] **createStudent** — business rule (nếu có): ví dụ trùng email → ném lỗi.  
- [ ] **verify()** các tương tác với repository (`save()`, `existsBy...()`).  

### Task 4: Viết test cho chức năng cập nhật
- [ ] **updateStudent** — *tìm thấy*: cập nhật thành công, giá trị mới đúng.  
- [ ] **updateStudent** — *không tìm thấy*: ném `ResourceNotFoundException`.  
- [ ] **updateStudent** — ràng buộc nghiệp vụ (nếu có): ví dụ email trùng người khác → ném lỗi.  

### Task 5: Viết test cho chức năng xóa
- [ ] **deleteStudent** — *tìm thấy*: gọi `deleteById()` đúng tham số.  
- [ ] **deleteStudent** — *không tìm thấy*: ném `ResourceNotFoundException`.  

### Task 6: Bảo đảm chất lượng test
- [ ] Kiểm tra **verifyNoMoreInteractions** với repository (khi cần).  
- [ ] Đặt tên test **theo Given-When-Then** để dễ đọc.  
- [ ] Tách **test data builder** (nếu cần) để tái sử dụng dữ liệu mẫu.  

---

## 🧪 Ma trận kịch bản gợi ý

| Nhóm | Kịch bản | Mong đợi |
|---|---|---|
| Read | getAllStudents (có dữ liệu) | Trả list với kích thước > 0 |
| Read | getAllStudents (rỗng) | Trả list rỗng (không lỗi) |
| Read | getStudentById — tồn tại | Trả đúng student |
| Read | getStudentById — không tồn tại | Ném `ResourceNotFoundException` |
| Create | Tạo mới — hợp lệ | Gọi `save()`, trả về đối tượng với ID (nếu logic có) |
| Create | Email trùng | Ném lỗi nghiệp vụ, **không gọi** `save()` |
| Update | Cập nhật — tồn tại | Gọi `findById()` + `save()`, trả giá trị cập nhật |
| Update | Cập nhật — không tồn tại | Ném `ResourceNotFoundException` |
| Delete | Xóa — tồn tại | Gọi `deleteById()` đúng tham số |
| Delete | Xóa — không tồn tại | Ném `ResourceNotFoundException` |

---

## 🔧 Gợi ý kỹ thuật (không code)
- [ ] Sử dụng các **phương thức Mockito**: `when()`, `thenReturn()`, `thenThrow()`, `verify()`, `times()`, `never()`.  
- [ ] Dùng **assertions** của JUnit 5: `assertEquals`, `assertTrue`, `assertThrows`, `assertAll`, `assertNotNull`.  
- [ ] Chuẩn hóa **naming**: `methodName_condition_expectedResult`.  
- [ ] Dùng **Given-When-Then** trong phần arrange/act/assert để dễ đọc.  

---

## 📈 Mục tiêu bao phủ & chất lượng
- [ ] **Line/Branch coverage** cho `StudentService` đạt mức hợp lý (ví dụ ≥ 80%).  
- [ ] Bao phủ **happy path** + **negative path** + **edge cases**.  
- [ ] Test **độc lập** (không gọi DB thật, không phụ thuộc IO ngoài).  

---

## ▶️ Cách chạy test (tham khảo)
- [ ] Chạy bằng Maven: `mvn -q -Dtest=StudentServiceTest test` hoặc `mvn test`.  
- [ ] Tích hợp CI (tuỳ chọn): đảm bảo job CI fail khi test fail.

---

## 🏷️ Giai đoạn 7 | Độ khó: Intermediate
- [ ] Sau khi hoàn thành, `StudentService` có bộ unit test vững, giúp tự tin refactor/mở rộng nghiệp vụ về sau.

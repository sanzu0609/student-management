package com.example.studentmanagement.controller;

import com.example.studentmanagement.controller.dto.PageResponse;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public PageResponse<Student> getAllStudents(
        @PageableDefault(
            page = 0,
            size = DEFAULT_PAGE_SIZE,
            sort = "id",
            direction = Sort.Direction.ASC
        ) Pageable pageable,
        HttpServletRequest request
    ) {
        Pageable sanitized = sanitizePageable(pageable, request);
        Page<Student> page = studentService.getStudents(sanitized);
        return PageResponse.from(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
        @PathVariable Long id,
        @Valid @RequestBody Student student
    ) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable sanitizePageable(Pageable pageable, HttpServletRequest request) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int size = pageable.getPageSize();

        String sizeParam = request.getParameter("size");
        if (sizeParam != null) {
            try {
                int requestedSize = Integer.parseInt(sizeParam);
                if (requestedSize < 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error.page.size.negative");
                }
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error.page.size.invalid");
            }
        }

        if (size < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error.page.size.negative");
        }
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        if (page != pageable.getPageNumber() || size != pageable.getPageSize()) {
            return PageRequest.of(page, size, pageable.getSort());
        }

        return pageable;
    }
}

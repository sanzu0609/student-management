package com.example.studentmanagement.service;

import com.example.studentmanagement.exception.InvalidStudentDataException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("studentPersistenceService")
public class StudentService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Page<Student> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    public Student createStudent(Student student) {
        validateStudent(student);
        resetIdentifier(student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existing = getStudentById(id);

        validateStudent(studentDetails);

        existing.setFirstName(studentDetails.getFirstName().trim());
        existing.setLastName(studentDetails.getLastName().trim());
        existing.setEmail(studentDetails.getEmail().trim());
        existing.setDateOfBirth(studentDetails.getDateOfBirth());

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student existing = getStudentById(id);
        studentRepository.deleteById(existing.getId());
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new InvalidStudentDataException("student", null, "error.student.payload-null");
        }

        String firstName = student.getFirstName();
        if (!StringUtils.hasText(firstName)) {
            throw new InvalidStudentDataException("firstName", firstName, "error.student.first-name.required");
        }

        String lastName = student.getLastName();
        if (!StringUtils.hasText(lastName)) {
            throw new InvalidStudentDataException("lastName", lastName, "error.student.last-name.required");
        }

        String email = student.getEmail();
        if (!StringUtils.hasText(email)) {
            throw new InvalidStudentDataException("email", email, "error.student.email.required");
        }

        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new InvalidStudentDataException("email", trimmedEmail, "error.student.email.invalid");
        }

        if (student.getDateOfBirth() == null) {
            throw new InvalidStudentDataException("dateOfBirth", null, "error.student.date-of-birth.required");
        }
    }

    private void resetIdentifier(Student student) {
        student.setId(null);
        student.setFirstName(student.getFirstName().trim());
        student.setLastName(student.getLastName().trim());
        student.setEmail(student.getEmail().trim());
    }
}

package com.example.studentmanagement.service;

import com.example.studentmanagement.exception.InvalidStudentDataException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
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

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        validateStudent(student);
        resetIdentifier(student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException(id));

        validateStudent(studentDetails);

        existing.setFirstName(studentDetails.getFirstName().trim());
        existing.setLastName(studentDetails.getLastName().trim());
        existing.setEmail(studentDetails.getEmail().trim());
        existing.setDateOfBirth(studentDetails.getDateOfBirth());

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new InvalidStudentDataException("Student payload must not be null.");
        }
        if (!StringUtils.hasText(student.getFirstName())) {
            throw new InvalidStudentDataException("First name is required.");
        }
        if (!StringUtils.hasText(student.getLastName())) {
            throw new InvalidStudentDataException("Last name is required.");
        }
        if (!StringUtils.hasText(student.getEmail())) {
            throw new InvalidStudentDataException("Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail().trim()).matches()) {
            throw new InvalidStudentDataException("Email format is invalid.");
        }
        if (student.getDateOfBirth() == null) {
            throw new InvalidStudentDataException("Date of birth is required.");
        }
    }

    private void resetIdentifier(Student student) {
        student.setId(null);
        student.setFirstName(student.getFirstName().trim());
        student.setLastName(student.getLastName().trim());
        student.setEmail(student.getEmail().trim());
    }
}
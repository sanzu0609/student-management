package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service("studentPersistenceService")
public class StudentService {

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
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));

        existing.setFirstName(studentDetails.getFirstName());
        existing.setLastName(studentDetails.getLastName());
        existing.setEmail(studentDetails.getEmail());
        existing.setDateOfBirth(studentDetails.getDateOfBirth());

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }
}

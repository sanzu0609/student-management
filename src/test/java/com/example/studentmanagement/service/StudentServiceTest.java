package com.example.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void studentServiceIsInitialisedWithMocks() {
        // Verifies @InjectMocks wires the service under test with mocked dependencies.
        assertNotNull(studentService);
    }

    @Test
    void getAllStudents_whenRepositoryReturnsStudents_returnsCompleteList() {
        List<Student> students = List.of(
            studentWithId(1L, "Alice"),
            studentWithId(2L, "Bob")
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(students.size(), result.size());
        assertEquals(students, result);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllStudents_whenRepositoryReturnsEmptyList_returnsEmptyList() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentService.getAllStudents();

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_whenStudentExists_returnsStudent() {
        long studentId = 10L;
        Student expected = studentWithId(studentId, "Charlie");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(expected));

        Student actual = studentService.getStudentById(studentId);

        assertEquals(expected, actual);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void getStudentById_whenStudentDoesNotExist_throwsResourceNotFound() {
        long studentId = 404L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(studentId));
        verify(studentRepository, times(1)).findById(studentId);
    }

    private Student studentWithId(Long id, String firstName) {
        Student student = new Student(
            firstName,
            "Nguyen",
            firstName.toLowerCase() + "@example.com",
            LocalDate.of(2000, 1, 1)
        );
        student.setId(id);
        return student;
    }
}

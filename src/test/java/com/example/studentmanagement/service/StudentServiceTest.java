package com.example.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.studentmanagement.exception.InvalidStudentDataException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    @Test
    void createStudent_whenPayloadValid_resetsIdentifierTrimsAndSaves() {
        Student input = studentWithId(55L, "  Diana  ");
        input.setLastName(" Prince ");
        input.setEmail(" diana.prince@themyscira.org ");

        Student saved = studentWithId(1L, "Diana");
        saved.setLastName("Prince");
        saved.setEmail("diana.prince@themyscira.org");
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        Student result = studentService.createStudent(input);

        assertEquals(saved, result);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).save(studentCaptor.capture());

        Student persistedArgument = studentCaptor.getValue();
        assertEquals(null, persistedArgument.getId());
        assertEquals("Diana", persistedArgument.getFirstName());
        assertEquals("Prince", persistedArgument.getLastName());
        assertEquals("diana.prince@themyscira.org", persistedArgument.getEmail());
    }

    @Test
    void createStudent_whenPayloadMissingFirstName_throwsInvalidStudentDataException() {
        Student invalid = new Student(
            " ",
            "Kent",
            "clark.kent@dailyplanet.com",
            LocalDate.of(1998, 6, 18)
        );

        assertThrows(InvalidStudentDataException.class, () -> studentService.createStudent(invalid));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void updateStudent_whenStudentExists_updatesFieldsAndSavesTrimmedData() {
        long studentId = 101L;
        Student existing = studentWithId(studentId, "Old");
        existing.setLastName("Value");
        existing.setEmail("old.value@example.com");
        existing.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Student payload = new Student(
            "  New  ",
            "  Name ",
            " new.name@example.com ",
            LocalDate.of(2001, 12, 3)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = studentService.updateStudent(studentId, payload);

        assertEquals(studentId, updated.getId());
        assertEquals("New", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertEquals("new.name@example.com", updated.getEmail());
        assertEquals(LocalDate.of(2001, 12, 3), updated.getDateOfBirth());

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(studentCaptor.capture());

        Student persisted = studentCaptor.getValue();
        assertEquals(studentId, persisted.getId());
        assertEquals("New", persisted.getFirstName());
        assertEquals("Name", persisted.getLastName());
        assertEquals("new.name@example.com", persisted.getEmail());
        assertEquals(LocalDate.of(2001, 12, 3), persisted.getDateOfBirth());
    }

    @Test
    void updateStudent_whenStudentMissing_throwsResourceNotFoundException() {
        long studentId = 202L;
        Student payload = new Student(
            "Bruce",
            "Wayne",
            "bruce.wayne@wayneenterprises.com",
            LocalDate.of(1995, 4, 17)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(studentId, payload));
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void updateStudent_whenPayloadInvalid_throwsInvalidStudentDataException() {
        long studentId = 303L;
        Student existing = studentWithId(studentId, "Existing");
        existing.setLastName("Student");
        existing.setEmail("existing.student@example.com");
        existing.setDateOfBirth(LocalDate.of(1993, 8, 9));

        Student invalidPayload = new Student(
            " ",
            "Rogers",
            "steve.rogers@avengers.com",
            LocalDate.of(1992, 7, 4)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existing));

        assertThrows(InvalidStudentDataException.class, () -> studentService.updateStudent(studentId, invalidPayload));
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any(Student.class));
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

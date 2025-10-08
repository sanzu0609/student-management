package com.example.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.studentmanagement.repository.StudentRepository;
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
}

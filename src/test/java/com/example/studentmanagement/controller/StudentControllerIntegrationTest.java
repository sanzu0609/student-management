package com.example.studentmanagement.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /students returns 201 and body")
    void createStudent_returnsCreated() throws Exception {
        String requestBody = """
            {
              \"firstName\": \" John \",
              \"lastName\": \" Doe \",
              \"email\": \"john.doe@example.com\",
              \"dateOfBirth\": \"2000-01-01\"
            }
            """;

        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("GET /students/{id} returns 200 when found")
    void getStudentById_returnsOk() throws Exception {
        Student saved = studentRepository.save(new Student(
            "Anna",
            "Nguyen",
            "anna.nguyen@example.com",
            LocalDate.of(1999, 5, 20)
        ));

        mockMvc.perform(get("/api/v1/students/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Anna"));
    }

    @Test
    @DisplayName("GET /students/{id} returns 404 when missing")
    void getStudentById_returnsNotFoundForMissing() throws Exception {
        mockMvc.perform(get("/api/v1/students/{id}", 9999L))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /students/{id} returns 200 and updates data")
    void updateStudent_returnsOk() throws Exception {
        Student saved = studentRepository.save(new Student(
            "Phong",
            "Tran",
            "phong.tran@example.com",
            LocalDate.of(1998, 3, 10)
        ));

        String updateBody = """
            {
              \"firstName\": \" Phong Updated \",
              \"lastName\": \" Tran Updated \",
              \"email\": \"phong.updated@example.com\",
              \"dateOfBirth\": \"1998-03-10\"
            }
            """;

        mockMvc.perform(put("/api/v1/students/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Phong Updated"))
            .andExpect(jsonPath("$.email").value("phong.updated@example.com"));
    }

    @Test
    @DisplayName("DELETE /students/{id} returns 204 and removes entity")
    void deleteStudent_returnsNoContent() throws Exception {
        Student saved = studentRepository.save(new Student(
            "Linh",
            "Pham",
            "linh.pham@example.com",
            LocalDate.of(2002, 7, 15)
        ));

        mockMvc.perform(delete("/api/v1/students/{id}", saved.getId()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/students/{id}", saved.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /students returns 400 with validation errors for invalid payload")
    void createStudent_returnsBadRequestForInvalidData() throws Exception {
        String requestBody = """
            {
              "firstName": "",
              "lastName": "Doe",
              "email": "not-an-email",
              "dateOfBirth": null
            }
            """;

        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
            .andExpect(jsonPath("$.errors[*].field", hasItem("firstName")))
            .andExpect(jsonPath("$.errors[*].field", hasItem("email")))
            .andExpect(jsonPath("$.errors[*].message", hasItem("must not be blank")));
    }

    @Test
    @DisplayName("PUT /students returns 400 when date of birth is in the future")
    void updateStudent_returnsBadRequestForFutureDate() throws Exception {
        Student saved = studentRepository.save(new Student(
            "Mai",
            "Le",
            "mai.le@example.com",
            LocalDate.of(2001, 9, 5)
        ));

        String futureDate = LocalDate.now().plusDays(1).toString();
        String updateBody = """
            {
              "firstName": "Mai",
              "lastName": "Le",
              "email": "mai.le@example.com",
              "dateOfBirth": "%s"
            }
            """.formatted(futureDate);

        mockMvc.perform(put("/api/v1/students/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
            .andExpect(jsonPath("$.errors[*].field", hasItem("dateOfBirth")))
            .andExpect(jsonPath("$.errors[*].message", hasItem("must be a past date")));
    }
}

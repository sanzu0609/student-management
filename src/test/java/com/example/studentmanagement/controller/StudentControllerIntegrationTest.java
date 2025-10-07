package com.example.studentmanagement.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.stream.IntStream;
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
    @DisplayName("GET /students returns paged payload with metadata")
    void getStudents_returnsPagedResponse() throws Exception {
        saveStudent("Cathy", "Adams", "cathy.adams@example.com", LocalDate.of(1997, 4, 12));
        saveStudent("Brian", "Doe", "brian.doe@example.com", LocalDate.of(1996, 6, 22));
        saveStudent("Anna", "Smith", "anna.smith@example.com", LocalDate.of(1995, 2, 3));

        mockMvc.perform(get("/api/v1/students")
                .param("page", "0")
                .param("size", "2")
                .param("sort", "lastName,asc")
                .param("sort", "firstName,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].lastName").value("Adams"))
            .andExpect(jsonPath("$.content[1].lastName").value("Doe"))
            .andExpect(jsonPath("$.page.number").value(0))
            .andExpect(jsonPath("$.page.size").value(2))
            .andExpect(jsonPath("$.page.totalElements").value(3))
            .andExpect(jsonPath("$.page.totalPages").value(2))
            .andExpect(jsonPath("$.page.first").value(true))
            .andExpect(jsonPath("$.page.last").value(false))
            .andExpect(jsonPath("$.page.numberOfElements").value(2))
            .andExpect(jsonPath("$.page.sort", hasSize(2)))
            .andExpect(jsonPath("$.page.sort[0].property").value("lastName"))
            .andExpect(jsonPath("$.page.sort[0].direction").value("ASC"));
    }

    @Test
    @DisplayName("GET /students?page=0&size=5 limits page content")
    void getStudents_pageZeroSizeFive() throws Exception {
        saveStudentsWithSequentialLastNames(8);

        mockMvc.perform(get("/api/v1/students")
                .param("page", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(5))
            .andExpect(jsonPath("$.page.number").value(0))
            .andExpect(jsonPath("$.page.size").value(5));
    }

    @Test
    @DisplayName("GET /students?page=1&size=5&sort=lastName,asc returns second page sorted")
    void getStudents_pageOneSortedByLastName() throws Exception {
        saveStudentsWithSequentialLastNames(12);

        mockMvc.perform(get("/api/v1/students")
                .param("page", "1")
                .param("size", "5")
                .param("sort", "lastName,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page.number").value(1))
            .andExpect(jsonPath("$.page.size").value(5))
            .andExpect(jsonPath("$.page.totalElements").value(12))
            .andExpect(jsonPath("$.page.totalPages").value(3))
            .andExpect(jsonPath("$.page.first").value(false))
            .andExpect(jsonPath("$.page.last").value(false))
            .andExpect(jsonPath("$.content[0].lastName").value("LastF"));
    }

    @Test
    @DisplayName("GET /students with multiple sort criteria applies ordering correctly")
    void getStudents_multipleSortCriteria() throws Exception {
        saveStudent("Charlie", "Nguyen", "charlie.nguyen@example.com", LocalDate.of(1998, 1, 1));
        saveStudent("Alice", "Nguyen", "alice.nguyen@example.com", LocalDate.of(1999, 1, 1));
        saveStudent("Bob", "Nguyen", "bob.nguyen@example.com", LocalDate.of(1997, 1, 1));

        mockMvc.perform(get("/api/v1/students")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "lastName,asc")
                .param("sort", "firstName,desc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].firstName").value("Charlie"))
            .andExpect(jsonPath("$.content[1].firstName").value("Bob"))
            .andExpect(jsonPath("$.content[2].firstName").value("Alice"));
    }

    @Test
    @DisplayName("GET /students applies default paging when parameters are absent")
    void getStudents_appliesDefaultPaging() throws Exception {
        saveStudent("Alpha", "One", "alpha.one@example.com", LocalDate.of(2000, 1, 1));
        saveStudent("Bravo", "Two", "bravo.two@example.com", LocalDate.of(2000, 2, 2));

        mockMvc.perform(get("/api/v1/students"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page.number").value(0))
            .andExpect(jsonPath("$.page.size").value(20))
            .andExpect(jsonPath("$.page.sort", hasSize(1)))
            .andExpect(jsonPath("$.page.sort[0].property").value("id"))
            .andExpect(jsonPath("$.page.sort[0].direction").value("ASC"));
    }

    @Test
    @DisplayName("GET /students caps requested size at maximum limit")
    void getStudents_capsPageSize() throws Exception {
        saveStudent("Gamma", "Three", "gamma.three@example.com", LocalDate.of(2000, 3, 3));

        mockMvc.perform(get("/api/v1/students").param("size", "200"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page.size").value(100))
            .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @DisplayName("GET /students rejects negative page size")
    void getStudents_rejectsNegativePageSize() throws Exception {
        mockMvc.perform(get("/api/v1/students").param("size", "-5"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Page size must be greater than zero."));
    }

    @Test
    @DisplayName("GET /students with invalid sort property returns 400")
    void getStudents_invalidSortProperty() throws Exception {
        mockMvc.perform(get("/api/v1/students").param("sort", "unknown,asc"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Invalid sort property: unknown"));
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
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Student not found with id: 9999"))
            .andExpect(jsonPath("$.path").value("/api/v1/students/9999"))
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.timestamp").exists());
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
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Student not found with id: " + saved.getId()))
            .andExpect(jsonPath("$.path").value("/api/v1/students/" + saved.getId()))
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.timestamp").exists());
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
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Request validation failed."))
            .andExpect(jsonPath("$.path").value("/api/v1/students"))
            .andExpect(jsonPath("$.errors[*].field", hasItem("firstName")))
            .andExpect(jsonPath("$.errors[*].field", hasItem("email")))
            .andExpect(jsonPath("$.errors[*].message", hasItem("must not be blank")))
            .andExpect(jsonPath("$.timestamp").exists());
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
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Request validation failed."))
            .andExpect(jsonPath("$.path").value("/api/v1/students/" + saved.getId()))
            .andExpect(jsonPath("$.errors[*].field", hasItem("dateOfBirth")))
            .andExpect(jsonPath("$.errors[*].message", hasItem("must be a past date")))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    private void saveStudent(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        studentRepository.save(new Student(firstName, lastName, email, dateOfBirth));
    }

    private void saveStudentsWithSequentialLastNames(int count) {
        IntStream.range(0, count).forEach(i -> {
            char suffix = (char) ('A' + i);
            saveStudent(
                "Student" + suffix,
                "Last" + suffix,
                "student" + suffix + "@example.com",
                LocalDate.of(1990, 1, 1).plusDays(i)
            );
        });
    }
}

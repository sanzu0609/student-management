package com.example.studentmanagement.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
        @NotBlank(message = "Full name is required") String fullName,
        @Email(message = "Email must be valid") @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Major is required") String major
) {
}

package com.demo.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    private Long id;
    private String name;
    @NotBlank(message = "Employee email cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotNull(message = "Salary cannot be null")
    @Positive(message = "Salary must be a positive value")
    private Double salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;
    private String departmentName;

}

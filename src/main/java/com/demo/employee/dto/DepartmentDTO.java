package com.demo.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO implements Serializable {

    private Long id;
    @NotNull(message = "Department name cannot be null")
    @NotBlank(message = "Department name cannot be blank")
    private String name;

}

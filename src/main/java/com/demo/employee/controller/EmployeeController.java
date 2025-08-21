package com.demo.employee.controller;

import com.demo.employee.api.EmployeeApi;
import com.demo.employee.dto.ApiResponseDTO;
import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController extends AbstractController implements EmployeeApi {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        return handleResponse(employeeService.createEmployee(dto), HttpStatus.CREATED, "Employee created successfully");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<List<EmployeeDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<EmployeeDTO> pagedEmployees = employeeService.getAllEmployees(PageRequest.of(page, size));
        return handlePaginatedResponse(pagedEmployees, pagedEmployees.getContent(), "Employees retrieved successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<EmployeeDTO>> getById(@PathVariable Long id) {
        return handleResponse(employeeService.getEmployeeById(id), "Employee retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto
    ) {
        return handleResponse(employeeService.updateEmployee(id, dto), "Employee updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return handleResponse(HttpStatus.OK, "Employee deleted successfully");
    }
}

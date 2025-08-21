package com.demo.employee.controller;

import com.demo.employee.api.DepartmentApi;
import com.demo.employee.dto.ApiResponseDTO;
import com.demo.employee.dto.DepartmentDTO;
import com.demo.employee.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController extends AbstractController implements DepartmentApi {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<DepartmentDTO>> createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        return handleResponse(departmentService.createDepartment(dto), HttpStatus.CREATED, "Department created successfully");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<List<DepartmentDTO>>> getAll() {
        return handleResponse(departmentService.getAllDepartments(), "Departments retrieved successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<DepartmentDTO>> getById(@PathVariable Long id) {
        return handleResponse(departmentService.getDepartmentById(id), "Department retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<DepartmentDTO>> update(@PathVariable Long id, @Valid @RequestBody DepartmentDTO dto) {
        return handleResponse(departmentService.updateDepartment(id, dto), "Department updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return handleResponse(HttpStatus.OK, "Department deleted successfully");
    }

}

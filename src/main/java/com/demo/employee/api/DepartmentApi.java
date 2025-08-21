package com.demo.employee.api;

import com.demo.employee.dto.ApiResponseDTO;
import com.demo.employee.dto.DepartmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department Management", description = "CRUD operations for departments")
@SecurityRequirement(name = "basicAuth")
public interface DepartmentApi {

    @Operation(summary = "Create new department")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Department created successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<ApiResponseDTO<DepartmentDTO>> createDepartment(@RequestBody DepartmentDTO dto);

    @Operation(summary = "Get all departments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<ApiResponseDTO<List<DepartmentDTO>>> getAll();

    @Operation(summary = "Get department by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    ResponseEntity<ApiResponseDTO<DepartmentDTO>> getById(@PathVariable Long id);

    @Operation(summary = "Update department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department updated successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    ResponseEntity<ApiResponseDTO<DepartmentDTO>> update(@PathVariable Long id, @RequestBody DepartmentDTO dto);

    @Operation(summary = "Delete department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id);
}

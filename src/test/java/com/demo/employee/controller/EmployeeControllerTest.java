package com.demo.employee.controller;

import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.exception.ResourceNotFoundException;
import com.demo.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("Mahmoud");
        employeeDTO.setEmail("mahmoud@example.com");
        employeeDTO.setSalary(50000.0);
        employeeDTO.setDepartmentId(100L);
    }

    @Test
    @DisplayName("Get all employees - 200 OK")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getAllEmployees_success() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        Page<EmployeeDTO> employeePage = new PageImpl<>(employees);
        Mockito.when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(employeePage);
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Mahmoud"));
    }

    @Test
    @DisplayName("Get all employees empty list - 200 OK")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getAllEmployees_empty() throws Exception {
        Page<EmployeeDTO> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(emptyPage);
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Get employee by id - 200 OK")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getEmployeeById_success() throws Exception {
        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Mahmoud"));
    }

    @Test
    @DisplayName("Get employee by id - 404 Not Found")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getEmployeeById_notFound() throws Exception {
        Mockito.when(employeeService.getEmployeeById(99L))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    @DisplayName("Create employee - 201 Created")
    @WithMockUser(roles = {"ADMIN"})
    void createEmployee_success() throws Exception {
        Mockito.when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Mahmoud"));
    }

    @Test
    @DisplayName("Create employee - 400 Bad Request")
    @WithMockUser(roles = {"ADMIN"})
    void createEmployee_invalid() throws Exception {
        EmployeeDTO invalid = new EmployeeDTO();

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Create employee - 403 Forbidden")
    @WithMockUser(roles = {"USER"})
    void createEmployee_forbidden() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update employee - 200 OK")
    @WithMockUser(roles = {"ADMIN"})
    void updateEmployee_success() throws Exception {
        EmployeeDTO updated = new EmployeeDTO();
        updated.setId(1L);
        updated.setName("Updated Mahmoud");
        updated.setEmail("updated@example.com");
        updated.setSalary(60000.0);
        updated.setDepartmentId(100L);

        Mockito.when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Mahmoud"));
    }

    @Test
    @DisplayName("Update employee - 404 Not Found")
    @WithMockUser(roles = {"ADMIN"})
    void updateEmployee_notFound() throws Exception {
        Mockito.when(employeeService.updateEmployee(eq(99L), any(EmployeeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(put("/api/employees/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    @DisplayName("Update employee - 403 Forbidden")
    @WithMockUser(roles = {"USER"})
    void updateEmployee_forbidden() throws Exception {
        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete employee - 200 OK")
    @WithMockUser(roles = {"ADMIN"})
    void deleteEmployee_success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete employee - 404 Not Found")
    @WithMockUser(roles = {"ADMIN"})
    void deleteEmployee_notFound() throws Exception {
        doThrow(new ResourceNotFoundException("Employee not found"))
                .when(employeeService).deleteEmployee(99L);

        mockMvc.perform(delete("/api/employees/99")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    @DisplayName("Delete employee - 403 Forbidden")
    @WithMockUser(roles = {"USER"})
    void deleteEmployee_forbidden() throws Exception {
        mockMvc.perform(delete("/api/employees/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Unsupported HTTP method - 405 Method Not Allowed")
    @WithMockUser(roles = {"ADMIN"})
    void unsupportedMethod() throws Exception {
        mockMvc.perform(patch("/api/employees/1")
                        .with(csrf()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Unsupported Media Type - 415")
    @WithMockUser(roles = {"ADMIN"})
    void unsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid")
                        .with(csrf()))
                .andExpect(status().isUnsupportedMediaType());
    }
}
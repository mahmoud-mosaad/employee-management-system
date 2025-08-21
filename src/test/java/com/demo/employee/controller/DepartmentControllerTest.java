package com.demo.employee.controller;

import com.demo.employee.dto.DepartmentDTO;
import com.demo.employee.exception.ResourceNotFoundException;
import com.demo.employee.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDepartment_Success() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("HR");

        DepartmentDTO returned = new DepartmentDTO();
        returned.setId(1L);
        returned.setName("HR");

        when(departmentService.createDepartment(any())).thenReturn(returned);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("HR"));

        verify(departmentService, times(1)).createDepartment(any());
    }

    @Test
    void testCreateDepartment_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateDepartment_Forbidden() throws Exception {
        DepartmentDTO dept = new DepartmentDTO();
        dept.setName("Test Department");
        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dept))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllDepartments_Success() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(1L);
        dto.setName("HR");

        when(departmentService.getAllDepartments()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("HR"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void testGetAllDepartments_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDepartmentById_Success() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(1L);
        dto.setName("HR");

        when(departmentService.getDepartmentById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("HR"));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDepartmentById_NotFound() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenThrow(new ResourceNotFoundException("Department not found"));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateDepartment_Success() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("Finance");

        DepartmentDTO returned = new DepartmentDTO();
        returned.setId(1L);
        returned.setName("Finance");

        when(departmentService.updateDepartment(eq(1L), any())).thenReturn(returned);

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Finance"));

        verify(departmentService, times(1)).updateDepartment(eq(1L), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteDepartment_Success() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    void testDeleteDepartment_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/departments/1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteDepartment_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/departments/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

}
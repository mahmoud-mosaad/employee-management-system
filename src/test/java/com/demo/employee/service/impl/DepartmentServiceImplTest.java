package com.demo.employee.service.impl;

import com.demo.employee.dto.DepartmentDTO;
import com.demo.employee.entity.Department;
import com.demo.employee.exception.ResourceNotFoundException;
import com.demo.employee.mapper.DepartmentMapper;
import com.demo.employee.repository.DepartmentRepository;
import com.demo.employee.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void testCreateDepartment_Success() {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("HR");

        Department entity = new Department();
        entity.setName("HR");

        Department savedEntity = new Department();
        savedEntity.setId(1L);
        savedEntity.setName("HR");

        DepartmentDTO expectedDto = new DepartmentDTO();
        expectedDto.setId(1L);
        expectedDto.setName("HR");

        when(departmentMapper.toEntity(dto)).thenReturn(entity);
        when(departmentRepository.save(entity)).thenReturn(savedEntity);
        when(departmentMapper.toDto(savedEntity)).thenReturn(expectedDto);

        DepartmentDTO result = departmentService.createDepartment(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("HR", result.getName());

        verify(departmentMapper).toEntity(dto);
        verify(departmentRepository).save(entity);
        verify(departmentMapper).toDto(savedEntity);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void testUpdateDepartment_Success() {
        Long deptId = 1L;
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("Finance");

        Department existing = new Department();
        existing.setId(deptId);
        existing.setName("Old Name");

        Department updatedEntity = new Department();
        updatedEntity.setId(deptId);
        updatedEntity.setName("Finance");

        DepartmentDTO expectedDto = new DepartmentDTO();
        expectedDto.setId(deptId);
        expectedDto.setName("Finance");

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(existing));
        when(departmentRepository.save(existing)).thenReturn(updatedEntity);
        when(departmentMapper.toDto(updatedEntity)).thenReturn(expectedDto);

        DepartmentDTO result = departmentService.updateDepartment(deptId, dto);

        assertNotNull(result);
        assertEquals(deptId, result.getId());
        assertEquals("Finance", result.getName());

        verify(departmentRepository).findById(deptId);
        verify(departmentRepository).save(existing);
        verify(departmentMapper).toDto(updatedEntity);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.updateDepartment(1L, new DepartmentDTO()));

        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(departmentRepository);
        verifyNoInteractions(departmentMapper);
    }

    @Test
    void testDeleteDepartment_Success() {
        Long deptId = 1L;
        Department department = new Department();
        department.setId(deptId);

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(deptId);

        verify(departmentRepository).findById(deptId);
        verify(departmentRepository).delete(department);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.deleteDepartment(1L));

        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void testGetDepartmentById_Success() {
        Long deptId = 1L;
        Department department = new Department();
        department.setId(deptId);
        department.setName("IT");

        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(deptId);
        dto.setName("IT");

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department)).thenReturn(dto);

        DepartmentDTO result = departmentService.getDepartmentById(deptId);

        assertNotNull(result);
        assertEquals(deptId, result.getId());
        assertEquals("IT", result.getName());

        verify(departmentRepository).findById(deptId);
        verify(departmentMapper).toDto(department);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getDepartmentById(1L));

        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void testGetAllDepartments() {
        Department dept1 = new Department();
        dept1.setId(1L);
        dept1.setName("HR");

        Department dept2 = new Department();
        dept2.setId(2L);
        dept2.setName("Finance");

        DepartmentDTO dto1 = new DepartmentDTO();
        dto1.setId(1L);
        dto1.setName("HR");

        DepartmentDTO dto2 = new DepartmentDTO();
        dto2.setId(2L);
        dto2.setName("Finance");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));
        when(departmentMapper.toDto(dept1)).thenReturn(dto1);
        when(departmentMapper.toDto(dept2)).thenReturn(dto2);

        List<DepartmentDTO> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("HR", result.get(0).getName());
        assertEquals("Finance", result.get(1).getName());

        verify(departmentRepository).findAll();
        verify(departmentMapper).toDto(dept1);
        verify(departmentMapper).toDto(dept2);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }
}

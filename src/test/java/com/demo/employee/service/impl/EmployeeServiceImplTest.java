package com.demo.employee.service.impl;

import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.entity.Department;
import com.demo.employee.entity.Employee;
import com.demo.employee.exception.ResourceNotFoundException;
import com.demo.employee.mapper.EmployeeMapper;
import com.demo.employee.repository.DepartmentRepository;
import com.demo.employee.repository.EmployeeRepository;
import com.demo.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testCreateEmployee_Success() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartmentId(1L);

        Employee employee = new Employee();
        employee.setName("John Doe");

        Department department = new Department();
        department.setId(1L);
        department.setName("HR");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("John Doe");
        savedEmployee.setDepartment(department);

        EmployeeDTO expectedDTO = new EmployeeDTO();
        expectedDTO.setId(1L);
        expectedDTO.setName("John Doe");
        expectedDTO.setDepartmentId(1L);

        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(employee)).thenReturn(savedEmployee);
        when(employeeMapper.toDto(savedEmployee)).thenReturn(expectedDTO);

        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals(1L, result.getDepartmentId());

        verify(employeeRepository, times(1)).save(employee);
        verify(departmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(employeeRepository, departmentRepository);
    }

    @Test
    void testUpdateEmployee_Success() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Jane Doe");
        employeeDTO.setDepartmentId(2L);

        Employee existing = new Employee();
        existing.setId(employeeId);
        existing.setName("Old Name");

        Department newDept = new Department();
        newDept.setId(2L);
        newDept.setName("Finance");

        Employee updated = new Employee();
        updated.setId(employeeId);
        updated.setName("Jane Doe");
        updated.setDepartment(newDept);

        EmployeeDTO expectedDTO = new EmployeeDTO();
        expectedDTO.setId(employeeId);
        expectedDTO.setName("Jane Doe");
        expectedDTO.setDepartmentId(2L);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existing));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDept));
        when(employeeRepository.save(existing)).thenReturn(updated);
        when(employeeMapper.toDto(updated)).thenReturn(expectedDTO);

        EmployeeDTO result = employeeService.updateEmployee(employeeId, employeeDTO);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals(2L, result.getDepartmentId());

        verify(employeeRepository).findById(employeeId);
        verify(departmentRepository).findById(2L);
        verify(employeeRepository).save(existing);
        verifyNoMoreInteractions(employeeRepository, departmentRepository);
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(1L, new EmployeeDTO()));

        verify(employeeRepository).findById(1L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testDeleteEmployee_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));

        verify(employeeRepository).findById(1L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetEmployeeById_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        EmployeeDTO result = employeeService.getEmployeeById(employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());

        verify(employeeRepository).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));

        verify(employeeRepository).findById(1L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetAllEmployees() {
        Employee e1 = new Employee();
        e1.setId(1L);
        Employee e2 = new Employee();
        e2.setId(2L);

        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setId(1L);
        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setId(2L);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Employee> page = new PageImpl<>(Arrays.asList(e1, e2), pageable, 2);

        when(employeeRepository.findAll(pageable)).thenReturn(page);
        when(employeeMapper.toDto(e1)).thenReturn(dto1);
        when(employeeMapper.toDto(e2)).thenReturn(dto2);

        Page<EmployeeDTO> result = employeeService.getAllEmployees(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());

        verify(employeeRepository).findAll(pageable);
        verifyNoMoreInteractions(employeeRepository);
    }
}

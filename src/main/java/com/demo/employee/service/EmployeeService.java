package com.demo.employee.service;

import com.demo.employee.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO dto);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO dto);
    void deleteEmployee(Long id);
    EmployeeDTO getEmployeeById(Long id);
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);

}

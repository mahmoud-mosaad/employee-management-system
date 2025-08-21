package com.demo.employee.mapper;

import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "department", ignore = true)
    Employee toEntity(EmployeeDTO dto);

}
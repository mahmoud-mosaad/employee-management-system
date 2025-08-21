package com.demo.employee.mapper;

import com.demo.employee.dto.DepartmentDTO;
import com.demo.employee.entity.Department;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDTO toDto(Department department);

    Department toEntity(DepartmentDTO departmentDTO);

}

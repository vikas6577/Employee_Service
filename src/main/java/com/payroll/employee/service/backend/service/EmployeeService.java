package com.payroll.employee.service.backend.service;


import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;

import java.util.Optional;

public interface EmployeeService {

    Optional<EmployeeDto> getEmployeeById(Long id);
    EmployeeDto createEmployee(EmployeeCreateDto employeeCreateDto);
    boolean deleteEmployee(Long id);
}

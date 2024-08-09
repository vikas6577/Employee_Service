package com.payroll.employee.service.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<EmployeeDto> getEmployeeById(Long employeeId)
    {
        return employeeRepository.findById(employeeId)
                .map(employeeEntity -> objectMapper.convertValue(employeeEntity,EmployeeDto.class));

    }
}

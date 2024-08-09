package com.payroll.employee.service.backend.controller;

import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    private ResponseEntity<EmployeeDto> getEmployeeById(Long employeeId)
    {
        Optional<EmployeeDto> employeeDto = employeeService.getEmployeeById(employeeId);

        return employeeDto
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))  // If present, return 200 OK with the EmployeeDto
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // If not present, return 404 Not Found
    }



}

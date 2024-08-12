package com.payroll.employee.service.backend.controller;

import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.service.EmployeeService;
import lombok.extern.flogger.Flogger;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    private ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id)
    {
        Optional<EmployeeDto> employeeDto = employeeService.getEmployeeById(id);

        return employeeDto
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))  // If present, return 200 OK with the EmployeeDto
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // If not present, return 404 Not Found
    }

    @PostMapping()
    private ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeCreateDto employeeCreateDto){
        EmployeeDto employeeDto=employeeService.createEmployee(employeeCreateDto);
        return new ResponseEntity<>(employeeDto,HttpStatus.CREATED);
    }




}

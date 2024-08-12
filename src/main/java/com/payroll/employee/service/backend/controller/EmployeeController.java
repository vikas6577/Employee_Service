package com.payroll.employee.service.backend.controller;

import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.PasswordDto;
import com.payroll.employee.service.backend.exception.ResourceNotFoundException;
import com.payroll.employee.service.backend.service.EmployeeService;
import lombok.extern.flogger.Flogger;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;
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

    @GetMapping
    private ResponseEntity<?> getAllEmployees() {
        List<EmployeeDto> employees=employeeService.getAllEmployees();
        if(employees.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    @PostMapping()
    private ResponseEntity<Map<String, Object>> createEmployee(@RequestBody EmployeeCreateDto employeeCreateDto){
        EmployeeDto employeeDto=employeeService.createEmployee(employeeCreateDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User Added Successfully");
        response.put("employee", employeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteEmployee(@PathVariable Long id){
        boolean deleted=employeeService.deleteEmployee(id);
        if(deleted){
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("No User found with the given id", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/employees/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody PasswordDto passwordDto) {
        try {
            employeeService.updatePassword(id, passwordDto);
            return ResponseEntity.ok("Password updated successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }



}

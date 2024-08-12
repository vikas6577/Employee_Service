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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    private ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable Long id)
    {
        Optional<EmployeeDto> employeeDto = employeeService.getEmployeeById(id);
        Map<String,Object> response= new HashMap<>();
        if(employeeDto.isPresent()){
            response.put("employee", employeeDto.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.put("message","No Employee present with the given id");
           return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
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

}

package com.payroll.employee.service.backend.controller;

import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.EmployeeUpdateDto;
import com.payroll.employee.service.backend.dto.PasswordDto;
import com.payroll.employee.service.backend.exception.ResourceNotFoundException;
import com.payroll.employee.service.backend.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.flogger.Flogger;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.csrf.CsrfToken;
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
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);


    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    private ResponseEntity<Map<String, Object>>getEmployeeById(@PathVariable Long id)
    {
        Map<String, Object> response = new HashMap<>();
        try{
            Optional<EmployeeDto> employeeDto = employeeService.getEmployeeById(id);
            if(employeeDto.isPresent()) {
                response.put("message", "User Details");
                response.put("employee", employeeDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else{
                response.put("ërror","Employee not found for id: "+ id);
                return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex){
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    private ResponseEntity<Map<String,Object>> getAllEmployees() {
        List<EmployeeDto> employees=employeeService.getAllEmployees();
        Map<String,Object> response=new HashMap<>();
        if(employees.isEmpty())
        {
            response.put("message","No user available");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        response.put("Employees",employees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    private ResponseEntity<Map<String, Object>> createEmployee(@RequestBody EmployeeCreateDto employeeCreateDto){
        Map<String, Object> response = new HashMap<>();
        try {
            EmployeeDto employeeDto = employeeService.createEmployee(employeeCreateDto);
            response.put("message", "User Added Successfully");
            response.put("employee", employeeDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
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

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody PasswordDto passwordDto) {
        try {
            log.info("Updating password for employee with id: " + id);
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

    @PatchMapping("/{id}/update")
    public ResponseEntity<String> updateDetails(@PathVariable Long id, @RequestBody EmployeeUpdateDto employeeUpdateDto){
       boolean Updated=employeeService.updateEmployee(id,employeeUpdateDto);
        if(Updated){
            return new ResponseEntity<>("Updated Successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Data Updation Failed", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployeesUnderManager(@PathVariable("id") Long managerId){
         List<EmployeeDto> employees=employeeService.getEmployeesUnderManager(managerId);
         return ResponseEntity.ok(employees);
    }

}

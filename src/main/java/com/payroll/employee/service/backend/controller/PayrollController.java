package com.payroll.employee.service.backend.controller;


import com.payroll.employee.service.backend.dto.PayrollDto;
import com.payroll.employee.service.backend.service.EmployeeService;
import com.payroll.employee.service.backend.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/allData")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping
   public ResponseEntity<List<PayrollDto>>getPayrollDataOfAllEmployees()
    {
        return new ResponseEntity<List<PayrollDto>>(payrollService.getPayrollDataOfAllEmployees(), HttpStatus.OK);
    }

}

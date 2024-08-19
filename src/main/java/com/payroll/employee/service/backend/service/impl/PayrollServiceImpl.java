package com.payroll.employee.service.backend.service.impl;

import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.PayrollDto;
import com.payroll.employee.service.backend.dto.SalaryDto;
import com.payroll.employee.service.backend.service.EmployeeService;
import com.payroll.employee.service.backend.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PayrollDto> getPayrollDataOfAllEmployees(){

        List<EmployeeDto> employeeDtoList=employeeService.getAllEmployees();
        List<PayrollDto> payrollDtoList=new ArrayList<>();


        String leaveUrl="http://localhost:8081/api/v1/leaves";

    }



    private List<SalaryDto> getAllEmployeeSalaries(){
        String salaryUrl="http://localhost:8081/api/v1/salaries";

    }

    private List<EmployeeDto>getlAllEmployeeLeaves(){
        String leaveUrl="http://localhost:8081/api/v1/leaves";
        ResponseEntity<SalaryDto[]>response=restTemplate.getForEntity(salaryUrl, SalaryDto[].class);
        SalaryDto[] salaries=response.getBody();
        return salaries !=null ? Arrays.asList(salaries):new ArrayList<>();

    }

}

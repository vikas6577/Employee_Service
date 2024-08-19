package com.payroll.employee.service.backend.service.impl;

import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.LeaveDto;
import com.payroll.employee.service.backend.dto.PayrollDto;
import com.payroll.employee.service.backend.dto.SalaryDto;
import com.payroll.employee.service.backend.service.EmployeeService;
import com.payroll.employee.service.backend.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayrollServiceImpl implements PayrollService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PayrollDto> getPayrollDataOfAllEmployees() {
        logger.info("Starting to fetch payroll data for all employees.");

        String leaveUrl = "http://localhost:8081/api/v1/leaves";
        String salaryUrl = "http://localhost:8081/api/v1/salaries";

        List<LeaveDto> leaveDtos = getAllEmployeesLeaves(leaveUrl);
        List<SalaryDto> salaryDtos = getAllEmployeesSalary(salaryUrl);
        List<EmployeeDto> employeeDtoList = employeeService.getAllEmployees();
        List<PayrollDto> payrollDtoList = mapToPayrollDtos(leaveDtos, salaryDtos, employeeDtoList);

        logger.info("Completed fetching payroll data for all employees.");
        return payrollDtoList;
    }

    public List<LeaveDto> getAllEmployeesLeaves(String leaveUrl) {
        logger.debug("Fetching leave data from URL: {}", leaveUrl);

        ResponseEntity<List<LeaveDto>> response = restTemplate.exchange(
                leaveUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LeaveDto>>() {}
        );

        List<LeaveDto> leaveDtos = response.getBody();
        logger.debug("Received {} leave records.", leaveDtos != null ? leaveDtos.size() : 0);
        return leaveDtos;
    }

    public List<SalaryDto> getAllEmployeesSalary(String salaryUrl) {
        logger.debug("Fetching salary data from URL: {}", salaryUrl);

        ResponseEntity<List<SalaryDto>> response = restTemplate.exchange(
                salaryUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SalaryDto>>() {}
        );

        List<SalaryDto> salaryDtos = response.getBody();
        logger.debug("Received {} salary records.", salaryDtos != null ? salaryDtos.size() : 0);
        return salaryDtos;
    }

    public List<PayrollDto> mapToPayrollDtos(List<LeaveDto> leaveDtos, List<SalaryDto> salaryDtos, List<EmployeeDto> employeeDtoList) {
        logger.debug("Mapping data to PayrollDto.");

        List<PayrollDto> payrollDtoList = new ArrayList<>();

        for (EmployeeDto employeeDto : employeeDtoList) {
            Long employeeId = employeeDto.getEmployeeId();

            // Find the corresponding LeaveDto and SalaryDto
            LeaveDto leaveDto = leaveDtos.stream()
                    .filter(leave -> leave.getEmployeeId().equals(employeeId))
                    .findFirst()
                    .orElse(null);

            SalaryDto salaryDto = salaryDtos.stream()
                    .filter(salary -> salary.getEmployeeId().equals(employeeId))
                    .findFirst()
                    .orElse(null);

            PayrollDto payrollDto = PayrollDto.builder()
                    .employeeId(employeeDto.getEmployeeId())
                    .email(employeeDto.getEmail())
                    .firstName(employeeDto.getFirstName())
                    .lastName(employeeDto.getLastName())
                    .phone(employeeDto.getPhone())
                    .birthDate(employeeDto.getBirthDate())
                    .role(employeeDto.getRole())
                    .reportsTo(employeeDto.getReportsTo())
                    .salary(salaryDto != null ? salaryDto.getSalary() : null)
                    .currentLeaves(leaveDto != null ? leaveDto.getCurrentLeaves() : null)
                    .totalLeaves(leaveDto != null ? leaveDto.getTotalLeaves() : null)
                    .build();

            payrollDtoList.add(payrollDto);
        }

        logger.debug("Mapped {} PayrollDto records.", payrollDtoList.size());
        return payrollDtoList;
    }
}

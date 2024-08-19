package com.payroll.employee.service.backend.service;

import com.payroll.employee.service.backend.dto.PayrollDto;

import java.util.List;

public interface PayrollService {

    List<PayrollDto> getPayrollDataOfAllEmployees();
}

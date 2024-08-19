package com.payroll.employee.service.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PayrollDto extends EmployeeDto{
    private Long salary;
    private Long currentLeaves;
    private Long totalLeaves;
}

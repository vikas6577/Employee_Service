package com.payroll.employee.service.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
public class PayrollDto extends EmployeeDto{
    private Long salary;
    private Long currentLeaves;
    private Long totalLeaves;
}

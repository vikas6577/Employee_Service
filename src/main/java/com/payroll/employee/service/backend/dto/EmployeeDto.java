package com.payroll.employee.service.backend.dto;

import com.payroll.employee.service.backend.enums.Designation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends EmployeeCreateDto {
    private Long employeeId;
    private String email;
}

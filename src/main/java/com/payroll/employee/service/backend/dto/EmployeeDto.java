package com.payroll.employee.service.backend.dto;

import com.payroll.employee.service.backend.enums.Designation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EmployeeDto extends EmployeeCreateDto {
    private Long employeeId;
    private String email;
}

package com.payroll.employee.service.backend.dto;

import com.payroll.employee.service.backend.enums.Designation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateDto {
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthDate;
    private Designation role;
    private Long reportsTo;
}

package com.payroll.employee.service.backend.dto;

import com.payroll.employee.service.backend.enums.Designation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate DOB;
    private Designation role;
    private Long reportsTo;
}

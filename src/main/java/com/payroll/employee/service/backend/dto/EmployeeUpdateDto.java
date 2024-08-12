package com.payroll.employee.service.backend.dto;


import com.payroll.employee.service.backend.enums.Designation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
    private Designation role;
    private Long reportsTo;
}

package com.payroll.employee.service.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDto {
    private Long employeeId;
    private Long currentLeaves;
    private Long totalLeaves;
}

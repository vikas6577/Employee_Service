package com.payroll.employee.service.backend.service;

import com.payroll.employee.service.backend.dto.LoginDto;

public interface LoginService {
    String login(LoginDto loginDto);
}

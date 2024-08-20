package com.payroll.employee.service.backend.service.impl;


import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        EmployeeEntity employee=employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Convert Designation enum to a GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole().name());
        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(),
                Collections.singletonList(authority));
    }
}

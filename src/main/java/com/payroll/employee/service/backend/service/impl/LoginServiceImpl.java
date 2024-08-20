package com.payroll.employee.service.backend.service.impl;

import com.payroll.employee.service.backend.dto.LoginDto;
import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.LoginService;
import com.payroll.employee.service.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userDetailsService;


    @Override
    public String login(LoginDto loginDto)
    {
        EmployeeEntity employee = employeeRepository.findByEmail(loginDto.getEmail());

        if (employee != null) {
            boolean isPasswordMatch =passwordEncoder.matches(loginDto.getPassword(), employee.getPassword());

            if (isPasswordMatch) {
                // Generate JWT token
                return jwtUtil.generateToken(employee.getEmail());
            }
        }

        return "Invalid username or password";
    //        try {
    //            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
    //            UserDetails userDetails =userDetailsService.loadUserByUsername(loginDto.getEmail());
    //            String token =jwtUtil.generateToken(userDetails.getUsername());
    //            return token;
    //        }catch (Exception e)
    //        {
    //            return "Bad credentials";
    //        }
    }


}

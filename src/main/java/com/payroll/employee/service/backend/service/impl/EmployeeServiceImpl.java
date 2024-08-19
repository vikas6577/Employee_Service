package com.payroll.employee.service.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.employee.service.backend.dto.*;
import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.enums.Designation;
import com.payroll.employee.service.backend.exception.ResourceNotFoundException;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<EmployeeDto> getEmployeeById(Long employeeId)
    {
        return employeeRepository.findById(employeeId)
                .map(employeeEntity -> objectMapper.convertValue(employeeEntity,EmployeeDto.class));

    }

    @Override
    public EmployeeDto createEmployee(EmployeeCreateDto employeeCreateDto) {
        try {
            if (phoneExists(employeeCreateDto.getPhone())) {
                throw new IllegalArgumentException("Phone number already exists: " + employeeCreateDto.getPhone());
            }

            Long lastEmployeeId = employeeRepository.findMaxEmployeeId();
            Long newEmployeeId = Optional.ofNullable(lastEmployeeId).map(id -> id + 1).orElse(1L);

            String password = employeeCreateDto.getFirstName() + employeeCreateDto.getLastName() + newEmployeeId;
            String encryptedPassword = passwordEncoder.encode(password);
            String email = employeeCreateDto.getFirstName() + "." + newEmployeeId + "." + employeeCreateDto.getLastName() + "@ukg.com";

            EmployeeEntity employee = EmployeeEntity.builder()
                    .employeeId(newEmployeeId)
                    .firstName(employeeCreateDto.getFirstName())
                    .lastName(employeeCreateDto.getLastName())
                    .phone(employeeCreateDto.getPhone())
                    .birthDate(employeeCreateDto.getBirthDate())
                    .role(employeeCreateDto.getRole())
                    .reportsTo(employeeCreateDto.getReportsTo())
                    .email(email)
                    .password(encryptedPassword)
                    .build();

            employeeRepository.save(employee);


            SalaryDto salaryDto = new SalaryDto(newEmployeeId, employeeCreateDto.getSalary());
            String createSalaryUrl = "http://localhost:8081/api/v1/salaries";
            restTemplate.postForObject(createSalaryUrl, salaryDto, Void.class);

            String createLeaveUrl = "http://localhost:8081/api/v1/leaves/" + newEmployeeId;
            restTemplate.postForObject(createLeaveUrl,null,Void.class);


            return convertToDto(employee);

        } catch (Exception e) {
            // Print the exception to the console
            System.err.println("Error occurred while creating employee: " + e.getMessage());
            e.printStackTrace();

            // Optionally, rethrow the exception or handle it as needed
            throw e; // or return a custom response or null
        }
    }


    private boolean phoneExists(String phone) {
        boolean phoneExist=employeeRepository.existsByPhone(phone);
        return phoneExist;
    }

    public boolean deleteEmployee(Long employeeId){
        Optional<EmployeeEntity> employeeData= employeeRepository.findById(employeeId);
            if(employeeData.isPresent()){
                employeeRepository.deleteById(employeeId);
                return true;
            }
            else{
                return false;
            }
    }


    private EmployeeDto convertToDto(EmployeeEntity employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employee.getEmployeeId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setPhone(employee.getPhone());
        employeeDto.setBirthDate(employee.getBirthDate());
        employeeDto.setRole(employee.getRole());
        employeeDto.setReportsTo(employee.getReportsTo());
        employeeDto.setEmail(employee.getEmail());
        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

        return employeeEntities
                .stream()
                .map(employeeEntity -> objectMapper.convertValue(employeeEntity,EmployeeDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<EmployeeDto> getEmployeesUnderManager(Long managerId) {
        if(!employeeRepository.existsByManagerID(managerId, Designation.MANAGER))
        {
            throw new ResourceNotFoundException("Manager not found");

        }

        List <EmployeeEntity> employeeEntities = employeeRepository.findAllByManagerID(managerId);

        List<EmployeeDto> employeeDtoList=employeeEntities.stream().map(
                this::convertToDto
        ).collect(Collectors.toList());

        return employeeDtoList;

    }


    @Override
    public boolean updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto) {
        try {
            EmployeeEntity employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
            employee.setRole(employeeUpdateDto.getRole());
            employee.setReportsTo(employeeUpdateDto.getReportsTo());
            employeeRepository.save(employee);
            return true;
        } catch (ResourceNotFoundException ex) {
            System.err.println(ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.err.println("An unexpected error occurred: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public String updatePassword(Long id, PasswordDto passwordDto)
    {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
        log.info("Employee found for id: {} ",employee );
        // Validate the provided old password against the stored hashed password
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), employee.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Encode the new password and set it
        String encodedNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        employee.setPassword(encodedNewPassword);

        // Save the updated employee entity
        employeeRepository.save(employee);

        return "Password updated successfully";
    }



    private void validatePassword(PasswordDto passwordDto,String actualPassword) {
        // Example validation logic
        if (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!passwordDto.getCurrentPassword().equals(actualPassword)) {
            throw new IllegalArgumentException("Old password does not match");
        }
    }


}

package com.payroll.employee.service.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.EmployeeUpdateDto;
import com.payroll.employee.service.backend.dto.PasswordDto;
import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.exception.ResourceNotFoundException;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public Optional<EmployeeDto> getEmployeeById(Long employeeId)
    {
        return employeeRepository.findById(employeeId)
                .map(employeeEntity -> objectMapper.convertValue(employeeEntity,EmployeeDto.class));

    }

    @Override
    public EmployeeDto createEmployee(EmployeeCreateDto employeeCreateDto) {
        if (phoneExists(employeeCreateDto.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists: " + employeeCreateDto.getPhone());
        }
        Long lastEmployeeId = employeeRepository.findMaxEmployeeId();
        Long newEmployeeId = Optional.ofNullable(lastEmployeeId).map(id -> id + 1).orElse(1L);

        String password = employeeCreateDto.getFirstName() + employeeCreateDto.getLastName() + newEmployeeId;
        String encryptedPassword= passwordEncoder.encode(password);
        String email = employeeCreateDto.getFirstName() + "." + newEmployeeId + "." + employeeCreateDto.getLastName()+"@ukg.com";

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
        return convertToDto(employee);
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

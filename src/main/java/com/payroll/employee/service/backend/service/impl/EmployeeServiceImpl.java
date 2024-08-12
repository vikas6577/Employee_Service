package com.payroll.employee.service.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.employee.service.backend.dto.EmployeeCreateDto;
import com.payroll.employee.service.backend.dto.EmployeeDto;
import com.payroll.employee.service.backend.dto.PasswordDto;
import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.enums.Designation;
import com.payroll.employee.service.backend.exception.ResourceNotFoundException;
import com.payroll.employee.service.backend.repository.EmployeeRepository;
import com.payroll.employee.service.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public Optional<EmployeeDto> getEmployeeById(Long employeeId)
    {
        return employeeRepository.findById(employeeId)
                .map(employeeEntity -> objectMapper.convertValue(employeeEntity,EmployeeDto.class));

    }

    @Override
    public EmployeeDto createEmployee(EmployeeCreateDto employeeCreateDto) {
        Long lastEmployeeId = employeeRepository.findMaxEmployeeId();

        Long newEmployeeId = Optional.ofNullable(lastEmployeeId)
                .map(id -> id + 1)
                .orElse(1L);


        log.info("Employee id is "+ newEmployeeId);
        String firstName=employeeCreateDto.getFirstName();
        String lastName=employeeCreateDto.getLastName();
        log.info("Phone number is "+employeeCreateDto.getPhone());
        String phone=employeeCreateDto.getPhone();

        log.info("DOB is "+ employeeCreateDto.getBirthDate());
        LocalDate dob=employeeCreateDto.getBirthDate();
        Designation designation=employeeCreateDto.getRole();
        Long reportsTo=employeeCreateDto.getReportsTo();
        String password=firstName+lastName+newEmployeeId;
        String email=firstName+"."+newEmployeeId+"."+lastName;

        EmployeeEntity Employee= new EmployeeEntity();
        Employee.setEmployeeId(newEmployeeId);
        Employee.setFirstName(firstName);
        Employee.setLastName(lastName);
        Employee.setPhone(phone);
        Employee.setBirthDate(dob);
        Employee.setRole(designation);
        Employee.setReportsTo(reportsTo);
        Employee.setEmail(email);
        Employee.setPassword(password);

        employeeRepository.save(Employee);
        System.out.println("Set data done");
        EmployeeDto employeeDto = convertToDto(Employee);
        System.out.println("data returnded from service");
        return employeeDto;
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
    public String updatePassword(Long id, PasswordDto passwordDto)
    {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
        validatePassword(passwordDto,employee.getPassword());

        employee.setPassword(passwordDto.getNewPassword());
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

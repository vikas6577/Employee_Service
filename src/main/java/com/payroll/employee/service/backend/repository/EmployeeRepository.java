package com.payroll.employee.service.backend.repository;

import com.payroll.employee.service.backend.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

}

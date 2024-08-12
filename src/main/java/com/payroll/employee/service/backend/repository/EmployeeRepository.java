package com.payroll.employee.service.backend.repository;

import com.payroll.employee.service.backend.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

    @Query(value = "SELECT e.employee_id FROM employee_detail e ORDER BY e.employee_id DESC LIMIT 1", nativeQuery = true)
    Long findMaxEmployeeId();
}

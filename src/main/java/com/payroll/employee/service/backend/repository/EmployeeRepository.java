package com.payroll.employee.service.backend.repository;

import com.payroll.employee.service.backend.entity.EmployeeEntity;
import com.payroll.employee.service.backend.enums.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

    @Query(value = "SELECT e.employee_id FROM employee_detail e ORDER BY e.employee_id DESC LIMIT 1", nativeQuery = true)
    Long findMaxEmployeeId();

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM EmployeeEntity e WHERE e.phone = :phone")
    boolean existsByPhone(@Param("phone") String phone);


    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN count(e) > 0 THEN true ELSE false END FROM EmployeeEntity e WHERE e.employeeId = :managerID AND e.role = :role")
    boolean existsByManagerID(@Param("managerID") Long managerID, @Param("role") Designation role);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.reportsTo= :managerID")
    List<EmployeeEntity> findAllByManagerID(@Param("managerID") Long managerID);
}

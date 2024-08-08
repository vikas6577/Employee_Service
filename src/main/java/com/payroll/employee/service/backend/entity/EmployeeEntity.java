package com.payroll.employee.service.backend.entity;

import com.payroll.employee.service.backend.enums.Designation;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "employee_detail"
)
public class EmployeeEntity {

    @Id
    @Column(
            name = "employee_id"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int employeeId;

    @Column(
            name="first_name",
            nullable = false
    )
    private String firstName;

    @Column(
            name ="last_name"
    )
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(
            nullable = false
    )
    private String phone;

    @Column(
            name = "birth_date",
            nullable = false
    )
    private LocalDate DOB;

    @Column(nullable = false)
    private Designation role;


    private Long reportsTo;

    @Column(nullable = false)
    private String password;
}

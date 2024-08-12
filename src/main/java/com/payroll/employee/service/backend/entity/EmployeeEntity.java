package com.payroll.employee.service.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.employee.service.backend.enums.Designation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "employee_detail"
)
@Builder
public class EmployeeEntity {

    @Id
    @Column(
            name = "employee_id"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long employeeId;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false)
    private Designation role;


    private Long reportsTo;

    @Column(nullable = false)
    private String password;
}

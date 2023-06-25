package org.bhavani.constructions.dao.entities.subentities;

import lombok.*;
import org.bhavani.constructions.dao.entities.models.EmployeeType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendancePK implements Serializable {

    @Column(name = "name")
    private String employeeName;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "site")
    private String site;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;
}

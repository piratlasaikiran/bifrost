package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.EmployeeType;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetAllEmployeeAttendances",
                query = "select E from EmployeeAttendanceEntity E"),
        @NamedQuery(name = "GetEmployeeAttendancesForEmployee",
                query = "select E from EmployeeAttendanceEntity E where E.employeeName = :employee_name"),
        @NamedQuery(name = "GetEmployeeAttendancesInSite",
                query = "select E from EmployeeAttendanceEntity E where E.site = :site_name")
})
@Table(name = "employee_attendance")
public class EmployeeAttendanceEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String employeeName;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "site")
    private String site;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_type")
    private AttendanceType attendanceType;

    @Column(name = "entered_by")
    private String enteredBy;

    @Column(name = "make_transaction")
    private boolean makeTransaction;

    @Column(name = "bank_account")
    private String bankAccount;
}

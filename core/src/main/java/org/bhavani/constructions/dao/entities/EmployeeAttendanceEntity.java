package org.bhavani.constructions.dao.entities;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;
import org.bhavani.constructions.dao.entities.subentities.EmployeeAttendancePK;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetAllEmployeeAttendances",
                query = "select E from EmployeeAttendanceEntity E")
})
@Table(name = "employee_attendance")
public class EmployeeAttendanceEntity extends BaseEntity {

    @EmbeddedId
    EmployeeAttendancePK employeeAttendancePK;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    private AttendanceType attendanceType;

    @Column(name = "entered_by")
    private String enteredBy;
}

package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;

import java.util.List;

public interface EmployeeAttendanceDao {
    void enterAttendance(EmployeeAttendanceEntity employeeAttendanceEntity);

    List<EmployeeAttendanceEntity> getAllEmployeeAttendance();
}

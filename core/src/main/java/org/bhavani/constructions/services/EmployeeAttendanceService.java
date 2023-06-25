package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;

import java.util.EnumSet;

public interface EmployeeAttendanceService {
    EmployeeAttendanceEntity enterAttendance(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO, String userId);

    EnumSet<AttendanceType> getAttendanceTypes();
}

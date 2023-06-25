package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;

public interface VendorAttendanceService {
    VendorAttendanceEntity enterAttendance(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, String userId);
}

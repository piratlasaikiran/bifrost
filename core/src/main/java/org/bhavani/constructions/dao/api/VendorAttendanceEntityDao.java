package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;

import java.util.List;
import java.util.Optional;

public interface VendorAttendanceEntityDao {
    void enterAttendance(VendorAttendanceEntity vendorAttendanceEntity);

    List<VendorAttendanceEntity> getAllVendorAttendance();

    Optional<VendorAttendanceEntity> getVendorAttendance(Long existingVendorAttendanceId);
}

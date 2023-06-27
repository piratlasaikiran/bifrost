package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;

import java.util.List;

public interface VendorAttendanceEntityDao {
    void enterAttendance(VendorAttendanceEntity vendorAttendanceEntity);

    List<VendorAttendanceEntity> getAllVendorAttendance();
}

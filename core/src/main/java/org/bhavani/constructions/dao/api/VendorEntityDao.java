package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.VendorEntity;

import java.util.List;
import java.util.Optional;

public interface VendorEntityDao {
    Optional<VendorEntity> getVendorById(String vendorId);

    Optional<VendorEntity> getVendorByName(String vendorName);

    void saveSupervisor(VendorEntity vendorEntity);

    List<VendorEntity> getAllVendors();

    List<VendorEntity> getVendorsInSite(String siteName);
}

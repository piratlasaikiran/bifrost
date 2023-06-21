package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;

import java.io.InputStream;
import java.util.List;

public interface VendorService {
    VendorEntity createVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDoc, String userId);

    List<CreateVendorRequestDTO> getVendors();

    List<String> getVendorIds();
}

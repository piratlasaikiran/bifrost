package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;
import org.bhavani.constructions.resources.DriverResource;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface VendorService {
    VendorEntity createVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDoc, FormDataContentDisposition contractDocumentContent, String userId);

    List<CreateVendorRequestDTO> getVendors();

    List<String> getVendorIds();

    Map<CommodityType, String> getAttendanceUnits(String vendorId);

    VendorEntity getVendor(String vendorId);

    VendorEntity updateVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDocument,
                              FormDataContentDisposition contractDocumentContent, String userId, String vendorId);
}

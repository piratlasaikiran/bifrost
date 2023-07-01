package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.VendorEntityDao;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;
import org.bhavani.constructions.services.VendorService;
import org.bhavani.constructions.utils.EntityBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.COMMODITY_ATTENDANCE_UNITS;
import static org.bhavani.constructions.constants.ErrorConstants.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DefaultVendorService implements VendorService {

    private final VendorEntityDao vendorEntityDao;

    @Override
    public VendorEntity createVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDoc, String userId) {
        try{
            VendorEntity vendorEntity = EntityBuilder.createVendorEntity(createVendorRequestDTO, contractDoc, userId);
            vendorEntityDao.getVendor(createVendorRequestDTO.getVendorId()).ifPresent(existingVendor -> {
                log.error("{} already present. Use different ID.", existingVendor.getVendorId());
                throw new IllegalArgumentException(USER_EXISTS);
            });
            vendorEntityDao.saveSupervisor(vendorEntity);
            return vendorEntity;
        }catch (IOException ioException){
            log.error("Error while parsing contract document");
            throw new RuntimeException(DOC_PARSING_ERROR);
        }
    }

    @Override
    public List<CreateVendorRequestDTO> getVendors() {
        List<VendorEntity> vendorEntities = getVendorEntities();
        List<CreateVendorRequestDTO> vendorRequestDTOS = new ArrayList<>();
        vendorEntities.forEach(vendorEntity -> vendorRequestDTOS.add(CreateVendorRequestDTO.builder()
                        .vendorId(vendorEntity.getVendorId())
                        .purpose(vendorEntity.getPurpose())
                        .location(vendorEntity.getLocation())
                        .mobileNumber(vendorEntity.getMobileNumber())
                        .commodityCosts(vendorEntity.getCommodityCosts())
                        .build()));
        return vendorRequestDTOS;
    }

    @Override
    public List<String> getVendorIds() {
        List<VendorEntity> vendorEntities = getVendorEntities();
        return vendorEntities.stream().map(VendorEntity::getVendorId).collect(Collectors.toList());
    }

    @Override
    public Map<CommodityType, String> getAttendanceUnits(String vendorId) {
        VendorEntity vendorEntity = vendorEntityDao.getVendor(vendorId).orElseThrow(() -> {
            log.error("Vendor with ID: {} not found", vendorId);
            return new RuntimeException(VENDOR_NOT_FOUND);
        });
        Map<CommodityType, String> vendorAttendanceUnits = new HashMap<>();
        vendorEntity.getCommodityCosts().forEach((commodity, cost) -> {
            vendorAttendanceUnits.put(commodity, COMMODITY_ATTENDANCE_UNITS.get(commodity));
        });
        return vendorAttendanceUnits;
    }

    @Override
    public VendorEntity getVendor(String vendorId) {
        Optional<VendorEntity> vendorEntity = vendorEntityDao.getVendor(vendorId);
        if(!vendorEntity.isPresent()){
            log.info("No vendor with ID: {}", vendorId);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return vendorEntity.get();
    }

    @Override
    public VendorEntity updateVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDocument, String userId) {
        VendorEntity vendorEntity = getVendor(createVendorRequestDTO.getVendorId());
        updateVendorDate(vendorEntity, createVendorRequestDTO, contractDocument, userId);
        return vendorEntity;
    }

    private void updateVendorDate(VendorEntity vendorEntity, CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDocument, String userId) {
        try{
            vendorEntity.setLocation(createVendorRequestDTO.getLocation());
            vendorEntity.setPurpose(createVendorRequestDTO.getPurpose());
            vendorEntity.setCommodityCosts(createVendorRequestDTO.getCommodityCosts());
            vendorEntity.setContractDocument(IOUtils.toByteArray(contractDocument));
            vendorEntity.setMobileNumber(createVendorRequestDTO.getMobileNumber());
            vendorEntity.setUpdatedBy(userId);
        }catch(IOException ioException){
            log.error("Error while updating vendor data");
            throw new RuntimeException(CORRUPTED_DATA);
        }
    }

    private List<VendorEntity> getVendorEntities() {
        return vendorEntityDao.getAllVendors();
    }
}

package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.VendorEntityDao;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;
import org.bhavani.constructions.services.VendorService;
import org.bhavani.constructions.utils.AWSS3Util;
import org.bhavani.constructions.utils.EntityBuilder;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.COMMODITY_ATTENDANCE_UNITS;
import static org.bhavani.constructions.constants.Constants.VENDOR_CONTRACT_FOLDER;
import static org.bhavani.constructions.constants.ErrorConstants.*;
import static org.bhavani.constructions.utils.EntityBuilder.convertListToCommaSeparatedString;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DefaultVendorService implements VendorService {

    private final VendorEntityDao vendorEntityDao;

    @Override
    public VendorEntity createVendor(CreateVendorRequestDTO createVendorRequestDTO,
                                     InputStream contractDoc, FormDataContentDisposition contractDocumentContent,
                                     String userId) {
        String contractDocLocationS3 = AWSS3Util.uploadToAWSS3(contractDoc, contractDocumentContent.getFileName(), VENDOR_CONTRACT_FOLDER);
        VendorEntity vendorEntity = EntityBuilder.createVendorEntity(createVendorRequestDTO, contractDocLocationS3, userId);
        vendorEntityDao.getVendorById(createVendorRequestDTO.getVendorId()).ifPresent(existingVendor -> {
            log.error("{} already present. Use different ID.", existingVendor.getVendorId());
            throw new IllegalArgumentException(USER_EXISTS);
        });
        vendorEntityDao.saveSupervisor(vendorEntity);
        return vendorEntity;
    }

    @Override
    public List<CreateVendorRequestDTO> getVendors() {
        List<VendorEntity> vendorEntities = getVendorEntities();
        List<CreateVendorRequestDTO> vendorRequestDTOS = new ArrayList<>();
        vendorEntities.forEach(vendorEntity -> vendorRequestDTOS.add(CreateVendorRequestDTO.builder()
                        .name(vendorEntity.getName())
                        .vendorId(vendorEntity.getVendorId())
                        .purposes(vendorEntity.getPurposes())
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
        VendorEntity vendorEntity = vendorEntityDao.getVendorById(vendorId).orElseThrow(() -> {
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
        Optional<VendorEntity> vendorEntity = vendorEntityDao.getVendorById(vendorId);
        if(!vendorEntity.isPresent()){
            log.info("No vendor with ID: {}", vendorId);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return vendorEntity.get();
    }

    @Override
    public VendorEntity updateVendor(CreateVendorRequestDTO createVendorRequestDTO, InputStream contractDocument,
                                     FormDataContentDisposition contractDocumentContent, String userId, String vendorId) {
        VendorEntity vendorEntity = getVendor(vendorId);
        if(!vendorId.equals(createVendorRequestDTO.getVendorId())) {
            vendorEntityDao.getVendorById(createVendorRequestDTO.getVendorId()).ifPresent(newVendorEntity -> {
                log.error("User: {} already exists", createVendorRequestDTO.getVendorId());
                throw new IllegalArgumentException(USER_EXISTS);
            });
        }
        String updatedContractDocS3Location = AWSS3Util.updateDocInAWS(vendorEntity.getContractDocument(), contractDocument, contractDocumentContent.getFileName(), VENDOR_CONTRACT_FOLDER);
        updateVendorData(vendorEntity, createVendorRequestDTO, updatedContractDocS3Location, userId);
        return vendorEntity;
    }

    private void updateVendorData(VendorEntity vendorEntity, CreateVendorRequestDTO createVendorRequestDTO, String updatedContractDocS3Location, String userId) {
        vendorEntity.setName(createVendorRequestDTO.getName());
        vendorEntity.setVendorId(createVendorRequestDTO.getVendorId());
        vendorEntity.setLocation(createVendorRequestDTO.getLocation());
        vendorEntity.setPurposes(convertListToCommaSeparatedString(createVendorRequestDTO.getPurposes().stream().map(Enum::toString).collect(Collectors.toList())));
        vendorEntity.setCommodityCosts(createVendorRequestDTO.getCommodityCosts());
        vendorEntity.setContractDocument(updatedContractDocS3Location);
        vendorEntity.setMobileNumber(createVendorRequestDTO.getMobileNumber());
        vendorEntity.setUpdatedBy(userId);
    }

    private List<VendorEntity> getVendorEntities() {
        return vendorEntityDao.getAllVendors();
    }
}

package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.VendorEntityDao;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;
import org.bhavani.constructions.services.VendorService;
import org.bhavani.constructions.utils.EntityBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;
import static org.bhavani.constructions.constants.ErrorConstants.USER_EXISTS;

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

    private List<VendorEntity> getVendorEntities() {
        return vendorEntityDao.getAllVendors();
    }
}

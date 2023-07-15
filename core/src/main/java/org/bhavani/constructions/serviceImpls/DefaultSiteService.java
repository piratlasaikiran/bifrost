package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dto.CreateSiteRequestDTO;
import org.bhavani.constructions.services.SiteService;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bhavani.constructions.constants.ErrorConstants.SITE_EXISTS;
import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.utils.EntityBuilder.convertListToCommaSeparatedString;
import static org.bhavani.constructions.utils.EntityBuilder.createSiteEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultSiteService implements SiteService {

    private final SiteEntityDao siteEntityDao;
    private final EmployeeAttendanceDao employeeAttendanceDao;
    private final AssetLocationEntityDao assetLocationEntityDao;
    private final VendorAttendanceEntityDao vendorAttendanceEntityDao;
    private final VendorEntityDao vendorEntityDao;
    private final TransactionEntityDao transactionEntityDao;

    @Override
    public SiteEntity createSite(CreateSiteRequestDTO createSiteRequestDTO) {
        SiteEntity siteEntity = createSiteEntity(createSiteRequestDTO);
        siteEntityDao.saveSite(siteEntity);
        log.info("Site: {} saved successfully", siteEntity.getSiteName());
        return siteEntity;
    }

    @Override
    public SiteEntity getSite(String siteName) {
        Optional<SiteEntity> siteEntity = siteEntityDao.getSite(siteName);
        if(!siteEntity.isPresent()) {
            log.error("Site: {} not found", siteName);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return siteEntity.get();
    }

    @Override
    public SiteEntity updateSite(String siteName, CreateSiteRequestDTO createSiteRequestDTO, String userId) {
        Optional<SiteEntity> siteEntity = siteEntityDao.getSite(siteName);
        if(!siteEntity.isPresent()){
            log.error("Site: {} not found", siteName);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        if(!siteName.equals(createSiteRequestDTO.getSiteName())){
            siteEntityDao.getSite(createSiteRequestDTO.getSiteName()).ifPresent(newSiteEntity -> {
                log.error("Site: {} already exists. Choose another name.", createSiteRequestDTO.getSiteName());
                throw new IllegalArgumentException(SITE_EXISTS);
            });
            updateDependentEntities(siteName, createSiteRequestDTO.getSiteName());
        }
        SiteEntity updatedSiteEntity = siteEntity.get();
        updatedSiteEntity.setSiteName(createSiteRequestDTO.getSiteName());
        updatedSiteEntity.setAddress(createSiteRequestDTO.getAddress());
        updatedSiteEntity.setSupervisors(convertListToCommaSeparatedString(createSiteRequestDTO.getSupervisors()));
        updatedSiteEntity.setVehicles(convertListToCommaSeparatedString(createSiteRequestDTO.getVehicles()));
        updatedSiteEntity.setCurrentStatus(createSiteRequestDTO.getSiteStatus());
        updatedSiteEntity.setWorkStartDate(createSiteRequestDTO.getWorkStartDate());
        updatedSiteEntity.setWorkEndDate(createSiteRequestDTO.getWorkEndDate());
        return updatedSiteEntity;
    }

    private void updateDependentEntities(String oldSiteName, String newSiteName) {
        List<EmployeeAttendanceEntity> employeeAttendanceEntities = employeeAttendanceDao.getAttendancesInSite(oldSiteName);
        employeeAttendanceEntities.forEach(employeeAttendanceEntity -> employeeAttendanceEntity.setSite(newSiteName));

        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao.getAssetsInSite(oldSiteName);
        assetLocationEntities.forEach(assetLocationEntity -> assetLocationEntity.setLocation(newSiteName));

        List<VendorAttendanceEntity> vendorAttendanceEntities = vendorAttendanceEntityDao.getVendorAttendanceInSite(oldSiteName);
        vendorAttendanceEntities.forEach(vendorAttendanceEntity -> vendorAttendanceEntity.setSite(newSiteName));

        List<VendorEntity> vendorEntities = vendorEntityDao.getVendorsInSite(oldSiteName);
        vendorEntities.forEach(vendorEntity -> vendorEntity.setLocation(newSiteName));

        List<TransactionEntity> transactionEntities = transactionEntityDao.getTransactionsBySiteName(oldSiteName);
        transactionEntities.forEach(transactionEntity -> transactionEntity.setSite(newSiteName));
    }

    @Override
    public List<CreateSiteRequestDTO> getSites() {
        List<SiteEntity> siteEntities = siteEntityDao.getSites();
        List<CreateSiteRequestDTO> siteRequestDTOS = new ArrayList<>();
        siteEntities.forEach(siteEntity -> siteRequestDTOS.add(CreateSiteRequestDTO.builder()
                        .siteName(siteEntity.getSiteName())
                        .address(siteEntity.getAddress())
                        .siteStatus(siteEntity.getCurrentStatus())
                        .supervisors(siteEntity.getSupervisors())
                        .vehicles(siteEntity.getVehicles())
                        .workStartDate(siteEntity.getWorkStartDate())
                        .workEndDate(siteEntity.getWorkEndDate())
                .build()));
        return siteRequestDTOS;
    }
}

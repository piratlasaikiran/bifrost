package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.AssetLocationEntityDao;
import org.bhavani.constructions.dao.api.AssetOwnershipEntityDao;
import org.bhavani.constructions.dao.api.SiteEntityDao;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.AssetOwnershipEntity;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.dto.CreateAssetOwnershipRequestDTO;
import org.bhavani.constructions.exceptions.OverlappingIntervalsException;
import org.bhavani.constructions.services.AssetManagementService;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bhavani.constructions.constants.Constants.VEHICLE;
import static org.bhavani.constructions.constants.ErrorConstants.*;
import static org.bhavani.constructions.utils.EntityBuilder.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DefaultAssetManagementService implements AssetManagementService {

    private final AssetLocationEntityDao assetLocationEntityDao;
    private final AssetOwnershipEntityDao assetOwnershipEntityDao;
    private final SiteEntityDao siteEntityDao;

    @Override
    public void saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO)
            throws OverlappingIntervalsException {
        AssetLocationEntity assetLocationEntity = createAssetLocationEntity(createAssetLocationRequestDTO);
        String assetName = createAssetLocationRequestDTO.getAssetName();
        SiteEntity siteEntity = siteEntityDao.getSite(createAssetLocationRequestDTO.getLocation()).orElseThrow(() -> {
            log.error("Error updating location. Site not found");
            return new RuntimeException(SITE_NOT_FOUND);
        });
        if(!isValidAssetLocationEntry(createAssetLocationRequestDTO)){
            log.error("Overlapping intervals.");
            throw new OverlappingIntervalsException(ASSET_LOCATION_INVALID);
        }

        if(createAssetLocationRequestDTO.getAssetType().equals(VEHICLE)){
            List<String> updatedVehicles = Stream.concat(siteEntity.getVehicles().stream(), Stream.of(assetName)).collect(Collectors.toList());
            siteEntity.setVehicles(convertListToCommaSeparatedString(updatedVehicles));
        }
        else{
            List<String> updatedSupervisors = Stream.concat(siteEntity.getSupervisors().stream(), Stream.of(assetName)).collect(Collectors.toList());
            siteEntity.setSupervisors(convertListToCommaSeparatedString(updatedSupervisors));
        }
        assetLocationEntityDao.saveAssetLocation(assetLocationEntity);
        log.info("Asset: {} at location: {} saved successfully", assetLocationEntity.getAssetName(), assetLocationEntity.getLocation());
    }

    @Override
    public AssetLocationEntity getAssetLocation(String assetName, String location, LocalDate startDate) {
        Optional<AssetLocationEntity> assetLocationEntity = assetLocationEntityDao.getAssetLocation(assetName, location, startDate);
        if(!assetLocationEntity.isPresent()){
            log.error("Asset: {} not found", assetName);
            throw new IllegalArgumentException();
        }
        return assetLocationEntity.get();
    }

    @Override
    public AssetLocationEntity updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, Long assetLocationId) throws OverlappingIntervalsException {
        if(!isValidAssetLocationEntry(createAssetLocationRequestDTO)){
            log.error("Overlapping intervals.");
            throw new OverlappingIntervalsException(ASSET_LOCATION_INVALID);
        }
        AssetLocationEntity assetLocationEntity = assetLocationEntityDao.getAssetLocation(assetLocationId).orElseThrow(() -> {
            log.error("AssetLocation with ID: {} doesn't exist. ", assetLocationId);
            return new IllegalArgumentException(ASSET_NOT_FOUND);
        });
        assetLocationEntity.setAssetType(createAssetLocationRequestDTO.getAssetType());
        assetLocationEntity.setAssetName(createAssetLocationRequestDTO.getAssetName());
        assetLocationEntity.setLocation(createAssetLocationRequestDTO.getLocation());
        assetLocationEntity.setStartDate(createAssetLocationRequestDTO.getStartDate());
        assetLocationEntity.setEndDate(createAssetLocationRequestDTO.getEndDate());
        return assetLocationEntity;
    }

    @Override
    public void deleteAssetLocation(String assetName, String assetLocation, LocalDate startDate) {
        Optional<AssetLocationEntity> assetLocationEntity = assetLocationEntityDao.getAssetLocation(assetName, assetLocation, startDate);
        if(!assetLocationEntity.isPresent()){
            log.error("Asset: {} not found", assetName);
            throw new IllegalArgumentException();
        }
        assetLocationEntityDao.deleteAssetLocationEntity(assetLocationEntity.get());
        log.info("Asset: {} deleted", assetName);
    }

    @Override
    public List<CreateAssetLocationRequestDTO> getAssetsLocation() {
        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao.getAssetsLocation();
        List<CreateAssetLocationRequestDTO> assetLocationRequestDTOS = new ArrayList<>();
        assetLocationEntities.forEach(assetLocationEntity -> {
            assetLocationRequestDTOS.add(CreateAssetLocationRequestDTO.builder()
                            .assetLocationId(assetLocationEntity.getId())
                            .assetName(assetLocationEntity.getAssetName())
                            .assetType(assetLocationEntity.getAssetType())
                            .location(assetLocationEntity.getLocation())
                            .startDate(assetLocationEntity.getStartDate())
                            .endDate(assetLocationEntity.getEndDate())
                    .build());
        });
        return assetLocationRequestDTOS;
    }

    @Override
    public void saveAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, String userId) throws OverlappingIntervalsException {
        AssetOwnershipEntity assetOwnershipEntity = createAssetOwnershipEntity(createAssetOwnershipRequestDTO, userId);
        if(!isValidAssetOwnershipEntry(createAssetOwnershipRequestDTO)){
            log.error("Overlapping intervals.");
            throw new OverlappingIntervalsException(ASSET_OWNERSHIP_INVALID);
        }
        assetOwnershipEntityDao.saveAssetOwnership(assetOwnershipEntity);
    }

    @Override
    public List<CreateAssetOwnershipRequestDTO> getAssetsOwnership() {
        List<AssetOwnershipEntity> assetOwnershipEntities = assetOwnershipEntityDao.getAssetsOwnership();
        List<CreateAssetOwnershipRequestDTO> assetOwnershipRequestDTOS = new ArrayList<>();
        assetOwnershipEntities.forEach(assetsOwnership -> {
            assetOwnershipRequestDTOS.add(CreateAssetOwnershipRequestDTO.builder()
                    .assetOwnershipId(assetsOwnership.getId())
                    .assetName(assetsOwnership.getAssetName())
                    .assetType(assetsOwnership.getAssetType())
                    .currentOwner(assetsOwnership.getCurrentOwner())
                    .startDate(assetsOwnership.getStartDate())
                    .endDate(assetsOwnership.getEndDate())
                    .build());
        });
        return assetOwnershipRequestDTOS;
    }

    @Override
    public AssetOwnershipEntity updateAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, Long assetOwnershipId) throws OverlappingIntervalsException {
        if(!isValidAssetOwnershipEntry(createAssetOwnershipRequestDTO)){
            log.error("Overlapping intervals.");
            throw new OverlappingIntervalsException(ASSET_LOCATION_INVALID);
        }
        AssetOwnershipEntity assetOwnershipEntity = assetOwnershipEntityDao.getAssetOwnership(assetOwnershipId).orElseThrow(() -> {
            log.error("AssetOwnership with ID: {} doesn't exist. ", assetOwnershipId);
            return new IllegalArgumentException(ASSET_NOT_FOUND);
        });
        assetOwnershipEntity.setAssetType(createAssetOwnershipRequestDTO.getAssetType());
        assetOwnershipEntity.setAssetName(createAssetOwnershipRequestDTO.getAssetName());
        assetOwnershipEntity.setCurrentOwner(createAssetOwnershipRequestDTO.getCurrentOwner());
        assetOwnershipEntity.setStartDate(createAssetOwnershipRequestDTO.getStartDate());
        assetOwnershipEntity.setEndDate(createAssetOwnershipRequestDTO.getEndDate());
        return assetOwnershipEntity;
    }

    private boolean isValidAssetOwnershipEntry(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO) {
        List<AssetOwnershipEntity> assetOwnershipEntities = assetOwnershipEntityDao
                .getAssetOwnershipEntities(createAssetOwnershipRequestDTO.getAssetName())
                .stream().filter(assetOwnershipEntity -> !Objects.equals(assetOwnershipEntity.getId(), createAssetOwnershipRequestDTO.getAssetOwnershipId()))
                .collect(Collectors.toList());
        for(AssetOwnershipEntity assetOwnershipEntity : assetOwnershipEntities){
            if(overlappingExistence(assetOwnershipEntity.getStartDate(), assetOwnershipEntity.getEndDate(),
                    createAssetOwnershipRequestDTO.getStartDate(), createAssetOwnershipRequestDTO.getEndDate()))
                return false;
        }
        return true;
    }

    private boolean isValidAssetLocationEntry(CreateAssetLocationRequestDTO createAssetLocationRequestDTO){
        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao
                .getAssetLocationEntities(createAssetLocationRequestDTO.getAssetName())
                .stream().filter(assetLocationEntity -> !Objects.equals(assetLocationEntity.getId(), createAssetLocationRequestDTO.getAssetLocationId()))
                .collect(Collectors.toList());
        for(AssetLocationEntity assetLocationEntity : assetLocationEntities){
            if(overlappingExistence(assetLocationEntity.getStartDate(), assetLocationEntity.getEndDate(),
                    createAssetLocationRequestDTO.getStartDate(), createAssetLocationRequestDTO.getEndDate()))
                return false;
        }
        return true;
    }

    private boolean overlappingExistence(LocalDate historicalStartDate, LocalDate historicalEndDate,  LocalDate currentEntityStartDate, LocalDate currentEntityEndDate) {
        if (Objects.isNull(currentEntityEndDate)) {
            currentEntityEndDate = LocalDate.now();
        }
        if (Objects.isNull(historicalEndDate)) {
            historicalEndDate = LocalDate.now();
        }
        return currentEntityStartDate.isBefore(historicalEndDate) && historicalStartDate.isBefore(currentEntityEndDate);
    }
}

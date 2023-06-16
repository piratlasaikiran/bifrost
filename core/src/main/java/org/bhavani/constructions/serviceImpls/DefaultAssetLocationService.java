package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.AssetLocationEntityDao;
import org.bhavani.constructions.dao.api.SiteEntityDao;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.exceptions.OverlappingIntervalsException;
import org.bhavani.constructions.services.AssetLocationService;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.VEHICLE;
import static org.bhavani.constructions.constants.ErrorConstants.ASSET_LOCATION_INVALID;
import static org.bhavani.constructions.constants.ErrorConstants.SITE_NOT_FOUND;
import static org.bhavani.constructions.utils.EntityBuilder.createAssetLocationEntity;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DefaultAssetLocationService implements AssetLocationService {

    private final AssetLocationEntityDao assetLocationEntityDao;
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
            siteEntity.getVehicles().add(assetName);
        }
        else{
            siteEntity.getSupervisors().add(assetName);
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
    public AssetLocationEntity updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, String assetName) throws OverlappingIntervalsException {
        if(!isValidAssetLocationEntry(createAssetLocationRequestDTO)){
            log.error("Overlapping intervals.");
            throw new OverlappingIntervalsException(ASSET_LOCATION_INVALID);
        }
        AssetLocationEntity assetLocationEntity = getAssetLocation(assetName, createAssetLocationRequestDTO.getLocation(), createAssetLocationRequestDTO.getStartDate());
        assetLocationEntity.setAssetType(createAssetLocationRequestDTO.getAssetType());
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
                            .assetName(assetLocationEntity.getAssetName())
                            .assetType(assetLocationEntity.getAssetType())
                            .location(assetLocationEntity.getLocation())
                            .startDate(assetLocationEntity.getStartDate())
                            .endDate(assetLocationEntity.getEndDate())
                    .build());
        });
        return assetLocationRequestDTOS;
    }

    private boolean isValidAssetLocationEntry(CreateAssetLocationRequestDTO createAssetLocationRequestDTO){
        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao
                .getAssetLocationEntities(createAssetLocationRequestDTO.getAssetName());
        for(AssetLocationEntity assetLocationEntity : assetLocationEntities){
            if(overlappingExistence(assetLocationEntity, createAssetLocationRequestDTO))
                return false;
        }
        return true;
    }

    private boolean overlappingExistence(AssetLocationEntity assetLocationEntity, CreateAssetLocationRequestDTO createAssetLocationRequestDTO) {
        LocalDate historicalStartDate = assetLocationEntity.getStartDate();
        LocalDate historicalEndDate = assetLocationEntity.getEndDate();
        LocalDate currentEntityStartDate = createAssetLocationRequestDTO.getStartDate();
        LocalDate currentEntityEndDate = createAssetLocationRequestDTO.getEndDate();

        if((historicalStartDate.isBefore(currentEntityStartDate) && historicalEndDate.isAfter(currentEntityEndDate)) ||
                (currentEntityStartDate.isBefore(historicalStartDate) && currentEntityEndDate.isAfter(historicalEndDate))  ||
                (currentEntityStartDate.isBefore(historicalStartDate) && currentEntityEndDate.isBefore(historicalEndDate)) ||
                (currentEntityStartDate.isEqual(historicalStartDate)) || currentEntityEndDate.isEqual(historicalEndDate))
            return true;
        return false;
    }
}

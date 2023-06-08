package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.AssetLocationEntityDao;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.services.AssetLocationService;

import javax.inject.Inject;

import java.util.Optional;

import static org.bhavani.constructions.utils.EntityBuilder.createAssetLocationEntity;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DefaultAssetLocationService implements AssetLocationService {

    private final AssetLocationEntityDao assetLocationEntityDao;

    @Override
    public void saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO) {
        AssetLocationEntity assetLocationEntity = createAssetLocationEntity(createAssetLocationRequestDTO);
        assetLocationEntityDao.saveAssetLocation(assetLocationEntity);
        log.info("Asset: {} at location: {} saved successfully", assetLocationEntity.getAssetName(), assetLocationEntity.getLocation());
    }

    @Override
    public AssetLocationEntity getAssetLocation(String assetName) {
        Optional<AssetLocationEntity> assetLocationEntity = assetLocationEntityDao.getAssetLocation(assetName);
        if(!assetLocationEntity.isPresent()){
            log.error("Asset: {} not found", assetName);
            throw new IllegalArgumentException();
        }
        return assetLocationEntity.get();
    }

    @Override
    public AssetLocationEntity updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, String assetName) {
        AssetLocationEntity assetLocationEntity = getAssetLocation(assetName);
        assetLocationEntity.setAssetType(createAssetLocationRequestDTO.getAssetType());
        assetLocationEntity.setLocation(createAssetLocationRequestDTO.getLocation());
        assetLocationEntity.setStartDate(createAssetLocationRequestDTO.getStartDate());
        assetLocationEntity.setEndDate(createAssetLocationRequestDTO.getEndDate());
        return assetLocationEntity;
    }

    @Override
    public void deleteAssetLocation(String assetName, String assetLocation, String startDate) {
        Optional<AssetLocationEntity> assetLocationEntity = assetLocationEntityDao.getAssetLocation(assetName, assetLocation, startDate);
        if(!assetLocationEntity.isPresent()){
            log.error("Asset: {} not found", assetName);
            throw new IllegalArgumentException();
        }
        assetLocationEntityDao.deleteAssetLocationEntity(assetLocationEntity.get());
        log.info("Asset: {} deleted", assetName);
    }
}

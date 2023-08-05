package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.AssetLocationEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssetLocationEntityDao {
    Optional<AssetLocationEntity> getAssetLocation(Long assetLocationId);

    Optional<AssetLocationEntity> getAssetLocation(String assetName, String assetLocation, LocalDate startDate);

    void saveAssetLocation(AssetLocationEntity assetLocationEntity);


    void deleteAssetLocationEntity(AssetLocationEntity assetLocationEntity);

    List<AssetLocationEntity> getAssetsLocation();

    List<AssetLocationEntity> getAssetLocationEntities(String assetName);

    List<AssetLocationEntity> getAssetsInSite(String siteName);
}

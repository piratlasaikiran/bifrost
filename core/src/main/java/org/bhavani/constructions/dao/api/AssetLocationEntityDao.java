package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.AssetLocationEntity;

import java.util.Optional;

public interface AssetLocationEntityDao {
    Optional<AssetLocationEntity> getAssetLocation(String assetName);

    Optional<AssetLocationEntity> getAssetLocation(String assetName, String assetLocation, String startDate);

    void saveAssetLocation(AssetLocationEntity assetLocationEntity);


    void deleteAssetLocationEntity(AssetLocationEntity assetLocationEntity);
}

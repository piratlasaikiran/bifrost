package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;

public interface AssetLocationService {

    void saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO);

    AssetLocationEntity getAssetLocation(String assetName);

    AssetLocationEntity updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, String assetName);

    void deleteAssetLocation(String assetName, String assetLocation, String startDate);
}

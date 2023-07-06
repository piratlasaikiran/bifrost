package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.AssetOwnershipEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.dto.CreateAssetOwnershipRequestDTO;
import org.bhavani.constructions.exceptions.OverlappingIntervalsException;

import java.time.LocalDate;
import java.util.List;

public interface AssetManagementService {

    void saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO) throws OverlappingIntervalsException;

    AssetLocationEntity getAssetLocation(String assetName, String location, LocalDate startDate);

    AssetLocationEntity updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, Long assetLocationId) throws OverlappingIntervalsException;

    void deleteAssetLocation(String assetName, String assetLocation, LocalDate startDate);

    List<CreateAssetLocationRequestDTO> getAssetsLocation();

    void saveAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, String userId) throws OverlappingIntervalsException;

    List<CreateAssetOwnershipRequestDTO> getAssetsOwnership();

    AssetOwnershipEntity updateAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, Long assetOwnershipId) throws OverlappingIntervalsException;
}

package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.AssetOwnershipEntity;

import java.util.List;
import java.util.Optional;

public interface AssetOwnershipEntityDao {

    Optional<AssetOwnershipEntity> getAssetOwnership(Long assetOwnershipId);


    void saveAssetOwnership(AssetOwnershipEntity assetOwnershipEntity);

    List<AssetOwnershipEntity> getAssetsOwnership();

    void deleteAssetOwnershipEntity(AssetOwnershipEntity assetOwnershipEntity);

    List<AssetOwnershipEntity> getAssetOwnershipEntities(String assetName);

    List<AssetOwnershipEntity> getAssetOwnershipEntitiesByOwnerName(String ownerName);
}

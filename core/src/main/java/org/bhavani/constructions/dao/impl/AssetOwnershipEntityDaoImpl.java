package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.AssetOwnershipEntityDao;
import org.bhavani.constructions.dao.entities.AssetOwnershipEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.ASSET_NAME;
import static org.bhavani.constructions.constants.Constants.EMPLOYEE_NAME;

@Slf4j
public class AssetOwnershipEntityDaoImpl extends AbstractDAO<AssetOwnershipEntity> implements AssetOwnershipEntityDao {

    @Inject
    public AssetOwnershipEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public Optional<AssetOwnershipEntity> getAssetOwnership(Long assetOwnershipId) {
        return this.get(assetOwnershipId);
    }

    @Override
    public void saveAssetOwnership(AssetOwnershipEntity assetOwnershipEntity) {
        this.currentSession().save(assetOwnershipEntity);
    }

    @Override
    public List<AssetOwnershipEntity> getAssetsOwnership() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllAssetOwnerships",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public void deleteAssetOwnershipEntity(AssetOwnershipEntity assetOwnershipEntity) {
        this.currentSession().delete(assetOwnershipEntity);
    }

    @Override
    public List<AssetOwnershipEntity> getAssetOwnershipEntities(String assetName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ASSET_NAME, assetName);
        log.info("Fetching asset: {}", assetName);
        return findAllByNamedQuery("GetAssetOwnershipsByName",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<AssetOwnershipEntity> getAssetOwnershipEntitiesByOwnerName(String ownerName) {
        Map<String, Object> params = new HashMap<>();
        params.put(EMPLOYEE_NAME, ownerName);
        log.info("Fetching asset with owner: {}", ownerName);
        return findAllByNamedQuery("GetAssetOwnershipsByOwner",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}

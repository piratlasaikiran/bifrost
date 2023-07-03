package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.AssetLocationEntityDao;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.*;

@Slf4j
public class AssetLocationEntityDaoImpl extends AbstractDAO<AssetLocationEntity> implements AssetLocationEntityDao {

    @Inject
    public AssetLocationEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<AssetLocationEntity> getAssetLocation(Long assetLocationId) {
        return this.get(assetLocationId);
    }

    @Override
    public Optional<AssetLocationEntity> getAssetLocation(String assetName, String assetLocation, LocalDate startDate) {
        Map<String, Object> params = new HashMap<>();
        params.put(ASSET_NAME, assetName);
        params.put(ASSET_LOCATION, assetLocation);
        params.put(START_DATE, startDate);
        log.info("Fetching asset: {}", assetName);
        return findOneByNamedQuery("GetAssetLocationByName.Location.Date", params);
    }

    @Override
    public void saveAssetLocation(AssetLocationEntity assetLocationEntity) {
        this.currentSession().save(assetLocationEntity);
    }

    @Override
    public void deleteAssetLocationEntity(AssetLocationEntity assetLocationEntity) {
        this.currentSession().delete(assetLocationEntity);
    }

    @Override
    public List<AssetLocationEntity> getAssetsLocation() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllAssetLocations",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

    @Override
    public List<AssetLocationEntity> getAssetLocationEntities(String assetName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ASSET_NAME, assetName);
        return findAllByNamedQuery("GetAssetLocationByName",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }

}

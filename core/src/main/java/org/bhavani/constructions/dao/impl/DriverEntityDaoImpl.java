package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.NAME;

@Slf4j
public class DriverEntityDaoImpl extends AbstractDAO<DriverEntity> implements DriverEntityDao {

    @Inject
    public DriverEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<DriverEntity> getDriver(String driverName) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, driverName);
        log.info("Fetching driver: {}", driverName);
        return findOneByNamedQuery("GetDriverByName", params);
    }

    @Override
    public void saveDriver(DriverEntity driverEntity) {
        log.info("Saving driver: {}", driverEntity.getName());
        persist(driverEntity);
    }

    @Override
    public void deleteDriver(DriverEntity driverEntity) {
        currentSession().delete(driverEntity);
    }

    @Override
    public List<DriverEntity> getDrivers() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllDrivers",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}

package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
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
    public Optional<DriverEntity> getEmployee(String driverName) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, driverName);
        log.info("Fetching driver: {}", driverName);
        return findOneByNamedQuery("GetDriverByName", params);
    }

    @Override
    public void saveEmployee(DriverEntity driver) {
        log.info("Saving driver: {}", driver.getName());
        persist(driver);
    }

    @Override
    public void updateEmployee(DriverEntity driver) {

    }

    @Override
    public void deleteEmployee(DriverEntity driver) {

    }
}
